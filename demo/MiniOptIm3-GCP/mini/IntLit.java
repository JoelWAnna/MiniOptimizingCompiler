package mini;
import compiler.Failure;
import mil.*;

/** Abstract syntax for integer literals.
 */
class IntLit extends Expr {

    /** The value of this integer literal.
     */
    private String num;

    /** Default constructor.
     */
    IntLit(String num) {
        this.num = num;
    }

    /** Print an indented description of this abstract syntax node,
     *  including a name for the node itself at the specified level
     *  of indentation, plus more deeply indented descriptions of
     *  any child nodes.
     */
    public void indent(IndentOutput out, int n) {
        out.indent(n, "IntLit: " + num);
    }

    /** Output a description of this node (with id n), returning the
     *  next available node id after this node and all of its children
     *  have been output.
     */
    public int toDot(DotOutput dot, int n) {
        return dot.node("IntLit: " + num, n);
    }

    /** Return the type of value that will be produced when this
     *  expression is evaluated.
     */
    public Type typeOf(Context ctxt, VarEnv env)
      throws Failure {
        return Type.INT;
    }

    /** Compile an expression into a tail that is passed as an argument
     *  to the specified continuation.
     */
    public Code compTail(final TailCont kt) {   // num
        return kt.with(new mil.Return(new Const(Integer.parseInt(num))));
    }
}
