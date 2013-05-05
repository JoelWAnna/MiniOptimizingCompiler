package mil;

class DataVal extends AllocVal {

    private Cfun c;

    /** Default constructor.
     */
    DataVal(Val[] vals, Cfun c) {
        super(vals);
        this.c = c;
    }

    void append(StringBuffer buf) { c.append(buf); append(buf, '(', ')'); }

    public Val match(TAlt[] alts, BlockCall def, ValEnv env)
      throws Fail {
        for (int i=0; i<alts.length; i++) {
            Val val = alts[i].match(c, vals, env);
            if (val!=null) {
                return val;
            }
        }
        return def.eval(env);
    }
}
