package mini;

import compiler.Failure;
import mil.*;

/** Abstract syntax for unary plus expressions.
 */
class UPlus extends UnArithExpr {

    /** Default constructor.
     */
    UPlus(Expr exp) {
        super(exp);
    }

    /** Return a string that provides a simple description of this
     *  particular type of operator node.
     */
    String label() { return "Unary plus"; }

    /** Compile an expression into a tail that is passed as an argument
     *  to the specified continuation.
     */
    public Code compTail(final TailCont kt) { // +exp
        return exp.compTail(kt);
    }
}
