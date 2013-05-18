package mil;

public class ClosAlloc extends Allocator {

    private ClosureDefn k;

    /** Default constructor.
     */
    public ClosAlloc(ClosureDefn k) {
        this.k = k;
    }

    /** Test to see if two Tail expressions are the same.
     */
    public boolean sameTail(Tail that) { return that.sameClosAlloc(this); }

    boolean sameClosAlloc(ClosAlloc that) { return this.k==that.k && this.sameArgs(that); }

    /** Find the list of Defns that this Tail depends on.
     */
    public Defns dependencies(Defns ds) { // k{args}
        return k.dependencies(Atom.dependencies(args, ds));
    }

    /** Display a printable representation of this MIL construct
     *  on the standard output.
     */
    public void display() { display(k.getId(), "{",  args, "}");  }

    /** Construct a new Call value that is based on the receiver,
     *  without copying the arguments.
     */
    Call callDup() { return new ClosAlloc(k); }

    public Val eval(ValEnv env)
      throws Fail { return new ClosVal(ValEnv.lookup(args, env), k); }

    boolean sameFormAlloc(Allocator a) { return a.isClosAlloc(k); }

    boolean isClosAlloc(ClosureDefn k) { return k==this.k; }

    /** Find the variables that are used in this Tail expression, adding them to
     *  the list that is passed in as a parameter.  Variables that are mentioned
     *  in BlockCalls, ClosAllocs, or CompAllocs are only included if the
     *  corresponding flag in usedArgs is set; all of the arguments in other types
     *  of Call (i.e., PrimCalls and DataAllocs) are considered to be "used".
     */
    Vars usedVars(Vars vs) { return k.usedVars(args, vs); }

    /** Rewrite this program to remove unused arguments in block calls.
     */
    void removeUnusedArgs() { k.removeUnusedArgs(this, args); }

    /** Test to determine whether this Code/Tail value corresponds to a
     *  closure allocator, returning either a ClosAlloc value, or else
     *  a null result.
     */
    ClosAlloc lookForClosAlloc() { return this; }

    /** Compute a Tail that gives the result of applying this ClosAlloc value
     *  to a specified argument a.
     */
    Tail withArg(Atom a) { return k.withArgs(args, a); }

    /** Compute an integer summary for a fragment of MIL code with the key property
     *  that alpha equivalent program fragments have the same summary value.
     */
    int summary() { return k.summary(args)*33 + 3; }

    /** Test to see if two Tail expressions are the alpha equivalent.
     */
    boolean alphaTail(Vars thisvars, Tail that, Vars thatvars) { return that.alphaClosAlloc(thatvars, this, thisvars); }

    /** Test two items for alpha equivalence.
     */
    boolean alphaClosAlloc(Vars thisvars, ClosAlloc that, Vars thatvars) { return this.k==that.k && this.alphaArgs(thisvars, that, thatvars); }

    public void analyzeCalls() { k.thunked(); }

    public void analyzeTailCalls() { k.thunked(); }
}
