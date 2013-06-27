package mil;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class MILProgram {

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

    public static void report(String msg) {
        debug.Log.println(msg);
        count++;
    }

    /** Limit the maximum number of optimization passes that will be performed on
     *  any MIL program.  The choice is somewhat arbitrary; a higher value might
     *  allow more aggressive optimization in some cases, but might also result in
     *  larger output programs as a result of over-specialization, unrolling, or
     *  inlining.
     */
    public static final int MAX_OPTIMIZE_PASSES = 20;

    /** Run the optimizer on this program.
     */
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

    /** Apply constructor function simplifications to this program.
     */
    void cfunSimplify() {
        for (DefnSCCs dsccs = sccs; dsccs!=null; dsccs=dsccs.next) {
            for (Defns ds=dsccs.head.getBindings(); ds!=null; ds=ds.next) {
                ds.head.cfunSimplify();
            }
        }
    }

    /** Run an inlining pass over this program, assuming a preceding call to
     *  shake() to compute call graph information.  Starts by performing a
     *  "return analysis" (to detect blocks that are guaranteed not to return);
     *  uses the results to perform some initial cleanup; and then performs
     *  simple loop detection to identify code that loops with no observable
     *  effect and would cause the inliner to enter an infinite loop.  With
     *  those preliminaries out of the way, we then invoke the main inliner!
     */
    public void inlining() {
        for (DefnSCCs dsccs = sccs; dsccs!=null; dsccs=dsccs.next) {
          Defns defns = dsccs.head.getBindings();
    
          // Initialize return analysis information for each definition in this scc:
          for (Defns ds=defns; ds!=null; ds=ds.next) {
              ds.head.resetDoesntReturn();
          }
    
          // Compute return analysis results for each item in this scc:
          boolean changed = true;
          do {
              changed = false;
              for (Defns ds=defns; ds!=null; ds=ds.next) {
                   changed |= ds.head.returnAnalysis();
              }
          } while (changed);
    
          // Use results of return analysis to clean up code:
          for (Defns ds=defns; ds!=null; ds=ds.next) {
              ds.head.cleanup();
          }
    
          // Run loop detection on this scc, rewriting some block definitions
          // that would otherwise send the inliner in to an infinite loop:
          for (Defns ds=defns; ds!=null; ds=ds.next) {
              ds.head.detectLoops(null);
          }
    
          // Perform inlining on the definitions inside this scc:
          for (Defns ds=defns; ds!=null; ds=ds.next) {
    //!System.out.println("inlining loop at: " + ds.head.getId());
              ds.head.inlining();
          }
        }
      }

    public void returnAnalysis() {
        // Calculate doesntReturn information for every Block:
        for (DefnSCCs dsccs = sccs; dsccs!=null; dsccs=dsccs.next) {
             // Initialize the "doesn't return" information for each definition
             for (Defns ds=dsccs.head.getBindings(); ds!=null; ds=ds.next) {
                 ds.head.resetDoesntReturn();
             }
  
             // Apply return analysis to each of the items in this scc.
             boolean changed = true;
             do {
                 changed = false;
                 for (Defns ds=dsccs.head.getBindings(); ds!=null; ds=ds.next) {
                      changed |= ds.head.returnAnalysis();
                 }
             } while (changed);
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

    /** Analyze and rewrite this program to remove unused Block and ClosureDefn arguments.
     */
    void eliminateUnusedArgs() {
        int totalUnused = 0;
  
        // Calculate unused argument information for every Block and ClosureDefn:
        for (DefnSCCs dsccs = sccs; dsccs!=null; dsccs=dsccs.next) {
             // Initialize used argument information for each definition
             for (Defns ds=dsccs.head.getBindings(); ds!=null; ds=ds.next) {
                 ds.head.clearUsedArgsInfo();
             }
  
             // Calculate the number of unused arguments for the definitions in this
             // strongly-connected component, iterating until either there are no
             // unused arguments, or else until we reach a fixed point (i.e., we get
             // the same number of unused args on two successive passes).
             int lastUnused = 0;
             int unused     = 0;
             do {
                 lastUnused = unused;
                 unused     = 0;
                 for (Defns ds=dsccs.head.getBindings(); ds!=null; ds=ds.next) {
                      unused += ds.head.countUnusedArgs();
                 }
             } while (unused>0 && unused!=lastUnused);
             totalUnused += unused;
        }
  
        // Rewrite the program if there are unused arguments:
        if (totalUnused>0) {
            for (DefnSCCs dsccs = sccs; dsccs!=null; dsccs=dsccs.next) {
                 for (Defns ds=dsccs.head.getBindings(); ds!=null; ds=ds.next) {
                      ds.head.removeUnusedArgs();
                 }
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

    public void collapse() {
        Blocks[] table = new Blocks[251];
        boolean found  = false;
  
        // Visit each block to compute summaries and populate the table:
        for (DefnSCCs dsccs = sccs; dsccs!=null; dsccs=dsccs.next) {
            for (Defns ds=dsccs.head.getBindings(); ds!=null; ds=ds.next) {
                found |= ds.head.summarizeBlocks(table);
            }
        }
  
        // Update the program to eliminate duplicate blocks:
        if (found) {
            for (DefnSCCs dsccs = sccs; dsccs!=null; dsccs=dsccs.next) {
                for (Defns ds=dsccs.head.getBindings(); ds!=null; ds=ds.next) {
                   ds.head.eliminateDuplicates();
                }
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

    public static int count_g = 0;

    /** Run a Global Contant Propagation pass over this program.  Assumes a previous call
     *  to shake() to compute call graph information.
     *  For each Defn call propagateConstants with the argument 3 for max number of different known specializations
     *
     *If a function is called with y different constants
     *and y is less than the max, then there will be y different
     *locks with each constant brought into the block 
     *
     */
    public void GlobalConstantPropagation() {
        for (DefnSCCs dsccs = sccs; dsccs!=null; dsccs=dsccs.next) {
            for (Defns ds=dsccs.head.getBindings(); ds!=null; ds=ds.next) {
                Defns addedDefns = ds.head.propagateConstants(4);

                for (;addedDefns != null; addedDefns = addedDefns.next) {
                    count++;
                }

              }
        }
      }
}
