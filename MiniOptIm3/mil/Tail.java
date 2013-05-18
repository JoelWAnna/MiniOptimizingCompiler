package mil;

public abstract class Tail {

    /** Test to determine whether a given tail expression is pure, that is,
     *  if it might have an externally visible side effect.
     */
    public boolean isPure() { return false; }

    /** Test to see if this Tail expression includes a free occurrence of a
     *  particular variable.
     */
    public abstract boolean contains(Var w);

    /** Test to see if two Tail expressions are the same.
     */
    public abstract boolean sameTail(Tail that);

    boolean sameReturn(Return that) { return false; }

    boolean sameEnter(Enter that) { return false; }

    boolean sameInvoke(Invoke that) { return false; }

    boolean sameBlockCall(BlockCall that) { return false; }

    boolean samePrimCall(PrimCall that) { return false; }

    boolean sameDataAlloc(DataAlloc that) { return false; }

    boolean sameClosAlloc(ClosAlloc that) { return false; }

    boolean sameCompAlloc(CompAlloc that) { return false; }

    /** Find the list of Defns that this Tail depends on.
     */
    public abstract Defns dependencies(Defns ds);

    /** Display a printable representation of this MIL construct
     *  on the standard output.
     */
    public abstract void display();

    /** Display a Tail value and then move to the next line.
     */
    public void displayln() { display(); System.out.println(); }

    /** Add the variables mentioned in this tail to the given list of variables.
     */
    public abstract Vars add(Vars vs);

    /** Apply an AtomSubst to this Tail, skipping the operation if
     *  the substition is empty as an attempt at an optimization.
     */
    public Tail apply(AtomSubst s) { return (s==null) ? this : forceApply(s); }

    /** Apply an AtomSubst to this Tail.
     */
    public abstract Tail forceApply(AtomSubst s);

    public abstract Val eval(ValEnv env)
      throws Fail;

    /** Eliminate a call to a newtype constructor in this Tail by replacing it
     *  with a tail that simply returns the original argument of the constructor.
     */
    Tail removeNewtypeCfun() {  // Default case: return the original tail
        return this;
    }

    /** Test to determine if this Tail or Code value is a BlockCall.
     */
    public BlockCall isBlockCall() { return null; }

    /** Test to determine if this Tail is an expression of the form (invoke v).
     */
    public boolean invokes(Var v) { return false; }

    /** Test to determine if this Tail is an expression of the form (v @ a)
     *  for some given v, and any a (except v), returning the argument a as
     *  a result.
     */
    public Atom enters(Var v) { return null; }

    /** Test to determine if this Tail is an expression of the form (f @ w)
     *  for some given w, and any f (except w), returning the function, f,
     *  as a result.
     */
    public Atom appliesTo(Var w) { return null; }

    /** Test for code that is guaranteed not to return.
     */
    boolean doesntReturn() { return false; }

    /** Test whether a given Code/Tail value is an expression of the form return v,
     *  with the specified variable v as its parameter.  We also return a true result
     *  for a Tail of the form return _, where the wildcard indicates that any
     *  return value is acceptable because the result will be ignored by the caller.
     *  This allows us to turn more calls in to tail calls when they occur at the end
     *  of "void functions" that do not return a useful result.
     */
    boolean isReturn(Var v) { return false; }

    boolean detectLoops(Block src, Blocks visited) { return false; }

    boolean loops() { return false; }

    Code prefixInline(Block src, Var r, Code c) { return null; }

    /** Perform suffix inlining on this tail, which either replaces a block call
     *  with an appropriately renamed copy of the block's body, or else returns
     *  null if the tail is either not a block call, or if the code of the block
     *  is not suitable for inlining.
     */
    Code suffixInline(Block src) { return null; }

    /** Skip goto blocks in a Tail (for a ClosureDefn or TopLevel).
     *  TODO: can this be simplified now that ClosureDefns hold Tails rather than Calls?
     *
     *  TODO: couldn't we return an arbitrary Tail here, not just a Call?
     */
    public Tail inlineTail() { return this; }

    /** Test to determine whether this Code/Tail in a specified src Block
     *  constitutes a goto block.  For this to be valid, the code must be
     *  an immediate BlockCall to a different block.
     */
    public BlockCall isGotoBlockCode(Block src) { return null; }

    public Allocator isAllocator() { return null; }

