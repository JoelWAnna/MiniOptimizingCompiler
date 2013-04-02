package mil;

public class ValEnv {
    private Var v;
    private Val val;
    private ValEnv rest;

    /** Default constructor.
     */
    public ValEnv(Var v, Val val, ValEnv rest) {
        this.v = v;
        this.val = val;
        this.rest = rest;
    }
    public static ValEnv extend(Var[] vars, Val[] vals, ValEnv env) {
        for (int i=0; i<vars.length; i++) {
            env = new ValEnv(vars[i], vals[i], env);
        }
        return env;
    }
    public static Val lookup(Var v, ValEnv env)
      throws Fail {
        while (env!=null) {
            if (v==env.v) {
                return env.val;
            }
        }
        throw new Fail("Could not find value for variable " + v);
    }
    public static Val[] lookup(Atom[] atoms, ValEnv env)
      throws Fail {
        Val[] vals = new Val[atoms.length];
        for (int i=0; i<atoms.length; i++) {
            vals[i] = atoms[i].lookup(env);
        }
        return vals;
    }
}
