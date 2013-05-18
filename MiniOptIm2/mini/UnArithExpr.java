package mini;
import compiler.Failure;
import mil.*;

/** Abstract syntax for unary expressions that operate on
 *  numeric arguments.
 */
abstract class UnArithExpr extends UnExpr {

    /** Default constructor.
     */
    UnArithExpr(Expr exp) {
        super(exp);
    }

    /** Records the argument type for this expression so that we can
     *  generate appropriate instructions during code generation.
     */
    protected Type at = null;

    /** Return the type of value that will be produced when this
     *  expression is evaluated.
     */
    public Type typeOf(Context ctxt, VarEnv env)
      throws Failure {
        // Covers unary plus and unary minus:
        at = exp.typeOf(ctxt, env);
        if (!at.equal(Type.INT) && !at.equal(Type.DOUBLE)) {
            throw new Failure("Unary operation expects numeric argument");
        }
        return at;
    }
}
