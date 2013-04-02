package mini;

import compiler.Failure;
import mil.*;

/** Represents a single formal parameter in a function definition,
 *  pairing a type (or null, for a void function that does not
 *  return an argument).
 */
class Formal {

    /** Holds the type of this formal parameter.
     */
    private Type type;

    /** Holds the name of this formal parameter.
     */
    private Id name;

    /** Default constructor.
     */
    Formal(Type type, Id name) {
        this.type = type;
        this.name = name;
    }

    /** Return the type of this formal parameter.
     */
    public Type getType() {
        return type;
    }

    /** Return the name of this formal parameter.
     */
    public Id getName() {
        return name;
    }

    /** Generate a printable description of this formal parameter.
     */
    public String toString() {
        return name.toString();
    }

    /** Print an indented description of this abstract syntax node,
     *  including a name for the node itself at the specified level
     *  of indentation, plus more deeply indented descriptions of
     *  any child nodes.
     */
    public void indent(IndentOutput out, int n) {
        out.indent(n, type + " " + name);
    }

    /** Output a description of this node (with id n), returning the
     *  next available node id after this node and all of its children
     *  have been output.
     */
    public int toDot(DotOutput dot, int n) {
        return dot.node(type + " " + name, n);
    }

    /** Captures the variable that represents this formal parameter.
     */
    private Var param;

    /** Retrieve the MIL variable associated with this parameter.
     */
    Var getParam() {
        return param;
    }

    /** Construct an environment that adds this formal parameter.
     */
    VarEnv extendEnv(VarEnv env) {
        param = new ParamTemp(name.toString());
        return new VarEnv(name, type, param, env);
    }
}
