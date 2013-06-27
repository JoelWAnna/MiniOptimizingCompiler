package mil;

public class Wildcard extends Var {

    private Wildcard() {}

    public static final Wildcard obj = new Wildcard();

    /** Generate a printable description of this atom.
     */
    public String toString() { return "_"; }
}
