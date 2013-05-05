package mil;

public class CompAlloc extends Allocator {

    private Block m;

    /** Default constructor.
     */
    public CompAlloc(Block m) {
        this.m = m;
    }

    /** Test to see if two Tail expressions are the same.
     */
    public boolean sameTail(Tail that) { return that.sameCompAlloc(this); }

    boolean sameCompAlloc(CompAlloc that) { return this.m==that.m && this.sameArgs(that); }

    /** Find the list of Defns that this Tail depends on.
     */
    public Defns dependencies(Defns ds) { // m[args]
        return m.dependencies(Atom.dependencies(args, ds));
    }

    /** Display a printable representation of this MIL construct
     *  on the standard output.
     */
    public void display() { display(m.getId(), "[",  args, "]");  }

    /** Construct a new Call value that is based on the receiver,
     *  without copying the arguments.
     */
    Call callDup() { return new CompAlloc(m); }

    public Val eval(ValEnv env)
      throws Fail { return new CompVal(ValEnv.lookup(args, env), m); }

    boolean sameFormAlloc(Allocator a) { return a.isCompAlloc(m); }

    boolean isCompAlloc(Block m) { return m==this.m; }

    /** Find the variables that are used in this Tail expression, adding them to
     *  the list that is passed in as a parameter.  Variables that are mentioned
     *  in BlockCalls, ClosAllocs, or CompAllocs are only included if the
     *  corresponding flag in usedArgs is set; all of the arguments in other types
     *  of Call (i.e., PrimCalls and DataAllocs) are considered to be "used".
     */
    Vars usedVars(Vars vs) { return m.usedVars(args, vs); }

    /** Rewrite this program to remove unused arguments in block calls.
     */
    void removeUnusedArgs() { m.removeUnusedArgs(this, args); }

    /** Test to see if this Code/Tail is a CompAlloc (that is, if it has
     *  the form m[x..] for some m and x..).
     */
    CompAlloc lookForCompAlloc() { return this; }

    /** Compute the direct block call m(x..) for a monadic thunk m[x..].
     */
    Call invoke() { return new BlockCall(m).withArgs(args); }

    public void analyzeCalls() { m.thunked(); }

    public void analyzeTailCalls() { m.thunked(); }

    /** Compute an integer summary for a fragment of MIL code with the key property
     *  that alpha equivalent program fragments have the same summary value.
     */
    int summary() { return m.summary(args)*33 + 4; }

    /** Test to see if two Tail expressions are the alpha equivalent.
     */
    boolean alphaTail(Vars thisvars, Tail that, Vars thatvars) { return that.alphaCompAlloc(thatvars, this, thisvars); }

    /** Test two items for alpha equivalence.
     */
    boolean alphaCompAlloc(Vars thisvars, CompAlloc that, Vars thatvars) { return this.m==that.m && this.alphaArgs(thisvars, that, thatvars); }

    void eliminateDuplicates() { m = m.replaceWith(); }
}
