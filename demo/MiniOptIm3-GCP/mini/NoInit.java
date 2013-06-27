package mini;
import compiler.Failure;
import mil.*;

/** Abstract syntax for an empty for-loop initializer.
 */
class NoInit extends ForInit {

    /** Print an indented description of this abstract syntax node,
     *  including a name for the node itself at the specified level
     *  of indentation, plus more deeply indented descriptions of
     *  any child nodes.
     */
    public void indent(IndentOutput out, int n) { 
        out.indent(n, "NoInit");
    }

    /** Output a description of this node (with id n), returning the
     *  next available node id after this node and all of its children
     *  have been output.
     */
    public int toDot(DotOutput dot, int n) {
        return dot.node("NoInit", n);
    }

    /** Check that this for initializer is valid, returning the
     *  variable environment after the initializer has run, which
     *  may have added new entries if the initializer introduces
     *  local variables (i.e., the VarDeclInit case).
     */
    public VarEnv check(Fundef def, Context ctxt, VarEnv env)
      throws Failure {
        return env;
    }

    /** Generate code that will execute this initializer and then continue
     *  with the code specified by the andThen argument.
     */
    Code compInit(final Code andThen) {
        return andThen;
    }
}
