package mini;
import compiler.Failure;
import mil.*;

/** Abstract syntax for less than expressions.
 */
class Lt extends BinCompExpr {

    /** Default constructor.
     */
    Lt(Expr left, Expr right) {
        super(left, right);
    }

    /** Return a string that provides a simple description of this
     *  particular type of operator node.
     */
    String label() { return "Lt, <"; }

    /** Compile an expression into a tail that is passed as an argument
     *  to the specified continuation.
     */
    public Code compTail(final TailCont kt) { // left < right
        Prim op = lt.equal(Type.INT) ? Prim.lt : Prim.dlt;
        return left.binary(op, right, kt);
    }
}
