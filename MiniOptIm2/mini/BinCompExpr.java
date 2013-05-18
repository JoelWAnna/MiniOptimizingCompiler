package mini;
import compiler.Failure;
import mil.*;

/** Abstract syntax for binary comparison expressions.
 */
abstract class BinCompExpr extends BinExpr {

    /** Default constructor.
     */
    BinCompExpr(Expr left, Expr right) {
        super(left, right);
    }

    /** Records the (left) argument type for this expression so that we
     *  can generate appropriate instructions during code generation.
     */
    protected Type lt = null;

    /** Return the type of value that will be produced when this
     *  expression is evaluated.
     */
    public Type typeOf(Context ctxt, VarEnv env)
      throws Failure {
        // Covers <, >, <=, >=
        lt      = left.typeOf(ctxt, env);
        Type rt = right.typeOf(ctxt, env);
        if (!lt.equal(rt)) {
            throw new Failure("Comparison operands have different types");
        }
        if (!lt.equal(Type.INT) && !lt.equal(Type.DOUBLE)) {
            throw new Failure("Cannot compare values of type " + lt);
        }
        return Type.BOOLEAN;
    }
}
