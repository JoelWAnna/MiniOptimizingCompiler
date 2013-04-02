package mil;

public class Cfun extends TopLevel {
    private int arity;
    int num;
    int outOf;

    /** Default constructor.
     */
    public Cfun(String id, int arity, int num, int outOf) {
        super(id);
        this.arity = arity;
        this.num = num;
        this.outOf = outOf;
    }
    public int getArity() {
        return arity;
    }
    public int getNum() {
        return num;
    }
    public int getOutOf() {
        return outOf;
    }
    public static final Cfun True = new Cfun("True",    0, 2, 2);
    public static final Cfun False = new Cfun("False",   0, 1, 2);
    public static final Cfun Nil = new Cfun("Nil",     0, 1, 2);
    public static final Cfun Cons = new Cfun("Cons",    2, 2, 2);
    public static final Cfun Pair = new Cfun("Pair",    2, 1, 1);
    public static final Cfun Nothing = new Cfun("Nothing", 0, 1, 2);
    public static final Cfun Just = new Cfun("Just",    1, 2, 2);

    /** Test to determine if this item represents a constructor function.
     */
    public Cfun isCfun() { return this; }
    boolean isNewtype() {
        return outOf==1 && arity==1;  // num==1 is also required but implied by outOf==1
      }
}
