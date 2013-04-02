package mini;

import compiler.Failure;
import mil.*;

/** Abstract syntax for logical not expressions (!).
 */
class LNot extends UnExpr {

    /** Default constructor.
     */
    LNot(Expr exp) {
        super(exp);
    }

    /** Return a string that provides a simple description of this
     *  particular type of operator node.
     */
    String label() { return "Logical not, !"; }

    /** Return the type of value that will be produced when this
     *  expression is evaluated.
     */
    public Type typeOf(Context ctxt, VarEnv env)
      throws Failure {
        if (!exp.typeOf(ctxt, env).equal(Type.BOOLEAN)) {
            ctxt.report(new Failure("Logical not expects boolean argument"));
        }
        return Type.BOOLEAN;
    }

    /** Compile an expression into a tail that is passed as an argument
     *  to the specified continuation.
     */
    public Code compTail(final TailCont kt) { // !exp
        return exp.compVar(new VarCont() {
            Code with(final Var v) {
                Temp  r    = new Temp();
                mil.Block join = new mil.Block(kt.with(new mil.Return(r)));
                return Stmt.ifthenelse(v,
                    new Bind(r, DataAlloc.False, join.toCode()),
                    new Bind(r, DataAlloc.True,  join.toCode()));
             }
        });
    }
}
