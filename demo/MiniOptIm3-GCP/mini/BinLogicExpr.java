package mini;
import compiler.Failure;
import mil.*;

/** Abstract syntax for binary logical expressions.
 */
abstract class BinLogicExpr extends BinExpr {

    /** Default constructor.
     */
    BinLogicExpr(Expr left, Expr right) {
        super(left, right);
    }

    /** Return the type of value that will be produced when this
     *  expression is evaluated.
     */
    public Type typeOf(Context ctxt, VarEnv env)
      throws Failure {
        // Covers &&, ||
        if (!left.typeOf(ctxt, env).equal(Type.BOOLEAN) ||
            !right.typeOf(ctxt, env).equal(Type.BOOLEAN)) {
            ctxt.report(new Failure("Boolean operands required"));
        }
        return Type.BOOLEAN;
    }
}
