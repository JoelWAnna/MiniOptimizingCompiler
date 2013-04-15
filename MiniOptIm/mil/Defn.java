package mil;

public abstract class Defn {

    /** Return the identifier that is associated with this definition.
     */
    public abstract String getId();

    /** Test to determine whether this Defn is for a top level value with
     *  the specified name.
     */
    public boolean defines(String id) { return false; }

    /** Records the successors/callees of this node.
     */
    private Defns callees = null;

    /** Records the predecessors/callers of this node.
     */
    private Defns callers = null;

    /** Update callees/callers information with dependencies.
     */
    public void calls(Defns xs) {
        for (callees=xs; xs!=null; xs=xs.next) {
          xs.head.callers = new Defns(this, xs.head.callers);
        }
      }

    /** Flag to indicate that this node has been visited during the depth-first
     *  search of the forward dependency graph.
     */
    private boolean visited = false;

    /** Visit this X during a depth-first search of the forward dependency graph.
     */
    public Defns forwardVisit(Defns result) {
        if (!this.visited) {
          this.visited = true;
          return new Defns(this, Defns.searchForward(this.callees, result));
        }
        return result;
      }

    /** Records the binding scc in which this binding has been placed.
     *  This field is initialized to null but is set to the appropriate
     *  binding scc during dependency analysis.
     */
    private DefnSCC scc = null;

    /** Return the binding scc that contains this binding.
     */
    public DefnSCC getScc() {
        return scc;
    }

    /** Visit this binding during a depth-first search of the reverse
     *  dependency graph.  The scc parameter is the binding scc in
     *  which all unvisited bindings that we find should be placed. 
     */
    public void reverseVisit(DefnSCC scc) {
        if (this.scc==null) {
          // If we arrive at a binding that hasn't been allocated to any SCC,
          // then we should put it in this SCC.
          this.scc = scc;
          scc.add(this);
          for (Defns callers=this.callers; callers!=null; callers=callers.next) {
            callers.head.reverseVisit(scc);
          }
        } else if (this.scc==scc) {
          // If we arrive at a binding that has the same binding scc
          // as the one we're building, then we know that it is recursive.
          scc.setRecursive();
          return;
        } else {
          // The only remaining possibility is that we've strayed outside
          // the binding scc we're building to a scc that *depends on*
          // the one we're building.  In other words, we've found a binding
          // scc dependency from this.scc to scc.
          DefnSCC.addDependency(this.scc, scc);
        }
      }
    private static int dfsNum = 0;
    private int visitNum = 0;
    public static void newDFS() {    // Begin a new depth-first search
        dfsNum++;
    }
    protected int occurs;
    public int getOccurs() {
        return occurs;
    }

    /** Visit this Defn as part of a depth first search, and building up a list
     *  of Defn nodes that could be used to compute strongly-connected components.
     */
    Defns visitDepends(Defns defns) {
        if (visitNum==dfsNum) {        // Repeat visit to this Defn?
            occurs++;
//!System.out.println(getId() + " has now occurred " + occurs + " times");
        } else {                       // First time at this Defn
            // Mark this Defn as visited, and initialize fields
            visitNum = dfsNum;
            occurs   = 1;
//!System.out.println("First occurrence of " + getId());
            scc      = null;
            callers  = null;
            callees  = null;

            // Find immediate dependencies
            Defns deps = dependencies();

//!System.out.println("------------");
//!displayDefn();
//!System.out.print("DEPENDS ON: ");
//!String msg = "";
//!for (Defns ds = deps; ds!=null; ds=ds.next) {
//!  System.out.print(msg); msg = ", ";
//!  System.out.print(ds.head.getId());
//!}
//!System.out.println();

            // Visit all the immediate dependencies
            for (; deps!=null; deps=deps.next) {
//!System.out.println("Adjacency: " + getId() + " -> " + deps.head.getId());
                defns = deps.head.visitDepends(defns);
                if (!Defns.isIn(deps.head, callees)) {
                    callees = new Defns(deps.head, callees);
                }
            }

            // Add the information about this node's callers/callees
            this.calls(callees);
            // And add it to the list of all definitions.
            defns = new Defns(this, defns);
        }
        return defns;
    }

    /** Find the list of Defns that this Defn depends on.
     */
    public abstract Defns dependencies();

    /** Find the list of Defns that this Tail depends on.
     */
    public Defns dependencies(Defns ds) {
        return /* Defns.isIn(this, ds) ? ds : */ new Defns(this, ds); // count multiplicities
    }
    public Defns getCallers() {
        return callers;
    }

    /** Display a printable representation of this MIL construct
     *  on the standard output.
     */
    public void display() {
        //!    System.out.println("[occurs=" + occurs
        //!                     + ", indegree=" + Defns.length(callers)
        //!                     + ", outdegree=" + Defns.length(callees) + "]");
        //!  System.out.print(this.getId() + " -> ");
        //!  for (Defns ds=callees; ds!=null; ds=ds.next) {
        //!    System.out.print(" " + ds.head.getId());
        //!  }
        //!  System.out.println(";");
              displayDefn();
          }
    abstract void displayDefn();
    void append(StringBuffer buf) { buf.append(getId()); }
    abstract void cfunSimplify();
    void liftAllocators() { /* Nothing to do */ }
    public abstract void inlining();
    boolean detectLoops(Blocks visited) { return false; }
    public abstract void flow();
    public abstract void buildLattice();
    protected int numberCalls;
    protected int numberThunks;
    public int getNumberCalls() {
        return numberCalls;
    }
    public int getNumberThunks() {
        return numberThunks;
    }
    public void resetCallCounts() {
        numberCalls = numberThunks = 0;
      }

    /** Register a non-tail call for this block.
     */
    public void called() { numberCalls++;  }

    /** Register the creation of a monadic thunk for this block.
     */
    public void thunked() { numberThunks++; }
    public abstract void analyzeCalls();

    /** Calculate the set of live variables for the code in a block,
     *  returning true if the block is recursive and new variables have
     *  been added to the liveVars list for the block during this call.
     *  For any other type of Defn, we just return false.
     */
    boolean computeLiveVars() { return false; }

    /** Rewrite trailing block calls (i.e., block calls at the end of code
     *  sequences) to use the lists of arguments that have previously been
     *  computed by the live variables analysis.
     */
    void fixTrailingBlockCalls() {
        /* do nothing */
    }
}
