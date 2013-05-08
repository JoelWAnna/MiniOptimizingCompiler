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

    /** Simplify uses of constructor functions in this code sequence.
     */
    Code cfunSimplify() {
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
     *  passes the value that would have been returned by the code in
     *  the original block to the specified continuation parameter.
     */
    Code deriveWithCont(Atom cont) { return this; }

    Code copy() { return this; }

    /** Perform inlining on this Code value, decrementing the limit each time
     *  a successful inlining is performed, and declining to pursue further
     *  inlining at this node once the limit reachs zero.
     */
    Code inlining(Block src, int limit) {
        return this;
      }

    /** Find the list of variables that are used in this code sequence.  Variables
     *  that are mentioned in BlockCalls, ClosAllocs, or CompAllocs are only
     *  included if the corresponding flag in usedArgs is set.
     */
    Vars usedVars() { return null; }

    /** Rewrite this program to remove unused arguments in block calls.
     */
    void removeUnusedArgs() { /* nothing to do here */ }

    /** Optimize a Code block using a simple flow analysis.
     */
    public Code flow(Facts facts, AtomSubst s) { return this; }

    public Code andThen(Var v, Code rest) { return this; }

    /** Live variable analysis on a section of code; rewrites bindings v <- t using
     *  a wildcard, _ <- t, if the variable v is not used in the following code.
     */
    Vars liveness() { return null; }

    /** Compute an integer summary for a fragment of MIL code with the key property
     *  that alpha equivalent program fragments have the same summary value.
     */
    int summary() { return 7; }

    /** Test to see if two Code sequences are alpha equivalent.
     */
    boolean alphaCode(Vars thisvars, Code that, Vars thatvars) { return that.alphaHalt(thatvars, this, thisvars); }

    /** Test two items for alpha equivalence.
     */
    boolean alphaHalt(Vars thisvars, Halt that, Vars thatvars) { return true; }

    void eliminateDuplicates() { /* Nothing to do */ }

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

    public Pairs outset(Pairs ins) { /* todo */ return ins;}
}
