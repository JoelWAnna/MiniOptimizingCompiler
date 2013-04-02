package mil;

public class CompAlloc extends Allocator {
    private Block m;

    /** Default constructor.
     */
    public CompAlloc(Block m) {
        this.m = m;
    }

    /** Test to see if two Tails are the same.
     */
    public boolean sameTail(Tail that) { return that.isCompAlloc(this); }
    boolean isCompAlloc(CompAlloc that) { return this.m==that.m && this.sameArgs(that); }

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

    /** Test to see if this Code/Tail is a CompAlloc (that is, if it has
     *  the form m[x..] for some m and x..).
     */
    CompAlloc lookForCompAlloc() { return this; }

    /** Compute the direct block call m(x..) for a monadic thunk m[x..].
     */
    Call invoke() { return new BlockCall(m).withArgs(args); }
    public void analyzeCalls() { m.thunked(); }
    public void analyzeTailCalls() { m.thunked(); }
}
