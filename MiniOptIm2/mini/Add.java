package mini;
import compiler.Failure;
import mil.*;

/** Abstract syntax for add expressions.
 */
class Add extends BinArithExpr {

    /** Default constructor.
     */
    Add(Expr left, Expr right) {
        super(left, right);
    }

    /** Return a string that provides a simple description of this
     *  particular type of operator node.
     */
    String label() { return "Add, +"; }

    /** Compile an expression into a tail that is passed as an argument
     *  to the specified continuation.
     */
    public Code compTail(final TailCont kt) { // left + right
        return left.binary(Prim.add, right, kt);
    }
}
