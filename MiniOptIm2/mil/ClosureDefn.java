package mil;

public class ClosureDefn extends Defn {

    private Var arg;

    private Tail tail;

    /** Default constructor.
     */
    public ClosureDefn(Var arg, Tail tail) {
        this.arg = arg;
        this.tail = tail;
    }

    private static int count = 0;

    private final String id = "k" + count++;

    private Var[] stored;

    void setStored(Var[] stored) {
        this.stored = stored;
    }

    /** Return the identifier that is associated with this definition.
     */
    public String getId() { return id; }

    /** Find the list of Defns that this Defn depends on.
     */
    public Defns dependencies() {  // k{stored} arg = tail
        return tail.dependencies(null);
    }

    void displayDefn() {
        Call.display(id, "{", stored, "} ");
        System.out.print(arg + " = ");
        tail.displayln();
    }

    public Val enter(Val[] vals, Val val)
      throws Fail {
        return tail.eval(new ValEnv(arg, val, ValEnv.extend(stored, vals, null)));
    }

    /** Apply constructor function simplifications to this program.
     */
    void cfunSimplify() { tail = tail.removeNewtypeCfun(); }

    public void inlining() {
        //!  System.out.println("==================================");
        //!  System.out.println("Going to try inlining on:");
        //!  displayDefn();
        //!  System.out.println();
               tail = tail.inlineTail();
        //!  System.out.println("And the result is:");
        //!  displayDefn();
        //!  System.out.println();
          }

    /** A bitmap that identifies the used arguments of this definition.  The base case,
     *  with no used arguments, can be represented by a null array.  Otherwise, it will
     *  be a non null array, the same length as the list of true in positions corresponding
     *  to arguments that are known to be used and false in all other positions.
     */
    private boolean[] usedArgs = null;

    /** Counts the total number of used arguments in this definition; this should match
     *  the number of true bits in the usedArgs array.
     */
    private int numUsedArgs = 0;

    /** Reset the bitmap and count for the used arguments of this definition,
     *  where relevant.
     */
    void clearUsedArgsInfo() {
        usedArgs    = null;
        numUsedArgs = 0;
    }

    /** Count the number of unused arguments for this definition using the current
     *  unusedArgs information for any other items that it references.
     */
    int countUnusedArgs() { return countUnusedArgs(stored); }

    /** Count the number of unused arguments for this definition.  A count of zero
     *  indicates that all arguments are used.
     */
    int countUnusedArgs(Var[] bound) {
        int unused = bound.length - numUsedArgs;      // count # of unused args
        if (unused > 0) {                             // skip if no unused args
            Vars vs = usedVars();                     // find vars used in body
            for (int i=0; i<bound.length; i++) {      // scan argument list
                if (usedArgs==null || !usedArgs[i]) { // skip if already known to be used
                    if (Vars.isIn(bound[i], vs) && !duplicated(i, bound)) {
                        if (usedArgs==null) {         // initialize usedArgs for first use
                            usedArgs = new boolean[bound.length];
                        }
                        usedArgs[i] = true;           // mark this argument as used
                        numUsedArgs++;                // update counts
                        unused--;
                    }
                }
            }
        }
        return unused;
    }

    /** A utility function that returns true if the variable at position i
     *  in the given array also appears in some earlier position in the array.
     *  (If this condition applies, then we can mark the later occurrence as
     *  unused; there is no need to pass the same variable twice.)
     */
    private static boolean duplicated(int i, Var[] bound) {
        // Did this variable appear in an earlier position?
        for (int j=0; j<i; j++) {
            if (bound[j]==bound[i]) {
  //!System.out.println("**************");
  //!System.out.println("**************");
  //!System.out.println("**************");
  //!System.out.println("**************");
                return true;
            }
        }
        return false;
    }

    /** Find the list of variables that are used in this definition.  Variables that
     *  are mentioned in BlockCalls, ClosAllocs, or CompAllocs are only included if
     *  the corresponding flag in usedArgs is set.
     */
    Vars usedVars() { return tail.usedVars(null); }

    /** Find the list of variables that are used in a call to this definition,
     *  taking account of the usedArgs setting so that we only include variables
     *  appearing in argument positions that are known to be used.
     */
    Vars usedVars(Atom[] args, Vars vs) {
        if (usedArgs!=null) {       // ignore this call if no args are used
          for (int i=0; i<args.length; i++) {
            if (usedArgs[i]) {      // ignore this argument if the flag is not set
              vs = args[i].add(vs);
            }
          }
        }
        return vs;
      }

    /** Use information about which and how many argument positions are used to
     *  trim down an array of variables (i.e., the formal parameters of a Block
     *  or the stored fields of a ClosureDefn).
     */
    Var[] removeUnusedVars(Var[] vars) {
        if (numUsedArgs==vars.length) {   // All arguments used, no rewrite needed
            return vars;
        } else if (usedArgs==null) {      // Drop all arguments; none are needed
            MILProgram.report("removing all arguments from " + getId());
            return Var.noVars;
        } else {                          // Select the subset of needed arguments
            Var[] newVars = new Var[numUsedArgs];
            for (int i=0, j=0; i<vars.length; i++) {
                if (usedArgs[i]) {
                    newVars[j++] = vars[i];
                } else {
                    MILProgram.report("removing unused argument " + vars[i]
                                                          + " from " + getId());
                }
            }
            return newVars;
        }
    }

    /** Update an argument list by removing unused arguments.
     */
    void removeUnusedArgs(Call call, Atom[] args) {
        if (numUsedArgs!=args.length) {   // Only rewrite if some arguments are unused
           Atom[] newArgs = new Atom[numUsedArgs];
           for (int i=0, j=0; j<numUsedArgs; i++) {
               if (usedArgs[i]) {         // copy used arguments
                   newArgs[j++] = args[i];
               }
           }
           call.withArgs(newArgs);        // set new argument list for this call
        }
    }

    /** Rewrite this program to remove unused arguments in block calls.
     */
    void removeUnusedArgs() {
        stored = removeUnusedVars(stored);   // remove unused stored parameters
        tail.removeUnusedArgs();             // update calls in tail
    }

    public void flow() {
        tail.liveness(null);
    }

    /** Compute a Tail that gives the result of entering this closure given the
     *  arguments that are stored in the closure and the extra argument that
     *  prompted us to enter this closure in the first place.
     */
    Tail withArgs(Atom[] args, Atom a) {
        return tail.forceApply(new AtomSubst(arg, a,
                                AtomSubst.extend(stored, args, null)));
      }

    public void analyzeCalls() { tail.analyzeCalls(); }

    /** Calculate a summary value for a list of Atom values, typically the arguments
     *  in a Call.
     */
    int summary(Atom[] args) {
        int sum = summary();
        for (int i=0; i<args.length; i++) {
            sum = 53*sum + args[i].summary();
        }
        return sum;
    }

    /** Compute a summary for this definition (if it is a block) and then look for
     *  a previously encountered block with the same code in the given table.
     *  Return true if a duplicate was found.
     */
    boolean summarizeBlocks(Blocks[] table) { return false; }

    void eliminateDuplicates() {
        tail.eliminateDuplicates();
     }

    /** propagateConstants
     * @param maxArgReplacement - determines the maximum tuple size of the lattice for each parameter
     *
     *
     *
     */
    public Defns propagateConstants(int maxArgReplacement) {
        // TODO: Implement this, only sibling class that it is implemented is Block.java
        System.out.println("reached ClosureDefn propagateConstants of block" + id);
        return null;
    }
}
