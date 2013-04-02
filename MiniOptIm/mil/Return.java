package mil;

public class Return extends Tail {
    private Atom a;

    /** Default constructor.
     */
    public Return(Atom a) {
        this.a = a;
    }

    /** Test for a free occurrence of a particular variable.
     */
    public boolean contains(Var v) { return a==v; }

    /** Test to see if two Tails are the same.
     */
    public boolean sameTail(Tail that) { return that.isReturn(this);    }
    boolean isReturn(Return that) { return this.a.sameAtom(that.a); }

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
    public boolean isPure() { return true; }

    /** Test whether a given Code/Tail value is an expression of the form return v,
     *  with the specified variable v as its parameter.  We also return a true result
     *  for a Tail of the form return _, where the wildcard indicates that any
     *  return value is acceptable because the result will be ignored by the caller.
     *  This allows us to turn more calls in to tail calls when they occur at the end
     *  of "void functions" that do not return a useful result.
     */
    boolean isReturn(Var v) { return a==v || a==Wildcard.obj; }
    Atom returnsAtom() { return a; }

    /** Special case treatment for top-level bindings of the form  x <- return y;
     *  we want to short out such bindings whenever possible by replacing all
     *  occurrences of x with y.
     */
    Vars liveness(Vars vs) {
        a = a.shortTopLevel();
        return a.add(vs);
      }
    Atom shortTopLevel(Atom d) { return a; }

    /** Compute the set of live variables that appear in this Tail, adding
     *  each one to the list that is passed in as a parameter if it is not
     *  already included.
     */
    Vars liveVars(Vars vs) {
        return a.add(vs);
    }
}
