package mil;

public abstract class Atom {
    public abstract String toString();

    /** Test to see if two atoms are the same.  For a pair of Const objects,
     *  this means that the two objects have the same val.  For any other
     *  pair of Atoms, we expect the objects themselves to be the same.
     */
    public boolean sameAtom(Atom that) { return this==that; }
    public boolean isVar(Var v) { return this==v; }
    public Const isConst() { return null; }

    /** Find the list of Defns that this Tail depends on.
     */
    public Defns dependencies(Defns ds) {
        return ds;     // Only Top atoms can register
    }

    /** Find out the list of Defns that this array of Atoms depends on.
     */
    public static Defns dependencies(Atom[] args, Defns ds) {
        if (args!=null) {
            for (int i=0; i<args.length; i++) {
                ds = args[i].dependencies(ds);
            }
        }
        return ds;
    }

    /** Add this atom as an argument variable to the given list; only local
     *  variables and temporaries are treated as argument variables because
     *  wildcards are ignored and all other atoms can be accessed as constants.
     */
    Vars add(Vars vs) { return vs; }

    /** Apply an AtomSubst to this atom.
     */
    public Atom apply(AtomSubst s) { return this; }
    public abstract Val lookup(ValEnv env)
      throws Fail;
    static Atom[] fromVars(Var[] vs) {
        Atom[] as = new Atom[vs.length];
        for (int i=0; i<vs.length; i++) {
          as[i] = vs[i];
        }
        return as;
      }
    public Tail lookupFact(Facts facts) { return null; }
    Atom shortTopLevel() { return this; }
    public Tail invokesTopLevel() { return null; }

    /** Test to determine if this Atom is known to hold a specific function
     *  value, represented by a ClosAlloc/closure allocator, according to the
     *  given set of facts.
     */
    ClosAlloc lookForClosAlloc(Facts facts) { return null; }

    /** Test to see whether this Atom, used as the argument of an invoke,
     *  is known to have been assigned to a monadic thunk.  Returns either
     *  the monadic thunk (a CompAlloc value) or null the Atom has some
     *  other value or if there is no known value in the set of facts.
     */
    CompAlloc lookForCompAlloc(Facts facts) { return null; }
}
