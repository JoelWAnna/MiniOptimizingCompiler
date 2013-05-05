package mini;
import compiler.Failure;
import mil.*;

/** Abstract syntax for statements.
 */
public abstract class Stmt {

    /** Print an indented description of this abstract syntax node,
     *  including a name for the node itself at the specified level
     *  of indentation, plus more deeply indented descriptions of
     *  any child nodes.
     */
    public abstract void indent(IndentOutput out, int n);

    /** Output a description of this node (with id n), including a
     *  link to its parent node (with id p) and returning the next
     *  available node id.
     */
    public int toDot(DotOutput dot, int p, int n) {
        dot.join(p, n);
        return toDot(dot, n);
    }

    /** Output a description of this node (with id n), returning the
     *  next available node id after this node and all of its children
     *  have been output.
     */
    public abstract int toDot(DotOutput dot, int n);

    /** Check that this statement is valid, taking the current environment
     *  as an argument and returning a possibly modified environment as a
     *  result.
     */
    public abstract VarEnv check(Fundef def, Context ctxt, boolean inLoop, VarEnv env)
      throws Failure;

    /** Produce code for an if-then-else.  Generated code will execute the
     *  first code sequence if the variable is True, but otherwise will default
     *  to the second.
     */
    protected static Code ifthenelse(Var v, Code tc, Code fc) {
        TAlt tbranch = new TAlt(Cfun.True, Var.noVars, tc.makeBlockCall());
        return new Match(v, new TAlt[] { tbranch }, fc.makeBlockCall());
    }

    /** Generate code that will execute this statement and then continue
     *  with the code specified by the andThen argument.
     */
    abstract Code compStmt(final Code andThen, final mil.Block breakBlock, final mil.Block contBlock);
}
