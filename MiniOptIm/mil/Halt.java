package mil;


/** Represents a code sequence that halts/terminates the current program.
 */
public class Halt extends Code {
    private Halt() {}
    public static final Halt obj = new Halt();

    /** Test for a free occurrence of a particular variable.
     */
    public boolean contains(Var w) { return false; }

    /** Find the list of Defns that this Code sequence depends on.
     */
    public Defns dependencies(Defns ds) {  // halt
        return ds;
    }

    /** Display a printable representation of this MIL construct
     *  on the standard output.
     */
    public void display() {
        indentln("halt");
    }

    /** Apply an AtomSubst to this Code.
     */
    public Code forceApply(AtomSubst s) {    // halt
        return this;
    }
    public Val eval(ValEnv env)
      throws Fail { throw new Fail("Execution halted"); }
    Code cfunSimplify() {
        return this;
      }

    /** Perform inlining on this Code value, decrementing the limit each time
     *  a successful inlining is performed, and declining to pursue further
     *  inlining at this node once the limit reachs zero.
     */
    Code inlining(Block src, int limit) {
        return this;
      }

    /** Modify this code sequence to add a trailing invoke operation.
     */
    Code deriveWithInvoke() { return this; }

    /** Modify this code sequence to add a trailing enter operation that
     *  applies the value that would have been returned by the code in
     *  the original block to the specified argument parameter.
     */
    Code deriveWithEnter(Atom arg) { return this; }

    /** Modify this code sequence to add a trailing enter operation that
     *  applies the value that would have been returned by the code in
     *  the original block to the specified argument parameter.
     */
    Code deriveWithCont(Atom cont) { return this; }
    Code copy() { return this; }

    /** Optimize a Code block using a simple dataflow analysis to track which variables
     *  have been defined using a binding (v <- return a) and which have been defined
     *  usi
     */
    public Code flow(Facts facts, AtomSubst s) { return this; }
    public Code andThen(Var v, Code rest) { return this; }

    /** Live variable analysis on a section of code; rewrites bindings v <- t using
     *  a wildcard, _ <- t, if the variable v is not used in the following code.
     */
    Vars liveness() { return null; }

    /** Compute the set of live variables in this code sequence.
     */
    Vars liveVars() {
        return null;
    }

    /** Rewrite trailing block calls (i.e., block calls at the end of code
     *  sequences) to use the lists of arguments that have previously been
     *  computed by the live variables analysis.
     */
    void fixTrailingBlockCalls() {
        /* do nothing */
    }
}
