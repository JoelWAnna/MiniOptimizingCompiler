package mil;

public class Enter extends Tail {

    private Atom f;

    private Atom a;

    /** Default constructor.
     */
    public Enter(Atom f, Atom a) {
        this.f = f;
        this.a = a;
    }

    /** Test to see if this Tail expression includes a free occurrence of a
     *  particular variable.
     */
    public boolean contains(Var w) { return f==w || a==w; }

    /** Test to see if two Tail expressions are the same.
     */
    public boolean sameTail(Tail that) { return that.sameEnter(this); }

    boolean sameEnter(Enter that) { return this.f.sameAtom(that.f) && this.a.sameAtom(that.a); }

    /** Find the list of Defns that this Tail depends on.
     */
    public Defns dependencies(Defns ds) { // f @ a
        return f.dependencies(a.dependencies(ds));
    }

    /** Display a printable representation of this MIL construct
     *  on the standard output.
     */
    public void display() { System.out.print(f + " @ " + a); }

    /** Add the variables mentioned in this tail to the given list of variables.
     */
    public Vars add(Vars vs) { return f.add(a.add(vs)); }

    /** Apply an AtomSubst to this Tail.
     */
    public Tail forceApply(AtomSubst s) { return new Enter(f.apply(s), a.apply(s)); }

    public Val eval(ValEnv env)
      throws Fail { return f.lookup(env).enter(a.lookup(env)); }

    /** Test to determine if this Tail is an expression of the form (v @ a)
     *  for some given v, and any a (except v), returning the argument a as
     *  a result.
     */
    public Atom enters(Var v) { return (f==v && a!=v) ? a : null;  }

    /** Test to determine if this Tail is an expression of the form (f @ w)
     *  for some given w, and any f (except w), returning the function, f,
     *  as a result.
     */
    public Atom appliesTo(Var w) { return (a==w && a!=f) ? f : null;  }

    /** Skip goto blocks in a Tail (for a ClosureDefn or TopLevel).
     *  TODO: can this be simplified now that ClosureDefns hold Tails rather than Calls?
     *
     *  TODO: couldn't we return an arbitrary Tail here, not just a Call?
     */
    public Tail inlineTail() {
        // TODO: uses code from later flow analysis section
        // TODO: duplicates code from rewrite for Enter
        // TODO: do we need a parallel case for Invoke?
        ClosAlloc clos = f.lookForClosAlloc(null); // Is f a known closure?
        if (clos!=null) {
          MILProgram.report("rewriting "+f+" @ "+a);
          return clos.withArg(a);                 // If so, apply it in place
        }
        return this;
      }

    /** Find the variables that are used in this Tail expression, adding them to
     *  the list that is passed in as a parameter.  Variables that are mentioned
     *  in BlockCalls, ClosAllocs, or CompAllocs are only included if the
     *  corresponding flag in usedArgs is set; all of the arguments in other types
     *  of Call (i.e., PrimCalls and DataAllocs) are considered to be "used".
     */
    Vars usedVars(Vars vs) { return f.add(a.add(vs)); }

    public Code rewrite(Facts facts) {
        ClosAlloc clos = f.lookForClosAlloc(facts); // Is f a known closure?
        if (clos!=null) {
          MILProgram.report("rewriting "+f+" @ "+a);
          // TODO: Is this rewrite always a good idea? Are there cases where it
          // ends up creating more work than it saves?
          Tail t = clos.withArg(a);                 // If so, apply it in place
          Code c = t.rewrite(facts);
          return (c==null) ? new Done(t) : c;
        }
        return null;
      }

    /** Special case treatment for top-level bindings of the form  x <- return y;
     *  we want to short out such bindings whenever possible by replacing all
     *  occurrences of x with y.
     *  TODO: is this comment out of date?
     */
    Vars liveness(Vars vs) {
        a = a.shortTopLevel();
        f = f.shortTopLevel(); 
        return f.add(a.add(vs));
      }

    /** Compute an integer summary for a fragment of MIL code with the key property
     *  that alpha equivalent program fragments have the same summary value.
     */
    int summary() { return 2; }

    /** Test to see if two Tail expressions are the alpha equivalent.
     */
    boolean alphaTail(Vars thisvars, Tail that, Vars thatvars) { return that.alphaEnter(thatvars, this, thisvars); }

    /** Test two items for alpha equivalence.
     */
    boolean alphaEnter(Vars thisvars, Enter that, Vars thatvars) { return this.f.alphaAtom(thisvars, that.f, thatvars)
        && this.a.alphaAtom(thisvars, that.a, thatvars); }

    /** Compute the set of live variables that appear in this Tail, adding
     *  each one to the list that is passed in as a parameter if it is not
     *  already included.
     */
    Vars liveVars(Vars vs) {
        return f.add(a.add(vs));
    }
}
