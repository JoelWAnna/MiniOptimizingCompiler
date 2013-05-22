package mil;

public class DataAlloc extends Allocator {

    private Cfun c;

    /** Default constructor.
     */
    public DataAlloc(Cfun c) {
        this.c = c;
    }

    /** Test to see if two Tail expressions are the same.
     */
    public boolean sameTail(Tail that) { return that.sameDataAlloc(this); }

    boolean sameDataAlloc(DataAlloc that) { return this.c==that.c && this.sameArgs(that); }

    /** Find the list of Defns that this Tail depends on.
     */
    public Defns dependencies(Defns ds) { // c(args)
        return Atom.dependencies(args, ds); // TODO: do we need to register cfuns?
    }

    /** Display a printable representation of this MIL construct
     *  on the standard output.
     */
    public void display() { display(c.getId(), "(",  args, ")");  }

    /** Construct a new Call value that is based on the receiver,
     *  without copying the arguments.
     */
    Call callDup() { return new DataAlloc(c); }

    public Val eval(ValEnv env)
      throws Fail { return new DataVal(ValEnv.lookup(args, env), c); }

    /** Eliminate a call to a newtype constructor in this Tail by replacing it
     *  with a tail that simply returns the original argument of the constructor.
     */
    Tail removeNewtypeCfun() {
        if (c.isNewtype()) {  // Look for use of a newtype constructor
            if (args==null || args.length!=1) {
                debug.Internal.error("newtype constructor with arity!=1");
            }
            return new Return(args[0]); // and generate a Return instead
        }
        return this;
    }

    boolean sameFormAlloc(Allocator a) { return a.isDataAlloc(c); }

    boolean isDataAlloc(Cfun c) { return c==this.c; }

    /** Figure out the BlockCall that will be used in place of the original
     *  after shorting out a Match.  Note that we require a DataAlloc fact
     *  for this to be possible (closures and monadic thunks shouldn't show
     *  up here if the program is well-typed, but we'll check for this just
     *  in case).  Once we've established an appropriate DataAlloc, we can
     *  start testing each of the alternatives to find a matching constructor,
     *  falling back on the default branch if no other option is available.
     */
    BlockCall shortMatch(AtomSubst s, TAlt[] alts, BlockCall d) {
        for (int i=0; i<alts.length; i++) { // search for matching alternative
          BlockCall bc = alts[i].shortMatch(s, c, args);
          if (bc!=null) {
            MILProgram.report("shorting out match on constructor "+c.getId());
            return bc;
          }
        }
        MILProgram.report("shorting out match using default for "+c.getId());
        return d.applyBlockCall(s); // use default branch if no match found
      }

    /** Special constructor for known constructor and argument list.
     */
    private DataAlloc(Cfun c, Atom[] args) {
        this.c    = c;
        this.args = args;
    }

    public static final DataAlloc True = new DataAlloc(Cfun.True,  Var.noVars);

    public static final DataAlloc False = new DataAlloc(Cfun.False, Var.noVars);

    /** Compute an integer summary for a fragment of MIL code with the key property
     *  that alpha equivalent program fragments have the same summary value.
     */
    int summary() { return c.summary(args)*33 + 2; }

    /** Test to see if two Tail expressions are the alpha equivalent.
     */
    boolean alphaTail(Vars thisvars, Tail that, Vars thatvars) { return that.alphaDataAlloc(thatvars, this, thisvars); }

    /** Test two items for alpha equivalence.
     */
    boolean alphaDataAlloc(Vars thisvars, DataAlloc that, Vars thatvars) { return this.c==that.c && this.alphaArgs(thisvars, that, thatvars); }
}
