package mini;
import compiler.Failure;
import mil.*;

/** Abstract syntax for inequality test expressions (==).
 */
class Neq extends BinEqualityExpr {

    /** Default constructor.
     */
    Neq(Expr left, Expr right) {
        super(left, right);
    }

    /** Return a string that provides a simple description of this
     *  particular type of operator node.
     */
    String label() { return "Neq, !="; }

    /** Compile an expression into a tail that is passed as an argument
     *  to the specified continuation.
     */
    public Code compTail(final TailCont kt) { // left != right
        Prim op = lt.equal(Type.BOOLEAN) ? Prim.bneq:
                  lt.equal(Type.INT)     ? Prim.neq :
                                           Prim.dneq;
        return left.binary(op, right, kt);
    }
}
