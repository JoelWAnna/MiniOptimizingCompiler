package mini;
import compiler.Failure;
import mil.*;

/** Abstract syntax for block statements.
 */
class Block extends Stmt {

    /** The list of statements in this block.
     */
    private Stmts stmts;

    /** Default constructor.
     */
    Block(Stmts stmts) {
        this.stmts = stmts;
    }

    /** Print an indented description of this abstract syntax node,
     *  including a name for the node itself at the specified level
     *  of indentation, plus more deeply indented descriptions of
     *  any child nodes.
     */
    public void indent(IndentOutput out, int n) {
        out.indent(n, "Block");
        stmts.indent(out, n+1);
    }

    /** Output a description of this node (with id n), returning the
     *  next available node id after this node and all of its children
     *  have been output.
     */
    public int toDot(DotOutput dot, int n) {
        return stmts.toDot(dot, n,
               dot.node("Block", n));
    }

    /** Check that this statement is valid, taking the current environment
     *  as an argument and returning a possibly modified environment as a
     *  result.
     */
    public VarEnv check(Fundef def, Context ctxt, boolean inLoop, VarEnv env)
      throws Failure {
        stmts.check(def, ctxt, inLoop, env);
        return env;
    }

    /** Generate code that will execute this statement and then continue
     *  with the code specified by the andThen argument.
     */
    Code compStmt(final Code andThen, final mil.Block breakBlock, final mil.Block contBlock) {     // { stmts }
        return stmts.compStmt(andThen, breakBlock, contBlock);
    }
}
