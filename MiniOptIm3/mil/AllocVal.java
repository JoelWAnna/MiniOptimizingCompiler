package mil;

abstract class AllocVal extends Val {

    protected Val[] vals;

    /** Default constructor.
     */
    AllocVal(Val[] vals) {
        this.vals = vals;
    }

    protected void append(StringBuffer buf, char open, char close) {
        buf.append(open);
        String sep = "";
        for (int i=0; i<vals.length; i++) {
            buf.append(sep);
            sep = ", ";
            vals[i].append(buf);
        }
        buf.append(close);
    }
}
