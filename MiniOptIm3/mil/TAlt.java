package mil;

/** Represents an alternative in a monadic Match.
 */
public class TAlt {

    private Cfun c;

    private Var[] args;

    private BlockCall bc;

    /** Default constructor.
     */
    public TAlt(Cfun c, Var[] args, BlockCall bc) {
        this.c = c;
        this.args = args;
        this.bc = bc;
    }

    /** Test for a free occurrence of a particular variable.
     */
    public boolean contains(Var w) {
        for (int i=0; i<args.length; i++) {
            if (args[i]==w) return false;
        }
        return bc.contains(w);
    }

    /** Find the list of Defns that this Code sequence depends on.
     */
    public Defns dependencies(Defns ds) {  // c args -> bc
        return bc.dependencies(ds);
    }

    /** Display a printable representation of this MIL construct
     *  on the standard output.
     */
    public void display() {
        Call.display(c.getId(), "(", args, ")");
        System.out.print(" -> ");
        bc.displayln();
    }

    /** Apply an AtomSubst to this TAlt, skipping if the substitution is empty.
     */
    public TAlt apply(AtomSubst s) { return (s==null) ? this : forceApply(s); }

    /** Apply an AtomSubst to this TAlt.
     */
    public TAlt forceApply(AtomSubst s) {  // c args -> bc
        /*
              // Extends substitution with fresh bindings for the variables in
              // args.  Simple, but often unnecessary allocation.
              Var[] vs = Temp.makeTemps(args.length);
              s        = AtomSubst.extend(args, vs, s);
              return new TAlt(c, vs, bc.forceApplyBlockCall(s));
        */
              // Extends substitution with identity bindings for the variables in
              // args.  Simple, but often unnecessary allocation.
              s = AtomSubst.extend(args, args, s);
              return new TAlt(c, args, bc.forceApplyBlockCall(s));
          }

    public Val match(Cfun c, Val[] vals, ValEnv env)
      throws Fail {
        return (c==this.c) ? bc.eval(ValEnv.extend(args, vals, env)) : null;
    }

    /** Extend a list of (n-1) alternatives for a type that has n constructors
     *  with an extra alternative for the missing nth constructor to avoid the
     *  need for a default branch.  We assume there is at least one alternative
     *  in alts, and that the unused constructor is at position notused in the
     *  underlying array of constructors.
     */
    static TAlt[] extendAlts(TAlt[] alts, int notused, BlockCall bc) {
        int    lastIdx = alts.length;   // index of last/new alternative
        TAlt[] newAlts = new TAlt[lastIdx+1];
        for (int i=0; i<lastIdx; i++) { // copy existing alternatives
            newAlts[i] = alts[i];
        }
        Cfun c           = alts[0].c.getConstrs()[notused];
        newAlts[lastIdx] = new TAlt(c, Temp.makeTemps(c.getArity()), bc);
        MILProgram.report("Replacing default branch with match on " + c.getId());
        return newAlts;
    }

    /** Account for the constructor that is used in this alternative, setting the
     *  corresponding flag in the argument array, which has one position for each
     *  constructor in the underlying type.
     */
    boolean[] cfunsUsed(boolean[] used) {
        // Allocate a flag array based on the constrs array for c:
        if (used==null) {
            used = new boolean[c.getConstrs().length];
        }
        // Flag use of this constructor as having been mentioned:
        int num = c.getNum();    // flag use of this constructor
        if (num>=0 && num<used.length) {
            if (used[num]) {
                debug.Internal.error("multiple alternatives for " + c);
            }
            used[num] = true;
        } else {
            debug.Internal.error("cfun index out of range");
        }
        return used;
    }

    /** Determine if this alternative is for a newtype constructor, in which case return
     *  the simple block call that can be used to replace it.
     */
    BlockCall isNewtypeAlt(Atom a) {
        return (c.isNewtype())
                  ? bc.forceApplyBlockCall(new AtomSubst(args[0], a, null))
                  : null;
    }

    /** Generate a new version of this alternative that branches to a
     *  derived block with a trailing invoke.
     */
    public TAlt deriveWithInvoke() {
        return new TAlt(c, args, bc.deriveWithInvoke());
    }

