package mil;

public class Wildcard extends Var {
    private Wildcard() {}
    public static final Wildcard obj = new Wildcard();
    public String toString() { return "_"; }
}
