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

    /** Test to see if this Tail expression includes a free occurrence of a
     *  particular variable.
     */
    public boolean contains(Var w) {
        for (int i=0; i<args.length; i++) {
            if (w==args[i]) {
                return true;
            }
        }
        return false;
    }

    /** Test to see if two Tail expressions s are the same.
     */
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

    Atom[] specializedArgs(Allocator[] allocs) {
        // Compute the number of actual arguments that are needed:
        int len = 0;
        for (int i=0; i<allocs.length; i++) {
          len += (allocs[i]==null ? 1 : allocs[i].getArity());
        }
  
        // Fill in the actual arguments:
        Atom[]    nargs = new Atom[len];
        int       pos   = 0;
        for (int i=0; i<args.length; i++) {
          if (allocs[i]==null) {
            nargs[pos++] = args[i];
          } else {
            pos = allocs[i].collectArgs(nargs, pos);
          }
        }
  
        // Return specialized list of arguments:
        return nargs;
    }

    /** Find the variables that are used in this Tail expression, adding them to
     *  the list that is passed in as a parameter.  Variables that are mentioned
     *  in BlockCalls, ClosAllocs, or CompAllocs are only included if the
     *  corresponding flag in usedArgs is set; all of the arguments in other types
     *  of Call (i.e., PrimCalls and DataAllocs) are considered to be "used".
     */
    Vars usedVars(Vars vs) {
        for (int i=0; i<args.length; i++) {
          vs = args[i].add(vs);
        }
        return vs;
      }

    Allocator[] collectAllocs(Facts facts) {
        int l              = args.length;
        Allocator[] allocs = null;
        for (int i=0; i<l; i++) {
          Tail t = args[i].lookupFact(facts);
          if (t!=null) {
            Allocator alloc = t.isAllocator(); // we're only going to keep info about Allocators
            if (alloc!=null) {
              if (allocs==null) {
                allocs = new Allocator[l];
              }
              allocs[i] = alloc;
            }
          }
        }
        return allocs;
      }

    /** Special case treatment for top-level bindings of the form  x <- return y;
     *  we want to short out such bindings whenever possible by replacing all
     *  occurrences of x with y.
     *  TODO: is this comment out of date?
     */
    Vars liveness(Vars vs) {
        for (int i=0; i<args.length; i++) {
          args[i] = args[i].shortTopLevel();
          vs = args[i].add(vs);
        }
        return vs;
      }

    /** Test to see if two Call expressions have alpha equivalent argument lists.
     */
    boolean alphaArgs(Vars thisvars, Call that, Vars thatvars) {
        for (int i=0; i<args.length; i++) {
            if (!this.args[i].alphaAtom(thisvars, that.args[i], thatvars)) {
                return false;
            }
        }
        return true;
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
