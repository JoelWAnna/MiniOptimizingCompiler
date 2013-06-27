package mil;

public class UNDEF extends Atom {

    private UNDEF() {}

    public static final UNDEF obj = new UNDEF();

    public String toString() { return "undefined"; }

    public Val lookup(ValEnv env)
      throws Fail { throw new Fail("lookup called on " + "undefined"); }
}
