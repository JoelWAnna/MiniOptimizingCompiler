package mini;
import compiler.Failure;
import mil.*;

/** Abstract syntax for subtract expressions.
 */
class Sub extends BinArithExpr {

    /** Default constructor.
     */
    Sub(Expr left, Expr right) {
        super(left, right);
    }

    /** Return a string that provides a simple description of this
     *  particular type of operator node.
     */
    String label() { return "Sub, -"; }

    /** Compile an expression into a tail that is passed as an argument
     *  to the specified continuation.
     */
    public Code compTail(final TailCont kt) { // left - right
        Prim op = lt.equal(Type.INT) ? Prim.sub : Prim.dsub;
        return left.binary(op, right, kt);
    }
}
