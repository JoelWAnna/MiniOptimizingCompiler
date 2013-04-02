package mil;

public class ClosAlloc extends Allocator {
    private ClosureDefn k;

    /** Default constructor.
     */
    public ClosAlloc(ClosureDefn k) {
        this.k = k;
    }

    /** Test to see if two Tails are the same.
     */
    public boolean sameTail(Tail that) { return that.isClosAlloc(this); }
    boolean isClosAlloc(ClosAlloc that) { return this.k==that.k && this.sameArgs(that); }

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

    /** Test to determine whether this Code/Tail value corresponds to a
     *  closure allocator, returning either a ClosAlloc value, or else
     *  a null result.
     */
    ClosAlloc lookForClosAlloc() { return this; }

    /** Compute a Tail that gives the result of applying this ClosAlloc value
     *  to a specified argument a.
     */
    Tail withArg(Atom a) { return k.withArgs(args, a); }
    public void analyzeCalls() { k.thunked(); }
    public void analyzeTailCalls() { k.thunked(); }
}
