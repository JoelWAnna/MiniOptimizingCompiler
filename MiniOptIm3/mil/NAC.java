package mil;

/**
 *When constructing the lattice for each argument to a block call, it is necessary to have two predefined
 *values, for the top and bottom of the lattice. New Atom children classes NAC & UNDEF were added to identify
 *if an argument is Not A Constant (NAC) or no information is known about the argument UNDEF.
 */
public class NAC extends Atom {

    private NAC() {}

    public static final NAC obj = new NAC();

    public String toString() { return "Not a constant"; }

    public Val lookup(ValEnv env)
      throws Fail { throw new Fail("lookup called on " + "Not a constant"); }
}
