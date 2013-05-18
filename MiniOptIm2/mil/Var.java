package mil;

public abstract class Var extends Atom {

    public static final Var[] noVars = new Var[0];

    public Vars addTo(Vars vs) { return Vars.add(this, vs); }

    public Vars removeFrom(Vars vs) { return Vars.remove(this, vs); }

    /** Apply an AtomSubst to this atom.
     */
    public Atom apply(AtomSubst s) { return AtomSubst.apply(this, s); }

    public Val lookup(ValEnv env)
      throws Fail { return ValEnv.lookup(this, env); }

    public Tail lookupFact(Facts facts) { return Facts.lookupFact(this, facts); }

    /** Test to determine if this Atom is known to hold a specific function
     *  value, represented by a ClosAlloc/closure allocator, according to the
     *  given set of facts.
     */
    ClosAlloc lookForClosAlloc(Facts facts) {
        Tail t = Facts.lookupFact(this, facts);
        return t==null ? null : t.lookForClosAlloc();
      }

    /** Test to see whether this Atom, used as the argument of an invoke,
     *  is known to have been assigned to a monadic thunk.  Returns either
     *  the monadic thunk (a CompAlloc value) or null the Atom has some
     *  other value or if there is no known value in the set of facts.
     */
    CompAlloc lookForCompAlloc(Facts facts) {
        Tail t = Facts.lookupFact(this, facts);
        return t==null ? null : t.lookForCompAlloc();
      }

    /** Test to see if two atoms are the same upto alpha renaming.
     */
    boolean alphaAtom(Vars thisvars, Atom that, Vars thatvars) { return that.alphaVar(thatvars, this, thisvars); }

    /** Test two items for alpha equivalence.
     */
    boolean alphaVar(Vars thisvars, Var that, Vars thatvars) { 
        int thisidx = Vars.lookup(this, thisvars);
        int thatidx = Vars.lookup(that, thatvars);
        return (thisidx==thatidx && (thisidx>=0 || this.sameAtom(that)));
    }

    /** Test for membership in an array.
     *  TODO: make a macro for this?
     */
    public static boolean isIn(Var val, Var[] vals) {
        for (int i=0; i<vals.length; i++) {
            if (vals[i]==val) {
                return true;
            }
        }
        return false;
    }
}
