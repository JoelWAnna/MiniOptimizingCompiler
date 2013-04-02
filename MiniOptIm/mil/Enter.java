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

    /** Test for a free occurrence of a particular variable.
     */
    public boolean contains(Var v) { return f==v || a==v; }

    /** Test to see if two Tails are the same.
     */
    public boolean sameTail(Tail that) { return that.isEnter(this);     }
    boolean isEnter(Enter that) { return this.f.sameAtom(that.f) && this.a.sameAtom(that.a); }

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

    /** Test to determine if this Tail is an expression of the form (v @ a)
     *  for some given v, and any a (except v), returning the argument a as
     *  a result.
     */
    public Atom enters(Var v) { return (f==v && a!=v) ? a : null;  }

    /** Test to determine if this Tail is an expression of the form (cont @ a)
     *  for some given a, and any cont (except a), returning the function, cont,
     *  as a result.
     */
    public Atom appliesTo(Var v) { return (a==v && a!=f) ? f : null;  }
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
     */
    Vars liveness(Vars vs) {
        a = a.shortTopLevel();
        f = f.shortTopLevel(); 
        return f.add(a.add(vs));
      }

    /** Compute the set of live variables that appear in this Tail, adding
     *  each one to the list that is passed in as a parameter if it is not
     *  already included.
     */
    Vars liveVars(Vars vs) {
        return f.add(a.add(vs));
    }
}
