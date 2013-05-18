package mini;
import compiler.Failure;
import mil.*;

/** Abstract syntax for binary bitwise operations.
 */
abstract class BinBitwiseExpr extends BinExpr {

    /** Default constructor.
     */
    BinBitwiseExpr(Expr left, Expr right) {
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
        // Covers &, |, ^
        lt      = left.typeOf(ctxt, env);
        Type rt = right.typeOf(ctxt, env);
        if (!lt.equal(Type.INT) && !lt.equal(Type.BOOLEAN)) {
            throw new Failure("Invalid operand types for arithmetic operation");
        }
        if (!lt.equal(rt)) {
            throw new Failure("Operands have different types");
        }
        return lt;
    }
}
