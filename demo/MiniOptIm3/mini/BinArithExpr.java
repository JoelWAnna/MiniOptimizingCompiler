package mini;
import compiler.Failure;
import mil.*;

/** Abstract syntax for binary arithmetic expressions.
 */
abstract class BinArithExpr extends BinExpr {

    /** Default constructor.
     */
    BinArithExpr(Expr left, Expr right) {
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
        // Covers +, -, *, /
        lt      = left.typeOf(ctxt, env);
        Type rt = right.typeOf(ctxt, env);
        if (!lt.equal(rt)) {
            throw new Failure("Arithmetic operands have different types");
        }
        if (!lt.equal(Type.INT) && !lt.equal(Type.DOUBLE)) {
            throw new Failure("Invalid operand types for arithmetic operation");
        }
        return lt;
    }
}
