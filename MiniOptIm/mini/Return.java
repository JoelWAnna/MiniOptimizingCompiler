package mini;

import compiler.Failure;
import mil.*;

/** Abstract syntax for return statements.
 */
class Return extends Stmt {

    /** The value that will be returned (or null if there
     *  is no return result in a void procedure).
     */
    private Expr expr;

    /** Default constructor.
     */
    Return(Expr expr) {
        this.expr = expr;
    }

    /** Special version of the constructor for Return to be used when
     *  no return expression is specified.
     */
    Return() {
        this(null);
    }

    /** Print an indented description of this abstract syntax node,
     *  including a name for the node itself at the specified level
     *  of indentation, plus more deeply indented descriptions of
     *  any child nodes.
     */
    public void indent(IndentOutput out, int n) {
        out.indent(n, "Return");
        if (expr!=null) {
            expr.indent(out, n+1);
        }
    }

    /** Output a description of this node (with id n), returning the
     *  next available node id after this node and all of its children
     *  have been output.
     */
    public int toDot(DotOutput dot, int n) {
        int c = dot.node("Return", n);
        return (expr==null) ? c : expr.toDot(dot, n, c);
    }

    /** Check that this statement is valid, taking the current environment
     *  as an argument and returning a possibly modified environment as a
     *  result.
     */
    public VarEnv check(Fundef def, Context ctxt, boolean inLoop, VarEnv env)
      throws Failure {
        return def.checkReturn(expr, ctxt, env);
    }

    /** Generate code that will execute this statement and then continue
     *  with the code specified by the andThen argument.
     */
    Code compStmt(final Code andThen, final mil.Block breakBlock, final mil.Block contBlock) {    // RETURN [expr];
        if (expr==null) {
            return Fundef.retvoid;
        } else {
            return expr.compVar(new VarCont() {
                Code with(final Var rv)  {
                    return new Done(new mil.Return(rv));
                }
            });
        }
    }
}
