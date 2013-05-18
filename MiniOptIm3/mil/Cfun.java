package mil;

public class Cfun extends TopLevel {

    private int arity;

    private int num;

    private Cfun[] constrs;

    /** Default constructor.
     */
    public Cfun(String id, int arity, int num, Cfun[] constrs) {
        super(id);
        this.arity = arity;
        this.num = num;
        this.constrs = constrs;
    
        constrs[num] = this; // Save this constructor in its array of constructors
    }

    public int getArity() {
        return arity;
    }

    public int getNum() {
        return num;
    }

    public Cfun[] getConstrs() {
        return constrs;
    }

    public static final Cfun[] boolType = new Cfun[2];

    public static final Cfun True = new Cfun("True",    0, 1, boolType);

    public static final Cfun False = new Cfun("False",   0, 0, boolType);

    public static final Cfun[] unitType = new Cfun[1];

    public static final Cfun Unit = new Cfun("Unit",    0, 0, unitType);

    public static final Cfun[] listType = new Cfun[2];

    public static final Cfun Nil = new Cfun("Nil",     0, 0, listType);

    public static final Cfun Cons = new Cfun("Cons",    2, 1, listType);

    public static final Cfun[] pairType = new Cfun[1];

    public static final Cfun Pair = new Cfun("Pair",    2, 0, pairType);

    public static final Cfun[] maybeType = new Cfun[2];

    public static final Cfun Nothing = new Cfun("Nothing", 0, 0, maybeType);

    public static final Cfun Just = new Cfun("Just",    1, 1, maybeType);

    /** Return true if this is a newtype constructor (i.e., a single argument
     *  constructor function for a type that only has one constructor).
     */
    boolean isNewtype() {
        return constrs.length==1 && arity==1;
        // num==1 is also required but is implied by constrs.length==1
    }

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
}
