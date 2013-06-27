package mini;
import compiler.Failure;
import mil.*;

/** Abstract syntax for equality test expressions (==).
 */
class Eql extends BinEqualityExpr {

    /** Default constructor.
     */
    Eql(Expr left, Expr right) {
        super(left, right);
    }

    /** Return a string that provides a simple description of this
     *  particular type of operator node.
     */
    String label() { return "Eql, =="; }

    /** Compile an expression into a tail that is passed as an argument
     *  to the specified continuation.
     */
    public Code compTail(final TailCont kt) { // left == right
        Prim op = lt.equal(Type.BOOLEAN) ? Prim.beq:
                  lt.equal(Type.INT)     ? Prim.eq :
                                           Prim.deq;
        return left.binary(op, right, kt);
    }
}
