package mini;

import compiler.Failure;
import mil.*;

/** Abstract syntax for for-loop initializers.
 */
abstract class ForInit {

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

    /** Check that this for initializer is valid, returning the
     *  variable environment after the initializer has run, which
     *  may have added new entries if the initializer introduces
     *  local variables (i.e., the VarDeclInit case).
     */
    public abstract VarEnv check(Fundef def, Context ctxt, VarEnv env)
      throws Failure;

    /** Generate code that will execute this initializer and then continue
     *  with the code specified by the andThen argument.
     */
    abstract Code compInit(final Code andThen);
}
