package mil;

public class DefnSCC {

    /** Records the list of X\s in this binding group.
     */
    private Defns bindings = null;

    /** Return the list of X\s in this scc.
     */
    public Defns getBindings() {
        return bindings;
    }

    /** Add an X to this scc.
     */
    public void add(Defn binding) {
        bindings = new Defns(binding, bindings);
      }

    /** Indicates if the bindings in this scc are recursive.  This flag is
     *  initialized to false but will be set to true if recursive bindings are
     *  discovered during dependency analysis.  If there are multiple bindings
     *  in this scc, then they must be mutually recursive (otherwise they would
     *  have been placed in different binding sccs) and this flag will be set to true.
     */
    private boolean recursive = false;

    /** This method is called when a recursive binding is
     *  discovered during dependency analysis.
     */
    public void setRecursive() {
        recursive = true;
      }

    /** Return a boolean true if this is a recursive binding scc.
     */
    public boolean isRecursive() {
        return recursive;
      }

    /** A list of the binding sccs on which this scc depends.  (This particular scc
     *  will not be included, so the graph of XSCCs will not have any cycles in it.)
     */
    private DefnSCCs dependsOn = null;

    public DefnSCCs getDependsOn() {
        return dependsOn;
    }

    /** Record a dependency between two binding sccs, avoiding
     *  self references and duplicate entries.
     */
    public static void addDependency(DefnSCC from, DefnSCC to) {
        if (from!=to && !find(to, from.dependsOn)) {
          from.dependsOn = new DefnSCCs(to, from.dependsOn);
        }
      }

    /** Search for a specific binding scc within a given list.
     */
    public static boolean find(DefnSCC scc, DefnSCCs sccs) {
        for (; sccs!=null; sccs=sccs.next) {
          if (sccs.head==scc) {
              return true;
          }
        }
        return false;
      }

    /** Display a printable representation of this MIL construct
     *  on the standard output.
     */
    public void display() {
        System.out.println("--------------------------------------");
        System.out.println(isRecursive() ? "recursive" : "not recursive");
        for (Defns ds=bindings; ds!=null; ds=ds.next) {
          ds.head.display();
        }
    }

    /** Heuristic to determine if this block is a good candidate for the casesOn().
     *  TODO: investigate better functions for finding candidates!
     */
    boolean contCand() {
        if (isRecursive()) {
            return false;
        }
        for (DefnSCCs ds = getDependsOn(); ds!=null; ds=ds.next) {
            if (!ds.head.contCand()) {
                return false;
            }
        }
        return true;
    }

    /** Compute the live variables for each of the definitions in this SCC,
     *  iterating, in classic dataflow style, until we reach a fixed point.
     */
    void computeLiveVars() {
        boolean changed;
        do {
            changed  = false;
            for (Defns ds = bindings; ds!=null; ds=ds.next) {
                changed |= ds.head.computeLiveVars();
            }
        } while (changed);
    }

    /** Rewrite trailing block calls (i.e., block calls at the end of code
     *  sequences) to use the lists of arguments that have previously been
     *  computed by the live variables analysis.
     */
    void fixTrailingBlockCalls() {
        for (Defns ds = bindings; ds!=null; ds=ds.next) {
            ds.head.fixTrailingBlockCalls();
        }
    }
}
