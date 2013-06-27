package mil;

public class Const extends Atom {

    private int val;

    /** Default constructor.
     */
    public Const(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }

    /** Generate a printable description of this atom.
     */
    public String toString() { return "" + val; }

    /** Test to see if two atoms are the same.  For a pair of Const objects,
     *  this means that the two objects have the same val.  For any other
     *  pair of Atoms, we expect the objects themselves to be the same.
     */
    public boolean sameAtom(Atom that) {
        Const c = that.isConst();
        return c!=null && c.getVal()==this.val;
    }

    /** Test to determine whether this Atom is a constant (or not).
     */
    public Const isConst() { return this; }

    public Val lookup(ValEnv env)
      throws Fail { return new IntVal(val); }

    /** Compute an integer summary for a fragment of MIL code with the key property
     *  that alpha equivalent program fragments have the same summary value.
     */
    int summary() { return val; }
}