    /** Generate a new version of this alternative that branches to a
     *  derived block with a trailing enter.
     */
    public TAlt deriveWithEnter(Atom arg) {
        return new TAlt(c, args, bc.deriveWithEnter(arg));
    }

    /** Generate a new version of this alternative that branches to a
     *  derived block with a trailing invocation of a continuation.
     */
    public TAlt deriveWithCont(Atom cont) {
        return new TAlt(c, args, bc.deriveWithCont(cont));
    }

    static TAlt[] copy(TAlt[] alts) {
        if (alts==null) {
          return null;
        } else {
          TAlt[] copied = new TAlt[alts.length];
          for (int i=0; i<alts.length; i++) {
            copied[i] = alts[i].copy();
          }
          return copied;
        }
      }

    TAlt copy() { return new TAlt(c, args, bc); }

    /** Test for code that is guaranteed not to return.
     */
    boolean doesntReturn() { return bc.doesntReturn(); }

    void inlineAlt() { bc = bc.inlineBlockCall(); }

    /** Find the list of variables that are used in this code sequence.  Variables
     *  that are mentioned in BlockCalls, ClosAllocs, or CompAllocs are only
     *  included if the corresponding flag in usedArgs is set.
     */
    Vars usedVars() { return Vars.remove(args, bc.usedVars(null)); }

    /** Rewrite this program to remove unused arguments in block calls.
     */
    void removeUnusedArgs() { bc.removeUnusedArgs(); }

    public void flow(Var v, Facts facts, AtomSubst s) {  // c args -> bc
        // Extends substitution with fresh bindings for the variables in
        // args.  Simple, but often unnecessary allocation.
        Var[] vs = Temp.makeTemps(args.length);
        s        = AtomSubst.extend(args, vs, s);
        // facts will be valid for bc because we've renamed using fresh vars
        facts    = new Facts(v, new DataAlloc(c).withArgs(vs), facts);
        bc       = bc.forceApplyBlockCall(s).rewriteBlockCall(facts);
        args     = vs;
      }

    /** Live variable analysis on a section of code; rewrites bindings v <- t using
     *  a wildcard, _ <- t, if the variable v is not used in the following code.
     */
    Vars liveness() {  // c args -> bc
        return Vars.remove(args, bc.liveness(null));
      }

    /** Test to determine whether this alternative will match a data value
     *  constructed using a specified constructor and argument list.  The
     *  substitution captures the original instantiation of the block as
     *  determined by the original BlockCall.
     */
    BlockCall shortMatch(AtomSubst s, Cfun c, Atom[] args) {
        if (c==this.c) {   // constructors match
          return bc.applyBlockCall(AtomSubst.extend(this.args, args, s));
        }
        return null;
      }

    /** Compute an integer summary for a fragment of MIL code with the key property
     *  that alpha equivalent program fragments have the same summary value.
     */
    int summary() { return 3*c.summary(args)+7*bc.summary(); }

    /** Test to see if two alternatives are alpha equivalent.
     */
    boolean alphaTAlt(Vars thisvars, TAlt that, Vars thatvars) {
        if (this.c!=that.c || this.args.length!=that.args.length) {
            return false;
        }
        for (int i=0; i<args.length; i++) {
            thisvars = new Vars(this.args[i], thisvars);
            thatvars = new Vars(that.args[i], thatvars);
        }
        return this.bc.alphaBlockCall(thisvars, that.bc, thatvars);
     }

    void eliminateDuplicates() { bc.eliminateDuplicates(); }

    /** Compute the set of live variables in this code sequence.
     */
    Vars liveVars() {
        return Vars.remove(args, bc.liveVars(null));
    }

    /** Rewrite trailing block calls (i.e., block calls at the end of code
     *  sequences) to use the lists of arguments that have previously been
     *  computed by the live variables analysis.
     */
    void fixTrailingBlockCalls() {
        bc.fixTrailingBlockCalls();
    }

    public Pairs addIns(Pairs ins) {      return bc.addIns(ins); }

    public Pairs kill(Pairs ins) { return bc.kill(ins); }

    public Pairs gen(Pairs ins) { return bc.gen(ins); }
}
