package mil;

public abstract class Call extends Tail {

    /** The list of arguments for this call. 
     */
    protected Atom[] args;

    /** Set the arguments for this body.  A typical pattern for constructing a
     *  Tail is:  new PrimCall(p).withArgs(args).  In this way, we can specify
     *  the arguments at the time of construction, but we also have flexibility
     *  to fix the arguments at some point after construction instead.
     */
    public Call withArgs(Atom[] args) {
        this.args = args;
        return this;
    }
    public int getArity() {
        return args.length;
    }

    /** Test for a free occurrence of a particular variable.
     */
    public boolean contains(Var v) {
        for (int i=0; i<args.length; i++) {
            if (v==args[i]) {
                return true;
            }
        }
        return false;
    }
    public boolean sameArgs(Call that) {
        for (int i=0; i<args.length; i++) {
            if (!this.args[i].sameAtom(that.args[i])) {
                return false;
            }
        }
        return true;
    }

    /** Print a call with a notation that includes the name of the item that is being
     *  called and a list of arguments, appropriately wrapped between a given open and
     *  close symbol (parentheses for block, primitive, and data constructor calls;
     *  braces for closure constructors; and brackets for monadic thunk constructors).
     */
    public static void display(String name, String open, Atom[] args, String close) {
        System.out.print(name);
        System.out.print(open);
        if (args!=null && args.length>0) {
            System.out.print(args[0].toString());
            for (int i=1; i<args.length; i++) {
                System.out.print(", ");
                System.out.print(args[i].toString());
            }
        }
        System.out.print(close);
    }

    /** Add the variables mentioned in this tail to the given list of variables.
     */
    public Vars add(Vars vs) { return Vars.add(args, vs); }

    /** Apply an AtomSubst to this Tail.
     */
    public Tail forceApply(AtomSubst s) { return callDup().withArgs(AtomSubst.apply(args, s)); }

    /** Construct a new Call value that is based on the receiver,
     *  without copying the arguments.
     */
    abstract Call callDup();

    /** A special version of apply that works only on Call values; used
     *  in places where type preservation of a Call value is required.
     *  TODO: is this still used?
     */
    public Call forceApplyCall(AtomSubst s) { return callDup().withArgs(AtomSubst.apply(args, s)); }

    /** Special case treatment for top-level bindings of the form  x <- return y;
     *  we want to short out such bindings whenever possible by replacing all
     *  occurrences of x with y.
     */
    Vars liveness(Vars vs) {
        for (int i=0; i<args.length; i++) {
          args[i] = args[i].shortTopLevel();
          vs = args[i].add(vs);
        }
        return vs;
      }

    /** Compute the set of live variables that appear in this Tail, adding
     *  each one to the list that is passed in as a parameter if it is not
     *  already included.
     */
    Vars liveVars(Vars vs) {
        if (args!=null) {
            for (int i=0; i<args.length; i++) {
                vs = args[i].add(vs);
            }
        }
        return vs;
    }
}
