package mini;
import compiler.Failure;
import mil.*;

/** Abstract syntax for assignments to array elements.
 */
class ArrayAssign extends Expr {

    /** The array that is being modified.
     */
    private Expr arr;

    /** The index of the element in the array that is being modified.
     */
    private Expr idx;

    /** The expression whose value will be saved in the specified
     *  array location.
     */
    private Expr rhs;

    /** Default constructor.
     */
    ArrayAssign(Expr arr, Expr idx, Expr rhs) {
        this.arr = arr;
        this.idx = idx;
        this.rhs = rhs;
    }

    /** Print an indented description of this abstract syntax node,
     *  including a name for the node itself at the specified level
     *  of indentation, plus more deeply indented descriptions of
     *  any child nodes.
     */
    public void indent(IndentOutput out, int n) {
        out.indent(n, "ArrayAssign");
        arr.indent(out, n+1);
        idx.indent(out, n+1);
        rhs.indent(out, n+1);
    }

    /** Output a description of this node (with id n), returning the
     *  next available node id after this node and all of its children
     *  have been output.
     */
    public int toDot(DotOutput dot, int n) {
        return rhs.toDot(dot, n,
               idx.toDot(dot, n,
               arr.toDot(dot, n,
               dot.node("ArrayAssign", n))));
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
        // Make sure that the rhs type matches the element type:
        if (!rhs.typeOf(ctxt,env).equal(et)) {
            throw new Failure("Right hand side of assignment does not match array type");
        }
        // Return the array element type:
        return et;
    }

    /** Compile an expression into a tail that is passed as an argument
     *  to the specified continuation.
     */
    public Code compTail(final TailCont kt) { // arr[idx] = rhs
        return arr.elemAddress(idx, new VarCont() {
            Code with(final Var m) {
               return rhs.compVar(new VarCont() {
                   Code with(final Var v) {
                       return new Bind(Wildcard.obj,
                                       new PrimCall(Prim.store, m, v),
                                       kt.with(new mil.Return(v)));
                   }
               });
           }
       });
    }
}
