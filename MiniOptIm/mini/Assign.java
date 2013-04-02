package mini;

import compiler.Failure;
import mil.*;

/** Abstract syntax for assignment expressions.
 */
class Assign extends Expr {

    /** The variable where the result will be saved.
     */
    private Id lhs;

    /** The expression whose value will be saved.
     */
    private Expr rhs;

    /** Default constructor.
     */
    Assign(Id lhs, Expr rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    /** Print an indented description of this abstract syntax node,
     *  including a name for the node itself at the specified level
     *  of indentation, plus more deeply indented descriptions of
     *  any child nodes.
     */
    public void indent(IndentOutput out, int n) {
        out.indent(n, "Assign");
        lhs.indent(out, n+1);
        rhs.indent(out, n+1);
    }

    /** Output a description of this node (with id n), returning the
     *  next available node id after this node and all of its children
     *  have been output.
     */
    public int toDot(DotOutput dot, int n) {
        return rhs.toDot(dot, n,
               lhs.toDot(dot, n,
               dot.node("Assign", n)));
    }

    /** Return the type of value that will be produced when this
     *  expression is evaluated.
     */
    public Type typeOf(Context ctxt, VarEnv env)
      throws Failure {
        Type lt = lhs.typeOf(ctxt, env);
        Type rt = rhs.typeOf(ctxt,env);
        if (!lt.equal(rt)) {
            ctxt.report(new Failure("Types in assignment do not match"));
        }
        return rt;
    }

    /** Compile an expression into a tail that is passed as an argument
     *  to the specified continuation.
     */
    public Code compTail(final TailCont kt) { // lhs = rhs
        return rhs.compTail(new TailCont() {
            Code with(final Tail t) {
                return lhs.assign(t, kt);
            }
        });
    }
}
