package mil;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
public class MILProgram {

    /** Construct an empty MIL program.
     */
    public MILProgram() {
        // TODO: Nothing to do here?  Remove this constructor?
    }

    /** Add a special block for aborting the program.
     */
    public static final Block abort = new Block(Halt.obj);

    /** Stores a list of the entry points for this program.
     */
    private Defns entries = null;

    /** Add an entry point for this program, if it is not already included.
     */
    public void addEntry(Defn defn) {
        if (!Defns.isIn(defn, entries)) {
            entries = new Defns(defn, entries);
        }
    }

    /** Record the list of strongly connected components in this program.
     */
    private DefnSCCs sccs;

    /** Compute the list of definitions for the reachable portion of the input graph.
     */
    public Defns reachable() {
        Defn.newDFS();                     // Begin a new depth-first search
        Defns defns = null;                // Compute a list of reachable Defns
        for (Defns ds=entries; ds!=null; ds=ds.next) {
            defns = ds.head.visitDepends(defns);
        }
        if (defns==null) {
            System.out.println("No definitions remain");
        }
  
  //!for (Defns ds=defns; ds!=null; ds=ds.next) {
  //!  ds.head.describe();
  //!}
        return defns;
    }

    /** Perform tree shaking on this program, computing strongly-connected components
     *  for the reachable portion of the input graph.
     */
    public void shake() {
        sccs = Defns.searchReverse(reachable()); // Compute the strongly-connected components
    }
    public void toDot(String name) {
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(name)));
            out.println("digraph MIL {");
            for (Defns es=entries; es!=null; es=es.next) {
                out.println("  " + es.head.getId() + ";");
            }
            for (Defns ds=reachable(); ds!=null; ds=ds.next) {
                for (Defns cs=ds.head.getCallers(); cs!=null; cs=cs.next) {
                    out.println("  " + cs.head.getId() + " -> " + ds.head.getId() + ";");
                }
            }
            out.println("}");
            out.close();
        } catch (IOException e) {
            System.out.println("Attempt to create dot output file " + name + " failed");
        }
    }

    /** Display a printable representation of this MIL construct
     *  on the standard output.
     */
    public void display() {
        for (DefnSCCs dsccs = sccs; dsccs!=null; dsccs=dsccs.next) {
            dsccs.head.display();
        }
    }
    public static int count = 0;
    public static int count_g = 0;
    public static void report(String msg) {
        System.out.println(msg);
        count++;
      }
    public void optimize() {
        shake();
        cfunSimplify();
    
        int lastcount;
        
        int outerIterator = 1;        
        do {
            count = 0;
            count_g = 0;
            int i=1;
            do {
              debug.Log.println("-------------------------");
        //!System.out.println("==================================================");
        //!System.out.println("Step " + i);
        //!System.out.println("==================================================");
        //!display();
        //!System.out.println("==================================================");
              lastcount = count;
              count = 0;
              inlining();
              debug.Log.println("Inlining pass finished, running shake.");
              shake();
              liftAllocators();  // TODO: Is this the right position for liftAllocators?
              flow();
              debug.Log.println("Flow pass finished, running shake.");
              shake();
              debug.Log.println("Steps performed = " + count);
              ++i;
            } while (i < 20 && count>0);
            //break;
            count = lastcount;
            int j=0;
            do {
                lastcount = count_g;
                count_g = 0;
                GlobalConstantPropagation();
                debug.Log.println("GlobalConstantPropagation pass finished, running shake.");
                shake();
                debug.Log.println("Steps performed = " + count_g);
                //display();
                ++j;
            } while (j<20 && count_g>0);
               count_g = lastcount;
            ++outerIterator;
        } while (outerIterator < 20 && count+count_g>0);

        debug.Log.println("Loops performed = " + outerIterator);
        
    }

    void cfunSimplify() {
        for (DefnSCCs dsccs = sccs; dsccs!=null; dsccs=dsccs.next) {
          for (Defns ds=dsccs.head.getBindings(); ds!=null; ds=ds.next) {
            ds.head.cfunSimplify();
          }
        }
      }

    /** Run a liftAllocators pass over this program, assuming a previous
     *  call to shake() to compute SCCs.
     */
    public void liftAllocators() {
        for (DefnSCCs dsccs = sccs; dsccs!=null; dsccs=dsccs.next) {
          for (Defns ds=dsccs.head.getBindings(); ds!=null; ds=ds.next) {
            ds.head.liftAllocators();
          }
        }
      }

    /** Run an inlining pass over this program.  Assumes a previous call
     *  to shake() to compute call graph information, and calls shake()
     *  automatically again at the end of the inlining pass to eliminate
     *  dead code and compute updated call graph information.
     */
    public void inlining() {
        for (DefnSCCs dsccs = sccs; dsccs!=null; dsccs=dsccs.next) {
    //      for (Defns ds=dsccs.head.getBindings(); ds!=null; ds=ds.next) {
    //        ds.head.cleanup();
    //      }
          for (Defns ds=dsccs.head.getBindings(); ds!=null; ds=ds.next) {
            ds.head.detectLoops(null);
          }
          for (Defns ds=dsccs.head.getBindings(); ds!=null; ds=ds.next) {
    //!System.out.println("inlining loop at: " + ds.head.getId());
            ds.head.inlining();
          }
        }
      }

    /** Run a flow pass over this program.  Assumes a previous call
     *  to shake() to compute call graph information, and calls inlining()
     *  automatically at the end of the flow pass, which in turn will call
     *  shake to eliminate dead code and compute updated call graph
     *  information.
     *SUSPECT COMMENT OUT OF DATE
     */
    public void flow() {
        for (DefnSCCs dsccs = sccs; dsccs!=null; dsccs=dsccs.next) {
          for (Defns ds=dsccs.head.getBindings(); ds!=null; ds=ds.next) {
            ds.head.flow();
          }
        }
      }
    public void analyzeCalls() {
        for (DefnSCCs dsccs = sccs; dsccs!=null; dsccs=dsccs.next) {
          for (Defns ds=dsccs.head.getBindings(); ds!=null; ds=ds.next) {
            ds.head.resetCallCounts();
          }
          // Now we have initialized all counts in this and preceding SCCs,
          // so it is safe to count calls in this SCC.
          for (Defns ds=dsccs.head.getBindings(); ds!=null; ds=ds.next) {
            ds.head.analyzeCalls();
          }
        }
    
        System.out.print("CALLED :");
        for (DefnSCCs dsccs = sccs; dsccs!=null; dsccs=dsccs.next) {
          for (Defns ds=dsccs.head.getBindings(); ds!=null; ds=ds.next) {
            int calls = ds.head.getNumberCalls();
            if (calls>0) {
              System.out.print(" " + ds.head.getId() + "[" + calls + "]");
            }
          }
        }
        System.out.println();
    
        System.out.print("THUNKED:");
        for (DefnSCCs dsccs = sccs; dsccs!=null; dsccs=dsccs.next) {
          for (Defns ds=dsccs.head.getBindings(); ds!=null; ds=ds.next) {
            int calls = ds.head.getNumberThunks();
            if (calls>0) {
              System.out.print(" " + ds.head.getId() + "[" + calls + "]");
            }
          }
        }
        System.out.println();
      }

    /** Run a Global Contant Propagation pass over this program.  Assumes a previous call
     *  to shake() to compute call graph information.
     *  For each Defn call propagateConstants with the argument 3 for max number of different known specializations

        If a function is called with y different constants
        and y is less than the max, then there will be y different
        locks with each constant brought into the block 

     */
    public void GlobalConstantPropagation() {
        for (DefnSCCs dsccs = sccs; dsccs!=null; dsccs=dsccs.next) {
            for (Defns ds=dsccs.head.getBindings(); ds!=null; ds=ds.next) {
                Defns addedDefns = ds.head.propagateConstants(1);

                for (;addedDefns != null; addedDefns = addedDefns.next) {
               //     addEntry(addedDefns.head);
                    count_g++;
                }

              }
        }
      }
    /** Perform a live variable analysis on this program to determine the
     *  correct formal and actual parameter lists for each block.  In this
     *  analysis, we are relying on the fact that the only trailing/tail
     *  BlockCalls in the generated MIL code are for in-function control flow,
     *  and that none of the actual function calls in the original source have
     *  been migrated in to tail position.
     */
    public void computeLiveVars() {
        for (DefnSCCs dsccs = sccs; dsccs!=null; dsccs=dsccs.next) {
          dsccs.head.computeLiveVars();
        }
    }

    /** Rewrite trailing block calls (i.e., block calls at the end of code
     *  sequences) to use the lists of arguments that have previously been
     *  computed by the live variables analysis.
     */
    public void fixTrailingBlockCalls() {
        for (DefnSCCs dsccs = sccs; dsccs!=null; dsccs=dsccs.next) {
            dsccs.head.fixTrailingBlockCalls();
        }
    }
}
