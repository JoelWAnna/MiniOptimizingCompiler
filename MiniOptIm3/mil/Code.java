package mil;

/** Base class for representing MIL code sequences.
 */
public abstract class Code {

    /** Test for a free occurrence of a particular variable.
     */
    public abstract boolean contains(Var w);

    /** Represents a code sequence that halts/terminates the current program.
     */
    public static final Code halt = new Done(PrimCall.halt);

    /** Find the list of Defns that this Code sequence depends on.
     */
    public abstract Defns dependencies(Defns ds);

    /** Display a printable representation of this MIL construct
     *  on the standard output.
     */
    public abstract void display();

    /** Print an indent at the beginning of a line.
     */
    public static final void indent() {
        System.out.print("  ");
    }

    /** Print a suitably indented string at the start of a line.
     */
    public static final void indentln(String s) {
        indent();
        System.out.println(s);
    }

    /** Apply an AtomSubst to this Code, skipping the operation if
     *  the substitution is empty as an attempt at an optimization.
     *  This operation essentially builds a fresh copy of the original
     *  code sequence, introducing new temporaries in place of any
     *  variables introduced by Binds.
     */
    public Code apply(AtomSubst s) { return (s==null) ? this : forceApply(s); }

    /** Apply an AtomSubst to this Code.
     */
    public abstract Code forceApply(AtomSubst s);

    public abstract Val eval(ValEnv env)
      throws Fail;

    /** Simplify uses of constructor functions in this code sequence.
     */
    abstract Code cfunSimplify();

    /** Modify this code sequence to add a trailing invoke operation.
     */
    abstract Code deriveWithInvoke();

    /** Given an expression of the form (w <- b(..); c), attempt to construct an
     *  equivalent code sequence that instead invokes a block whose code includes
     *  a trailing invoke.
     */
    public Code invokes(Var w, BlockCall bc) {
        return null;
    }

    /** Modify this code sequence to add a trailing enter operation that
     *  applies the value that would have been returned by the code in
     *  the original block to the specified argument parameter.
     */
    abstract Code deriveWithEnter(Atom arg);

    /** Given an expression of the form (w <- b(..); c), attempt to construct an
     *  equivalent code sequence that instead calls a block whose code includes a
     *  trailing enter.
     */
    public Code enters(Var w, BlockCall bc) { return null; }

    /** Modify this code sequence to add a trailing enter operation that
     *  passes the value that would have been returned by the code in
     *  the original block to the specified continuation parameter.
     */
    abstract Code deriveWithCont(Atom cont);

    /** Given an expression of the form (w <- b(..); c), attempt to construct an
     *  equivalent code sequence that instead calls a block whose code will attempt
     *  to call a continuation on the result instead of returning it directly.
     */
    public Code appliesTo(Var w, BlockCall bc) { return null; }

    /** Test to determine if this code is an expression of the form case v of alts where v
     *  is the result of a preceding block call.  If so, return a transformed version of
     *  the code that makes use of a newly defined, known continuation that can subsequently
     *  be pushed in to the individual branches of the case for further optimization.
     */
    public Code casesOn(Var v, BlockCall bc) { return null; }

    abstract Code copy();

    /** Test for code that is guaranteed not to return.
     */
    abstract boolean doesntReturn();

    /** Perform simple clean-up of a code sequence before we begin the main
     *  inlining process.  If this ends up distilling the code sequence to
     *  a single Done, then we won't attempt inlining for this code (because
     *  anyone that attempts to use/call this block will be able to inline
     *  that call, and then this block will go away.
     */
    Code cleanup(Block src) { return this; }

    /** Test whether a given Code/Tail value is an expression of the form return v,
     *  with the specified variable v as its parameter.  We also return a true result
     *  for a Tail of the form return _, where the wildcard indicates that any
     *  return value is acceptable because the result will be ignored by the caller.
     *  This allows us to turn more calls in to tail calls when they occur at the end
     *  of "void functions" that do not return a useful result.
     */
    boolean isReturn(Var v) { return false; }

    boolean detectLoops(Block src, Blocks visited) { return false; }

    public static final int INLINE_ITER_LIMIT = 3;

    /** Perform inlining on this Code sequence, looking for opportunities
     *  to: inline BlockCalls in both Bind and Done nodes; to skip goto
     *  blocks referenced from Match nodes; and to apply the right monad
     *  law by rewriting Code of the form (v <- t; return v) as t.
     *  As a special case, we do not apply inlining to the code of a block
     *  that contains a single Tail; any calls to that block should be
     *  inlined anyway, at which point the block will become deadcode.
     *  In addition, expanding a single tail block like this could lead
     *  to significant code duplication, not least because we also have
     *  another transformation (toBlockCall) that turns an expanded code
     *  sequence back in to a single block call, and we want to avoid
     *  oscillation back and forth between these two forms.
     *  
     *  TODO: Alas, the special treatment of TopLevel values doesn't work.
     *  A code sequence of the form (_ <- return 0; b()) for a top-level
     *  value can trick the inliner into firing (because it's not a single
     *  Done value), expanding the code for b() in place.  Forcing every
     *  TopLevel to be a BlockCall doesn't work either because then the
     *  code that inlines enters to top-level closures won't notice that
     *  the top-level value is indeed a closure (because it goes through
     *  an unnecessary block).  This could probably be handled by a special
     *  case that routes through the block to see if a simple closure can be
     *  found ...
     */
    Code inliningBody(Block src) { return inlining(src, INLINE_ITER_LIMIT); }

