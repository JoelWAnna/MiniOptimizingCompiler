package mil;

class CompVal extends AllocVal {
    private Block m;

    /** Default constructor.
     */
    CompVal(Val[] vals, Block m) {
        super(vals);
        this.m = m;
    }
    void append(StringBuffer buf) { m.append(buf); append(buf, '[', ']'); }
    public Val invoke()
      throws Fail { return m.call(vals); }
}
