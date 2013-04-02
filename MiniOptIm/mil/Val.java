package mil;

public abstract class Val {
    public String toString() {
        StringBuffer buf = new StringBuffer();
        this.append(buf);
        return buf.toString();
    }
    abstract void append(StringBuffer buf);
    public Val match(TAlt[] alts, BlockCall def, ValEnv env)
      throws Fail { throw new Fail("Runtime type error in pattern match"); }
    public Val enter(Val val)
      throws Fail { throw new Fail("Runtime type error in enter"); }
    public Val invoke()
      throws Fail { throw new Fail("Runtime type error in invoke"); }
    public int asInt()
      throws Fail { throw new Fail("Runtime type error, integer value expected"); }
    public static final Val[] noVals = new Val[0];
    public static final Val trueVal = new DataVal(noVals, Cfun.True);
    public static final Val falseVal = new DataVal(noVals, Cfun.False);
    public static Val asBool(boolean b) {
        return b ? trueVal : falseVal;
    }
}
