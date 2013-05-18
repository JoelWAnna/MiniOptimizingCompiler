package mini;
import compiler.Failure;
import mil.*;

/** Abstract syntax for array accesses.
 */
class ArrayAccess extends Expr {

    /** The array to read from.
     */
    private Expr arr;

    /** The index within the array.
     */
    private Expr idx;

    /** Default constructor.
     */
    ArrayAccess(Expr arr, Expr idx) {
        this.arr = arr;
        this.idx = idx;
    }

    /** Print an indented description of this abstract syntax node,
     *  including a name for the node itself at the specified level
     *  of indentation, plus more deeply indented descriptions of
     *  any child nodes.
     */
    public void indent(IndentOutput out, int n) {
        out.indent(n, "ArrayAccess");
        arr.indent(out, n+1);
        idx.indent(out, n+1);
    }

    /** Output a description of this node (with id n), returning the
     *  next available node id after this node and all of its children
     *  have been output.
     */
    public int toDot(DotOutput dot, int n) {
        return idx.toDot(dot, n,
               arr.toDot(dot, n,
               dot.node("ArrayAccess", n)));
    }

    /** Return the type of value that will be produced when this
     *  expression is evaluated.
     */
    public Type typeOf(Context ctxt, VarEnv env)
      throws Failure {
        // Calculate type of elements in arr, which must be an array:
        Type et = arr.typeOf(ctxt,env).elementType();
        if (et==null) {
            throw new Failure("Array expression does not have array type");
        }
        // Make sure index type is an int
        if (!idx.typeOf(ctxt,env).equal(Type.INT)) {
            ctxt.report(new Failure("Arithmetic operands have different types"));
        }
        // Return the array element type:
        return et;
    }

    /** Compile an expression into a tail that is passed as an argument
     *  to the specified continuation.
     */
    public Code compTail(final TailCont kt) { // arr[idx]
        return arr.elemAddress(idx, new VarCont() {
                   Code with(final Var m) {
                       return kt.with(new PrimCall(Prim.load, m));
                   }
               });
    }
}
