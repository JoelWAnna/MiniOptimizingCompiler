package mil;

public class NAC extends Atom {

    private NAC() {}

    public static final NAC obj = new NAC();

    public String toString() { return "Not a constant"; }

    public Val lookup(ValEnv env)
      throws Fail { throw new Fail("lookup called on " + "Not a constant"); }
}
