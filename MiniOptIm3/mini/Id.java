package mini;
import compiler.Failure;
import mil.*;

/** Abstract syntax for identifiers/variables.
 */
class Id extends Expr {

    /** The identifier name.
     */
    String name;

    /** Default constructor.
     */
    Id(String name) {
        this.name = name;
    }

    /** Generate a printable description of this identifier.
     */
    public String toString() {
        return name;
    }

    /** Print an indented description of this abstract syntax node,
     *  including a name for the node itself at the specified level
     *  of indentation, plus more deeply indented descriptions of
     *  any child nodes.
     */
    public void indent(IndentOutput out, int n) {
        out.indent(n, "Id, " + name);
    }

    /** Output a description of this node (with id n), returning the
     *  next available node id after this node and all of its children
     *  have been output.
     */
    public int toDot(DotOutput dot, int n) {
        return dot.node("Id, " + name, n);
    }

    /** Test to see if two identifiers have the same name.
     */
    public boolean equals(Id that) {
        return this.name.equals(that.name);
    }

    /** Record the environment for this variable so that
     *  we can access its details during code generation.
     */
    private VarEnv ve = null;

    /** Return the type of value that will be produced when this
     *  expression is evaluated.
     */
    public Type typeOf(Context ctxt, VarEnv env)
      throws Failure {
        ve = VarEnv.find(this, env);
        if (ve==null) {
            throw new Failure("The variable " + name + " is not in scope");
        }
        return ve.getType();
    }

    /** Compile an expression into a variable that is passed as an argument to
     *  the specified continuation.
     */
    public Code compVar(final VarCont kv) { return kv.with(ve.getVar()); }

    /** Generate code for an assignment, saving the result produced by
     *  the specified tail in this identifier, and passing a copy of
     *  that value on to the given continuation.
     */
    Code assign(Tail t, TailCont kt) {
        Var v = ve.getVar();
        return new Bind(v, t, kt.with(new mil.Return(v)));
    }

    /** Compile an expression into a tail that is passed as an argument
     *  to the specified continuation.
     */
    public Code compTail(final TailCont kt) {
        return kt.with(new mil.Return(ve.getVar()));
    }
}
