package mini;
import compiler.Failure;
import mil.*;

/** Abstract syntax for code to construct a new array.
 */
class NewArray extends Expr {

    /** The type of the elements in the array that will be allocated.
     */
    private Type type;

    /** An expression whose value will determine how many elements the
     *  new array should contain.
     */
    private Expr size;

    /** Default constructor.
     */
    NewArray(Type type, Expr size) {
        this.type = type;
        this.size = size;
    }

    /** Print an indented description of this abstract syntax node,
     *  including a name for the node itself at the specified level
     *  of indentation, plus more deeply indented descriptions of
     *  any child nodes.
     */
    public void indent(IndentOutput out, int n) {
        out.indent(n, "NewArray of " + type);
        if (size!=null) {
            size.indent(out, n+1);
        }
    }

    /** Output a description of this node (with id n), returning the
     *  next available node id after this node and all of its children
     *  have been output.
     */
    public int toDot(DotOutput dot, int n) {
        return size.toDot(dot, n,
               dot.node("NewArray of " + type, n));
    }

    /** Return the type of value that will be produced when this
     *  expression is evaluated.
     */
    public Type typeOf(Context ctxt, VarEnv env)
      throws Failure {
        if (!size.typeOf(ctxt, env).equal(Type.INT)) {
            throw new Failure("Array size must be an integer");
        }
        return new ArrayType(type);
    }

    /** Compile an expression into a tail that is passed as an argument
     *  to the specified continuation.
     */
    public Code compTail(final TailCont kt) { // NEW type[size]
        return size.compVar(new VarCont() {
            Code with(final Var s) {
                Var bytes = new Temp();
                return new Bind(bytes, new PrimCall(Prim.mul, new Const(4), s),
                       kt.with(new PrimCall(Prim.newarray, bytes)));
            }
        });
    }
}