    /** Perform inlining on this Code value, decrementing the limit each time
     *  a successful inlining is performed, and declining to pursue further
     *  inlining at this node once the limit reachs zero.
     */
    abstract Code inlining(Block src, int limit);

    Code prefixInline(AtomSubst s, Var u, Code d) {
        debug.Internal.error("This code cannot be inlined");
        return this;
      }

    int prefixInlineLength(int len) { return 0; }

    /** Compute the length of this Code sequence for the purposes of prefix inlining.
     *  The returned value is either the length of the code sequence (counting
     *  one for each Bind and Done node) or 0 if the code sequence ends with
     *  something other than Done.  Argument should be initialized to 0 for
     *  first call.
     */
    abstract int suffixInlineLength(int len);

    /** Test to determine whether this Code is a Done.
     */
    public Tail isDone() { return null; }

    /** Test to determine whether this Code/Tail in a specified src Block
     *  constitutes a goto block.  For this to be valid, the code must be
     *  an immediate BlockCall to a different block.
     */
    public BlockCall isGotoBlockCode(Block src) { return null; }

    void liftAllocators() { /* Nothing to do in this case */ }

    boolean liftAllocators(Bind parent) { return false; }

    /** Find the list of variables that are used in this code sequence.  Variables
     *  that are mentioned in BlockCalls, ClosAllocs, or CompAllocs are only
     *  included if the corresponding flag in usedArgs is set.
     */
    abstract Vars usedVars();

    /** Rewrite this program to remove unused arguments in block calls.
     */
    abstract void removeUnusedArgs();

    /** Optimize a Code block using a simple flow analysis.
     */
    public abstract Code flow(Facts facts, AtomSubst s);

    public abstract Code andThen(Var v, Code rest);

    /** Live variable analysis on a section of code; rewrites bindings v <- t using
     *  a wildcard, _ <- t, if the variable v is not used in the following code.
     */
    abstract Vars liveness();

    /** Test to determine whether this Code/Tail value corresponds to a
     *  closure allocator, returning either a ClosAlloc value, or else
     *  a null result.
     */
    ClosAlloc lookForClosAlloc() { return null; }

    /** Test to see if this Code/Tail is a CompAlloc (that is, if it has
     *  the form m[x..] for some m and x..).
     */
    CompAlloc lookForCompAlloc() { return null; }

    /** Test to see if this code is Match that can be shorted out. Even
     *  If we find a Match, we still need to check for a relevant item
     *  in the set of Facts (after applying a subsitution that
     *  captures the result of entering the block that starts with the
     *  Match).  Again, if it turns out that the optimization cannot be
     *  used, then we return null.
     */
    BlockCall shortMatch(Var[] formals, Atom[] args, Facts facts) { return null; }

    /** Compute an integer summary for a fragment of MIL code with the key property
     *  that alpha equivalent program fragments have the same summary value.
     */
    abstract int summary();

    /** Test to see if two Code sequences are alpha equivalent.
     */
    abstract boolean alphaCode(Vars thisvars, Code that, Vars thatvars);

    /** Test two items for alpha equivalence.
     */
    boolean alphaDone(Vars thisvars, Done that, Vars thatvars) { return false; }

    /** Test two items for alpha equivalence.
     */
    boolean alphaBind(Vars thisvars, Bind that, Vars thatvars) { return false; }

    /** Test two items for alpha equivalence.
     */
    boolean alphaMatch(Vars thisvars, Match that, Vars thatvars) { return false; }

    abstract void eliminateDuplicates();

    public void analyzeCalls() { }

    /** Generate a new block that contains this code sequence and return
     *  a BlockCall that can be used to call the new block.
     */
    public BlockCall makeBlockCall() {
        return new BlockCall(new Block(this));
    }

    /** Compute the set of live variables in this code sequence.
     */
    abstract Vars liveVars();

    /** Rewrite trailing block calls (i.e., block calls at the end of code
     *  sequences) to use the lists of arguments that have previously been
     *  computed by the live variables analysis.
     */
    abstract void fixTrailingBlockCalls();

    public abstract G_Facts inset(G_Facts outs, String id);

    public abstract G_Facts outset(G_Facts ins, String id);
}
