package mil;

public class Return extends Tail {

    private Atom a;

    /** Default constructor.
     */
    public Return(Atom a) {
        this.a = a;
    }

    /** Test to determine whether a given tail expression is pure, that is,
     *  if it might have an externally visible side effect.
     */
    public boolean isPure() { return true; }

    /** Test to see if this Tail expression includes a free occurrence of a
     *  particular variable.
     */
    public boolean contains(Var w) { return a==w; }

    /** Test to see if two Tail expressions are the same.
     */
    public boolean sameTail(Tail that) { return that.sameReturn(this); }

    boolean sameReturn(Return that) { return this.a.sameAtom(that.a); }

    /** Find the list of Defns that this Tail depends on.
     */
    public Defns dependencies(Defns ds) { // a
        return a.dependencies(ds);
    }

    /** Display a printable representation of this MIL construct
     *  on the standard output.
     */
    public void display() { System.out.print("return " + a); }

    /** Add the variables mentioned in this tail to the given list of variables.
     */
    public Vars add(Vars vs) { return a.add(vs); }

    /** Apply an AtomSubst to this Tail.
     */
    public Tail forceApply(AtomSubst s) { return new Return(a.apply(s)); }

    public Val eval(ValEnv env)
      throws Fail { return a.lookup(env); }

    /** Test whether a given Code/Tail value is an expression of the form return v,
     *  with the specified variable v as its parameter.  We also return a true result
     *  for a Tail of the form return _, where the wildcard indicates that any
     *  return value is acceptable because the result will be ignored by the caller.
     *  This allows us to turn more calls in to tail calls when they occur at the end
     *  of "void functions" that do not return a useful result.
     */
    boolean isReturn(Var v) { return a==v || a==Wildcard.obj; }

    /** Find the variables that are used in this Tail expression, adding them to
     *  the list that is passed in as a parameter.  Variables that are mentioned
     *  in BlockCalls, ClosAllocs, or CompAllocs are only included if the
     *  corresponding flag in usedArgs is set; all of the arguments in other types
     *  of Call (i.e., PrimCalls and DataAllocs) are considered to be "used".
     */
    Vars usedVars(Vars vs) { return a.add(vs); }

    Atom returnsAtom() { return a; }

    /** Special case treatment for top-level bindings of the form  x <- return y;
     *  we want to short out such bindings whenever possible by replacing all
     *  occurrences of x with y.
     *  TODO: is this comment out of date?
     */
    Vars liveness(Vars vs) {
        a = a.shortTopLevel();
        return a.add(vs);
      }

    Atom shortTopLevel(Atom d) { return a; }

    /** Compute an integer summary for a fragment of MIL code with the key property
     *  that alpha equivalent program fragments have the same summary value.
     */
    int summary() { return 1; }

    /** Test to see if two Tail expressions are the alpha equivalent.
     */
    boolean alphaTail(Vars thisvars, Tail that, Vars thatvars) { return that.alphaReturn(thatvars, this, thisvars); }

    /** Test two items for alpha equivalence.
     */
    boolean alphaReturn(Vars thisvars, Return that, Vars thatvars) { return this.a.alphaAtom(thisvars, that.a, thatvars); }

    /** Compute the set of live variables that appear in this Tail, adding
     *  each one to the list that is passed in as a parameter if it is not
     *  already included.
     */
    Vars liveVars(Vars vs) {
        return a.add(vs);
    }
}
