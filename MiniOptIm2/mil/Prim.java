package mil;

public class Prim {

    /** The name that will be used for this primitive.
     */
    private String id;

    /** The arity/number of arguments for this primitive.
     */
    private int arity;

    /** Records the purity setting (PURE, IMPURE, or THUNK) for this
     *  primitive.
     */
    private int purity;

    /** Flag to indicate if this function will never return,
     *  meaning that anything following a call to this function
     *  will be ignored.
     */
    private boolean doesntReturn;

    /** Default constructor.
     */
    public Prim(String id, int arity, int purity, boolean doesntReturn) {
        this.id = id;
        this.arity = arity;
        this.purity = purity;
        this.doesntReturn = doesntReturn;
    }

    /** Return the name of this primitive.
     */
    public String getId() {
        return id;
    }

    /** Return the arity for this primitive.
     */
    public int getArity() {
        return arity;
    }

    /** Identifies a "pure" primitive, that is, a primitive that has no effect other than to
     *  compute a value.  In particular, a call to a pure primitive in a context where the
     *  result will not be used can always be deleted without a change in program semantics.
     */
    public static final int PURE = 0;

    /** Identifies an "impure" primitive, which is a primitive that, although not monadic,
     *  can have an effect when executed.  In particular, this means that a call to an
     *  impure primitive cannot be deleted, even if its result is not used.  This includes
     *  operations like division that, without any typing tricks to eliminate division by
     *  zero, can raise an exception.  Removing a use of division---except in cases where
     *  the second argument is a known, nonzero constant---could potentially change the
     *  meaning of a program.
     */
    public static final int IMPURE = 1;

    /** Identifies a "monadic" primitive that requires a thunk.
     */
    public static final int THUNK = 2;

    /** Determine if this primitive is pure, i.e., if a call to this primitive can
     *  be deleted if its result is not used.
     */
    public boolean isPure() { return purity==PURE; }

    public static final Prim not = new Prim("not",  1, PURE, false);

    public static final Prim and = new Prim("and",  2, PURE, false);

    public static final Prim or = new Prim("or",   2, PURE, false);

    public static final Prim xor = new Prim("xor",  2, PURE, false);

    public static final Prim bnot = new Prim("bnot", 1, PURE, false);

    public static final Prim shl = new Prim("shl",  2, PURE, false);

    public static final Prim shr = new Prim("shr",  2, PURE, false);

    public static final Prim neg = new Prim("neg",  1, PURE, false);

    public static final Prim add = new Prim("add",  2, PURE, false);

    public static final Prim sub = new Prim("sub",  2, PURE, false);

    public static final Prim mul = new Prim("mul",  2, PURE, false);

    public static final Prim div = new Prim("div",  2, IMPURE, false);

    public static final Prim eq = new Prim("eq",   2, PURE, false);

    public static final Prim neq = new Prim("neq",  2, PURE, false);

    public static final Prim lt = new Prim("lt",   2, PURE, false);

    public static final Prim lte = new Prim("lte",  2, PURE, false);

    public static final Prim gt = new Prim("gt",   2, PURE, false);

    public static final Prim gte = new Prim("gte",  2, PURE, false);

    /** Represents a primitive that halts/terminates the current program.
     */
    public static final Prim halt = new Prim("halt", 0, IMPURE, true);

    public Val call(Val[] vals)
      throws Fail {
        if (arity!=vals.length) {
            throw new Fail("primitive " + id + " does not have " + arity + " arguments");
        }
        if (id.equals("add")) {
            return new IntVal(vals[0].asInt() + vals[1].asInt());
        } else if (id.equals("sub")) {
            return new IntVal(vals[0].asInt() - vals[1].asInt());
        } else if (id.equals("eq")) {
            return Val.asBool(vals[0].asInt() == vals[1].asInt());
        } else {
          throw new Fail("No implementation for primitive " + id);
        }
    }

    /** Test for code that is guaranteed not to return.
     */
    boolean doesntReturn() { return doesntReturn; }

    /** Represents a primitive that will enter a non-terminating loop.  Marked as
     *  impure because eliminating a call to this primitive, even when its value
     *  is not used will typically change the behavior of the enclosing program.
     */
    public static final Prim loop = new Prim("loop", 0, Prim.IMPURE, true);

    /** Compute an integer summary for a fragment of MIL code with the key property
     *  that alpha equivalent program fragments have the same summary value.
     */
    int summary() { return getId().hashCode(); }

    /** Calculate a summary value for a list of Atom values, typically the arguments
     *  in a Call.
     */
    int summary(Atom[] args) {
        int sum = summary();
        for (int i=0; i<args.length; i++) {
            sum = 53*sum + args[i].summary();
        }
        return sum;
    }

    /** Represents a primitive for printing an integer argument.
     */
    public static final Prim print = new Prim("print",    1, IMPURE, false);

    /** Represents a primitive for loading an integer from an address
     *  in memory.
     */
    public static final Prim load = new Prim("load",     1, IMPURE, false);

    /** Represents a primitive for saving an integer value at an address
     *  in memory.
     */
    public static final Prim store = new Prim("store",    2, IMPURE, false);

    /** Represents a primitive for allocating a new array.
     */
    public static final Prim newarray = new Prim("newarray", 1, IMPURE, false);

    /** Represents a primtive for (strict) boolean and.
     */
    public static final Prim band = new Prim("band", 2, PURE, false);

    /** Represents a primtive for (strict) boolean or.
     */
    public static final Prim bor = new Prim("bor",  2, PURE, false);

    /** Represents a primtive for boolean xor.
     */
    public static final Prim bxor = new Prim("bxor", 2, PURE, false);

    /** Represents a primtive for boolean equality.
     */
    public static final Prim beq = new Prim("beq",  2, PURE, false);

    /** Represents a primtive for boolean inequality.
     */
    public static final Prim bneq = new Prim("bneq", 2, PURE, false);

    /** Represents a primtive for double negation.
     */
    public static final Prim dneg = new Prim("dneg", 1, PURE, false);

    /** Represents a primtive for double equality.
     */
    public static final Prim deq = new Prim("deq",  2, PURE, false);

    /** Represents a primtive for double inequality.
     */
    public static final Prim dneq = new Prim("dneq", 2, PURE, false);

    /** Represents a primtive for double greater than.
     */
    public static final Prim dgt = new Prim("dgt",  2, PURE, false);

    /** Represents a primtive for double greater than or equal.
     */
    public static final Prim dgte = new Prim("dgte", 2, PURE, false);

    /** Represents a primtive for double less than.
     */
    public static final Prim dlt = new Prim("dlt",  2, PURE, false);

    /** Represents a primtive for double less than or equal.
     */
    public static final Prim dlte = new Prim("dlte", 2, PURE, false);

    /** Represents a primtive for double addition.
     */
    public static final Prim dadd = new Prim("dadd", 2, PURE, false);

    /** Represents a primtive for double subtract.
     */
    public static final Prim dsub = new Prim("dsub", 2, PURE, false);

    /** Represents a primtive for double multiply.
     */
    public static final Prim dmul = new Prim("dmul", 2, PURE, false);

    /** Represents a primtive for double divide.
     */
    public static final Prim ddiv = new Prim("ddiv", 2, PURE, false);
}
