package mini;

import compiler.Failure;
import mil.*;

/** Abstract syntax for expressions used as statements.
 */
class ExprStmt extends Stmt {

    /** The expression that will be executed as a statement.
     */
    private Expr exp;

    /** Default constructor.
     */
    ExprStmt(Expr exp) {
        this.exp = exp;
    }

    /** Print an indented description of this abstract syntax node,
     *  including a name for the node itself at the specified level
     *  of indentation, plus more deeply indented descriptions of
     *  any child nodes.
     */
    public void indent(IndentOutput out, int n) {
        out.indent(n, "ExprStmt");
        exp.indent(out, n+1);
    }

    /** Output a description of this node (with id n), returning the
     *  next available node id after this node and all of its children
     *  have been output.
     */
    public int toDot(DotOutput dot, int n) {
        return exp.toDot(dot, n,
               dot.node("ExprStmt", n));
    }

    /** Check that this statement is valid, taking the current environment
     *  as an argument and returning a possibly modified environment as a
     *  result.
     */
    public VarEnv check(Fundef def, Context ctxt, boolean inLoop, VarEnv env)
      throws Failure {
        // type check the expression, but discard the result.
        exp.checkExpr(ctxt, env);
        def.returns = false;
        return env;
    }

    /** Generate code that will execute this statement and then continue
     *  with the code specified by the andThen argument.
     */
    Code compStmt(final Code andThen, final mil.Block breakBlock, final mil.Block contBlock) {  // exp;
        return exp.compTail(new TailCont() {
            Code with(final Tail t)  {
                return new Bind(Wildcard.obj, t, andThen);
            }
        });
    }
}
