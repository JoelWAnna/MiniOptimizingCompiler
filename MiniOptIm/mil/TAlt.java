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
    boolean[] cfunsUsed(boolean[] used) {
        // Allocate the flag array using the outOf value for c:
        if (used==null) {
          used = new boolean[c.getOutOf()];
        }
        // Flag use of this constructor as having been mentioned:
        int num = c.getNum() - 1;  // flag use of this constructor
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
    BlockCall isNewtypeAlt(Atom a) {
        return (c.isNewtype())
                  ? bc.forceApplyBlockCall(new AtomSubst(args[0], a, null))
                  : null;
      }
    void inlineAlt() { bc = bc.inlineBlockCall(); }
    public TAlt deriveWithInvoke() {
        return new TAlt(c, args, bc.deriveWithInvoke());
      }
    public TAlt deriveWithEnter(Atom arg) {
        return new TAlt(c, args, bc.deriveWithEnter(arg));
      }
    public TAlt deriveWithCont(Atom cont) {
        //!System.out.println("deriveWithCont: Alt");
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
    public void flow(Facts facts, AtomSubst s) {  // c args -> bc
        // Extends substitution with fresh bindings for the variables in
        // args.  Simple, but often unnecessary allocation.
        Var[] vs = Temp.makeTemps(args.length);
        s        = AtomSubst.extend(args, vs, s);
        // facts will be valid for bc because we've renamed using fresh vars
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
}
