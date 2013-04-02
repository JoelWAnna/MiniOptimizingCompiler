package mil;

public abstract class Tail {

    /** Test for a free occurrence of a particular variable.
     */
    public abstract boolean contains(Var v);

    /** Test to see if two Tails are the same.
     */
    public abstract boolean sameTail(Tail that);
    boolean isReturn(Return that) { return false; }
    boolean isEnter(Enter that) { return false; }
    boolean isInvoke(Invoke that) { return false; }
    boolean isBlockCall(BlockCall that) { return false; }
    boolean isPrimCall(PrimCall that) { return false; }
    boolean isDataAlloc(DataAlloc that) { return false; }
    boolean isClosAlloc(ClosAlloc that) { return false; }
    boolean isCompAlloc(CompAlloc that) { return false; }

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
    public boolean isPure() { return false; }
    Tail removeNewtypeCfun() {  // Default case: return the original tail
        return this;
      }
    public Allocator isAllocator() { return null; }
    boolean detectLoops(Block src, Blocks visited) { return false; }
    boolean loops() { return false; }

    /** Test whether a given Code/Tail value is an expression of the form return v,
     *  with the specified variable v as its parameter.  We also return a true result
     *  for a Tail of the form return _, where the wildcard indicates that any
     *  return value is acceptable because the result will be ignored by the caller.
     *  This allows us to turn more calls in to tail calls when they occur at the end
     *  of "void functions" that do not return a useful result.
     */
    boolean isReturn(Var v) { return false; }
    Code prefixInline(Block src, Var r, Code c) { return null; }

    /**
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

    /** Test to determine if this Tail is an expression of the form (cont @ a)
     *  for some given a, and any cont (except a), returning the function, cont,
     *  as a result.
     */
    public Atom appliesTo(Var v) { return null; }

    /** Add a fact v <- t to the specified list; a fact like this cannot be
     *  included if v appears in t.  (I'm not expecting the latter condition
     *  to occur often/ever, but we should be careful, just in case.)
     */
    public Facts addFact(Var v, Facts facts) {
        return (isPure() && !contains(v)) ? new Facts(v, this, facts) : facts;
      }
    public Tail lookupFact(Top top) { return null; }
    Atom returnsAtom() { return null; }
    public Code rewrite(Facts facts) { return null; }

    /** Special case treatment for top-level bindings of the form  x <- return y;
     *  we want to short out such bindings whenever possible by replacing all
     *  occurrences of x with y.
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
    Atom[] isPrim(Prim p) { return null; }
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
}
