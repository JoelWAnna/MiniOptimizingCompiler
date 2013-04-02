package mini;

import compiler.Failure;
import mil.*;

/** Represents a variable introduction with a single variable
 *  name.
 */
public class VarIntro {

    /** The name of the variable that is introduced here.
     */
    protected Id id;

    /** Default constructor.
     */
    public VarIntro(Id id) {
        this.id = id;
    }

    /** Print an indented description of this abstract syntax node,
     *  including a name for the node itself at the specified level
     *  of indentation, plus more deeply indented descriptions of
     *  any child nodes.
     */
    public void indent(IndentOutput out, int n) {
        out.indent(n, "VarIntro");
        id.indent(out, n+1);
    }

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
    public int toDot(DotOutput dot, int n) {
        return dot.node("VarIntro: " + id, n);
    }

    /** Record the environment for this variable so that
     *  we can access its details during code generation.
     */
    protected VarEnv ve = null;

    /** Check this variable introduction, given the type for
     *  the associated variable declaration and the initial
     *  environment, and then return the modified environment.
     */
    public VarEnv check(Fundef def, Type type, Context ctxt, VarEnv env) {
        // Extend the environment:
        return ve = new VarEnv(id, type, new LocalTemp(id.toString()), env);
    }

    /** Generate code that will execute this statement and then continue
     *  with the code specified by the andThen argument.
     */
    Code compStmt(final Code andThen, final mil.Block breakBlock, final mil.Block contBlock) { // var   (initialize to 0 to ensure that var is in scope)
        return new Bind(ve.getVar(), new mil.Return(new Const(0)), andThen);
    }
}
