package mil;

public class Top extends Var {

    private TopLevel tl;

    /** Default constructor.
     */
    public Top(TopLevel tl) {
        this.tl = tl;
    }

    /** Generate a printable description of this atom.
     */
    public String toString() { return tl.getId(); }

    /** Find the list of Defns that this Tail depends on.
     */
    public Defns dependencies(Defns ds) {
        return tl.dependencies(ds);
    }

    public Val lookup(ValEnv env)
      throws Fail { throw new Fail("lookup Top not implemented yet"); }

    public Tail lookupFact(Facts facts) { return tl.lookupFact(this); /* top level can't use local facts */ }

    public Tail invokesTopLevel() { return tl.invokesTopLevel(); }

    Atom shortTopLevel() { return tl.shortTopLevel(this); }

    /** Test to determine if this Atom is known to hold a specific function
     *  value, represented by a ClosAlloc/closure allocator, according to the
     *  given set of facts.
     */
    ClosAlloc lookForClosAlloc(Facts facts) { return tl.lookForClosAlloc(); }

    /** Test to see whether this Atom, used as the argument of an invoke,
     *  is known to have been assigned to a monadic thunk.  Returns either
     *  the monadic thunk (a CompAlloc value) or null the Atom has some
     *  other value or if there is no known value in the set of facts.
     */
    CompAlloc lookForCompAlloc(Facts facts) { return tl.lookForCompAlloc(); }
}
