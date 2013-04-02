package mil;

class ClosVal extends AllocVal {
    private ClosureDefn k;

    /** Default constructor.
     */
    ClosVal(Val[] vals, ClosureDefn k) {
        super(vals);
        this.k = k;
    }
    void append(StringBuffer buf) { k.append(buf); append(buf, '{', '}'); }
    public Val enter(Val val)
      throws Fail { return k.enter(vals, val); }
}
