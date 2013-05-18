package mil;

public class Invoke extends Tail {

    private Atom a;

    /** Default constructor.
     */
    public Invoke(Atom a) {
        this.a = a;
    }

    /** Test to see if this Tail expression includes a free occurrence of a
     *  particular variable.
     */
    public boolean contains(Var w) { return a==w; }

    /** Test to see if two Tail expressions are the same.
     */
    public boolean sameTail(Tail that) { return that.sameInvoke(this); }

    boolean sameInvoke(Invoke that) { return this.a.sameAtom(that.a); }

    /** Find the list of Defns that this Tail depends on.
     */
    public Defns dependencies(Defns ds) { // invoke a
        return a.dependencies(ds);
    }

    /** Display a printable representation of this MIL construct
     *  on the standard output.
     */
    public void display() { System.out.print("invoke " + a); }

    /** Add the variables mentioned in this tail to the given list of variables.
     */
    public Vars add(Vars vs) { return a.add(vs); }

    /** Apply an AtomSubst to this Tail.
     */
    public Tail forceApply(AtomSubst s) { return new Invoke(a.apply(s)); }

    public Val eval(ValEnv env)
      throws Fail { return a.lookup(env).invoke(); }

    /** Test to determine if this Tail is an expression of the form (invoke v).
     */
    public boolean invokes(Var v) { return v==a;  }

    /** Find the variables that are used in this Tail expression, adding them to
     *  the list that is passed in as a parameter.  Variables that are mentioned
     *  in BlockCalls, ClosAllocs, or CompAllocs are only included if the
     *  corresponding flag in usedArgs is set; all of the arguments in other types
     *  of Call (i.e., PrimCalls and DataAllocs) are considered to be "used".
     */
    Vars usedVars(Vars vs) { return a.add(vs); }

    public Code rewrite(Facts facts) {
        CompAlloc comp = a.lookForCompAlloc(facts); // Is f a known thunk?
        if (comp!=null) {
          MILProgram.report("rewriting invoke "+a);
          // Is this rewrite always a good idea? For example, if we have
          //    t <- m[x,y,z]; u <- invoke t; ... code that uses t ...
          // then we can rewrite to 
          //    t <- m[x,y,z]; u <- m(x,y,z); ... code that uses t ...
          // but we won't be able to drop the construction of the thunk t
          // as dead code.
          Tail t = comp.invoke();                  // If so, call it directly
          Code c = t.rewrite(facts);
          return (c==null) ? new Done(t) : c;
        }
        Tail t = a.invokesTopLevel();
        return (t==null) ? null : new Done(t);
      }

    /** Special case treatment for top-level bindings of the form  x <- return y;
     *  we want to short out such bindings whenever possible by replacing all
     *  occurrences of x with y.
     *  TODO: is this comment out of date?
     */
    Vars liveness(Vars vs) {
        a = a.shortTopLevel();
        return a.add(vs);
      }

    /** Compute an integer summary for a fragment of MIL code with the key property
     *  that alpha equivalent program fragments have the same summary value.
     */
    int summary() { return 3; }

    /** Test to see if two Tail expressions are the alpha equivalent.
     */
    boolean alphaTail(Vars thisvars, Tail that, Vars thatvars) { return that.alphaInvoke(thatvars, this, thisvars); }

    /** Test two items for alpha equivalence.
     */
    boolean alphaInvoke(Vars thisvars, Invoke that, Vars thatvars) { return this.a.alphaAtom(thisvars, that.a, thatvars); }

    /** Compute the set of live variables that appear in this Tail, adding
     *  each one to the list that is passed in as a parameter if it is not
     *  already included.
     */
    Vars liveVars(Vars vs) {
        return a.add(vs);
    }
}
