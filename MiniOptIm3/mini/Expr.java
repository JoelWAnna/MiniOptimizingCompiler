package mini;
import compiler.Failure;
import mil.*;

/** Abstract syntax for expressions.
 */
public abstract class Expr {

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

    /** Return the type of value that will be produced when this
     *  expression is evaluated.
     */
    public abstract Type typeOf(Context ctxt, VarEnv env)
      throws Failure;

    /** Type check this expression in places where it is used as a statement.
     *  We override this method in Call to deal with methods that
     *  return void.
     */
    void checkExpr(Context ctxt, VarEnv env)
      throws Failure {
        typeOf(ctxt, env);
    }

    /** Compile an expression into a variable that is passed as an argument to
     *  the specified continuation.
     */
    public Code compVar(final VarCont kv) { return compTail(new TailCont() {
        Code with(final Tail t) {
            Var v = new Temp();
            return new Bind(v, t, kv.with(v));
        }
    });
  }

    /** Compile an expression into a tail that is passed as an argument
     *  to the specified continuation.
     */
    public abstract Code compTail(final TailCont kt);

    /** Compilation rule for a unary operator; calculate the value of the
     *  argument (in a variable) and then call the appropriate primitive.
     */
    protected Code unary(final Prim op, final TailCont kt) {
        return this.compVar(new VarCont() {
            Code with(final Var v) {
                return kt.with(new PrimCall(op, v));
            }
        });
    }

    /** Compilation rule for a binary operator; calculate the values of the
     *  two arguments (in variables) and then call the appropriate primitive.
     */
    protected Code binary(final Prim op, final Expr that, final TailCont kt) {
        return this.compVar(new VarCont() {
            Code with(final Var l) {
                return that.compVar(new VarCont() {
                    Code with(final Var r) {
                        return kt.with(new PrimCall(op, l, r));
                    }
                });
            }
        });
    }

    /** Compilation rule to find the address of the idx'th element of this
     *  (array) expression.
     */
    protected Code elemAddress(final Expr idx, final VarCont kv) {
        return this.compVar(new VarCont() {
            Code with(final Var a) {
                return idx.compVar(new VarCont() {
                    Code with(final Var i) {
                        Var o = new Temp();
                        Var m = new Temp();
                        return new Bind(o, new PrimCall(Prim.mul, new Const(4), i),
                               new Bind(m, new PrimCall(Prim.add, a, o),
                               kv.with(m)));
                    }
                });
            }
        });
     }
}