    /** Find the variables that are used in this Tail expression, adding them to
     *  the list that is passed in as a parameter.  Variables that are mentioned
     *  in BlockCalls, ClosAllocs, or CompAllocs are only included if the
     *  corresponding flag in usedArgs is set; all of the arguments in other types
     *  of Call (i.e., PrimCalls and DataAllocs) are considered to be "used".
     */
    abstract Vars usedVars(Vars vs);

    /** Rewrite this program to remove unused arguments in block calls.
     */
    void removeUnusedArgs() { /* nothing to do here */ }

    /** Add a fact v = t to the specified list; a fact like this cannot be
     *  included if v appears in t.  (I'm not expecting the latter condition
     *  to occur often/ever, but we should be careful, just in case.)
     */
    public Facts addFact(Var v, Facts facts) {
        return (isPure() && !contains(v)) ? new Facts(v, this, facts) : facts;
      }

    public Tail lookupFact(Top top) { return null; }

    Atom returnsAtom() { return null; }

    Atom isBnot() { return null; }

    public Code rewrite(Facts facts) { return null; }

    /** Special case treatment for top-level bindings of the form  x <- return y;
     *  we want to short out such bindings whenever possible by replacing all
     *  occurrences of x with y.
     *  TODO: is this comment out of date?
     */
    abstract Vars liveness(Vars vs);

    Atom shortTopLevel(Atom d) { return d; }

    /** Test to determine whether this Code/Tail value corresponds to a
     *  closure allocator, returning either a ClosAlloc value, or else
     *  a null result.
     */
    ClosAlloc lookForClosAlloc() { return null; }

    /** Test to see if this Code/Tail is a CompAlloc (that is, if it has
     *  the form m[x..] for some m and x..).
     */
    CompAlloc lookForCompAlloc() { return null; }

    /** Figure out the BlockCall that will be used in place of the original
     *  after shorting out a Match.  Note that we require a DataAlloc fact
     *  for this to be possible (closures and monadic thunks shouldn't show
     *  up here if the program is well-typed, but we'll check for this just
     *  in case).  Once we've established an appropriate DataAlloc, we can
     *  start testing each of the alternatives to find a matching constructor,
     *  falling back on the default branch if no other option is available.
     */
    BlockCall shortMatch(AtomSubst s, TAlt[] alts, BlockCall d) { return null; }

    /** Test to see if this tail expression is a call to a specific primitive,
     *  returning null in the (most likely) case that it is not.
     */
    Atom[] isPrim(Prim p) { return null; }

    /** Compute an integer summary for a fragment of MIL code with the key property
     *  that alpha equivalent program fragments have the same summary value.
     */
    abstract int summary();

    /** Test to see if two Tail expressions are the alpha equivalent.
     */
    abstract boolean alphaTail(Vars thisvars, Tail that, Vars thatvars);

    /** Test two items for alpha equivalence.
     */
    boolean alphaReturn(Vars thisvars, Return that, Vars thatvars) { return false; }

    /** Test two items for alpha equivalence.
     */
    boolean alphaEnter(Vars thisvars, Enter that, Vars thatvars) { return false; }

    /** Test two items for alpha equivalence.
     */
    boolean alphaInvoke(Vars thisvars, Invoke that, Vars thatvars) { return false; }

    /** Test two items for alpha equivalence.
     */
    boolean alphaBlockCall(Vars thisvars, BlockCall that, Vars thatvars) { return false; }

    /** Test two items for alpha equivalence.
     */
    boolean alphaPrimCall(Vars thisvars, PrimCall that, Vars thatvars) { return false; }

    /** Test two items for alpha equivalence.
     */
    boolean alphaDataAlloc(Vars thisvars, DataAlloc that, Vars thatvars) { return false; }

    /** Test two items for alpha equivalence.
     */
    boolean alphaClosAlloc(Vars thisvars, ClosAlloc that, Vars thatvars) { return false; }

    /** Test two items for alpha equivalence.
     */
    boolean alphaCompAlloc(Vars thisvars, CompAlloc that, Vars thatvars) { return false; }

    void eliminateDuplicates() { /* nothing to do in most cases */ }

    public void analyzeCalls() { }

    public void analyzeTailCalls() { }

    /** Compute the set of live variables that appear in this Tail, adding
     *  each one to the list that is passed in as a parameter if it is not
     *  already included.
     */
    abstract Vars liveVars(Vars vs);

    /** Rewrite trailing block calls (i.e., block calls at the end of code
     *  sequences) to use the lists of arguments that have previously been
     *  computed by the live variables analysis.
     */
    void fixTrailingBlockCalls() {
        /* do nothing */
    }

    public Pairs addIns(Pairs ins) { return null; }
}
