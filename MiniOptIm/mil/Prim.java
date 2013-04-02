package mil;

public class Prim {
    private String id;
    private int arity;
    private int purity;

    /** Default constructor.
     */
    public Prim(String id, int arity, int purity) {
        this.id = id;
        this.arity = arity;
        this.purity = purity;
    }
    public String getId() {
        return id;
    }
    public int getArity() {
        return arity;
    }

    /** Identifies a "pure" primitive, that is, a primitive that has no effect
     *  other than to compute a value.  In particular, a call to a pure primitive
     *  in a context where the result will not be used can always be deleted
     *  without changing the semantics of the program.
     */
    public static final int PURE = 0;

    /** Identifies an "impure" primitive, which is a primitive that, although not
     *  monadic, can have an effect when executed.  In particular, this means that
     *  a call to an impure primitive cannot be deleted, even if its result is not
     *  used.  This includes operations like division that, without any typing
     *  tricks to eliminate division by zero, can raise an exception.  Removing a
     *  use of division---except in cases where the second argument is a known,
     *  nonzero constant---could potentially change the meaning of a program.
     */
    public static final int IMPURE = 1;

    /** Identifies a "monadic" primitive that requires a thunk.
     */
    public static final int THUNK = 2;

    /** Determine if this primitive is pure, i.e., if a call to this primitive can
     *  be deleted if its result is not used.
     */
    public boolean isPure() { return purity==PURE; }
    public static final Prim not = new Prim("not", 1, PURE);
    public static final Prim and = new Prim("and", 2, PURE);
    public static final Prim or = new Prim("or",  2, PURE);
    public static final Prim xor = new Prim("xor", 2, PURE);
    public static final Prim shl = new Prim("shl", 2, PURE);
    public static final Prim shr = new Prim("shr", 2, PURE);
    public static final Prim neg = new Prim("neg", 1, PURE);
    public static final Prim add = new Prim("add", 2, PURE);
    public static final Prim sub = new Prim("sub", 2, PURE);
    public static final Prim mul = new Prim("mul", 2, PURE);
    public static final Prim div = new Prim("div", 2, IMPURE);
    public static final Prim eq = new Prim("eq",  2, PURE);
    public static final Prim neq = new Prim("neq", 2, PURE);
    public static final Prim lt = new Prim("lt",  2, PURE);
    public static final Prim lte = new Prim("lte", 2, PURE);
    public static final Prim gt = new Prim("gt",  2, PURE);
    public static final Prim gte = new Prim("gte", 2, PURE);
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

    /** Represents a primitive that will enter a non-terminating loop.  Marked as
     *  impure because eliminating a call to this primitive, even when its value
     *  is not used will typically change the behavior of the enclosing program.
     */
    public static final Prim loop = new Prim("loop", 0, Prim.IMPURE);

    /** Represents a primitive for printing an integer argument.
     */
    public static final Prim print = new Prim("print",    1, IMPURE);

    /** Represents a primitive for loading an integer from an address
     *  in memory.
     */
    public static final Prim load = new Prim("load",     1, IMPURE);

    /** Represents a primitive for saving an integer value at an address
     *  in memory.
     */
    public static final Prim store = new Prim("store",    2, IMPURE);

    /** Represents a primitive for allocating a new array.
     */
    public static final Prim newarray = new Prim("newarray", 1, IMPURE);
}
