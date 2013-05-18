package mini;
import compiler.Failure;
import mil.*;

/** Abstract syntax for multiply expressions.
 */
class Mul extends BinArithExpr {

    /** Default constructor.
     */
    Mul(Expr left, Expr right) {
        super(left, right);
    }

    /** Return a string that provides a simple description of this
     *  particular type of operator node.
     */
    String label() { return "Mul, *"; }

    /** Compile an expression into a tail that is passed as an argument
     *  to the specified continuation.
     */
    public Code compTail(final TailCont kt) { // left * right
        Prim op = lt.equal(Type.INT) ? Prim.mul : Prim.dmul;
        return left.binary(op, right, kt);
    }
}
