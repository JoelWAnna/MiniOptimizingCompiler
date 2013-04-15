package mil;

public abstract class Var extends Atom {
    public static final Var[] noVars = new Var[0];
	 static final Var TOPLATTICE = null;

    /** Test to determine if this item represents a constructor function.
     */
    public Cfun isCfun() { return null; }
    public TopLevel getTopLevel() { return null; }
    public void setTopLevel(Tail tail) { // TODO: it's ugly that we have to allow for this case :-(
        debug.Internal.error("Attempt to set top-level value for non-TopLevel variable");
    }
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
