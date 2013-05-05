package mini;
import compiler.Failure;
import mil.*;

/** Abstract syntax for logical or expressions (||).
 */
class LOr extends BinLogicExpr {

    /** Default constructor.
     */
    LOr(Expr left, Expr right) {
        super(left, right);
    }

    /** Return a string that provides a simple description of this
     *  particular type of operator node.
     */
    String label() { return "LOr, ||"; }

    /** Compile an expression into a tail that is passed as an argument
     *  to the specified continuation.
     */
    public Code compTail(final TailCont kt) { // left || right
        return left.compVar(new VarCont() {
            Code with(final Var v) {
                final Temp  r    = new Temp();
                final mil.Block join = new mil.Block(kt.with(new mil.Return(r)));
                return Stmt.ifthenelse(v,
                    new Bind(r, DataAlloc.True,  join.toCode()),
                    right.compTail(new TailCont() {
                        Code with(final Tail t) {
                            return new Bind(r, t, join.toCode());
                        }
                    }));
            }
        });
    }
}
