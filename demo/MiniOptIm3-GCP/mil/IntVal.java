package mil;

class IntVal extends Val {

    private int num;

    /** Default constructor.
     */
    IntVal(int num) {
        this.num = num;
    }

    void append(StringBuffer buf) { buf.append(num); }

    public int asInt()
      throws Fail { return num; }
}
