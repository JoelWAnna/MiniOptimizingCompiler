package mini;
import compiler.Failure;
import mil.*;

/** Abstract syntax for print statements.
 */
class Print extends Stmt {

    /** The value that should be printed out.
     */
    private Expr exp;

    /** Default constructor.
     */
    Print(Expr exp) {
        this.exp = exp;
    }

    /** Print an indented description of this abstract syntax node,
     *  including a name for the node itself at the specified level
     *  of indentation, plus more deeply indented descriptions of
     *  any child nodes.
     */
    public void indent(IndentOutput out, int n) {
        out.indent(n, "Print");
        exp.indent(out, n+1);
    }

    /** Output a description of this node (with id n), returning the
     *  next available node id after this node and all of its children
     *  have been output.
     */
    public int toDot(DotOutput dot, int n) {
        return exp.toDot(dot, n,
               dot.node("Print", n));
    }

    /** Check that this statement is valid, taking the current environment
     *  as an argument and returning a possibly modified environment as a
     *  result.
     */
    public VarEnv check(Fundef def, Context ctxt, boolean inLoop, VarEnv env)
      throws Failure {
        try {
            if (!exp.typeOf(ctxt, env).equal(Type.INT)) {
                ctxt.report(new Failure("print requires integer argument"));
            }
        } catch (Failure f) {
            // report any error that occured while checking the expression.
            ctxt.report(f);
        }
        def.returns = false;
        return env;
    }

    /** Generate code that will execute this statement and then continue
     *  with the code specified by the andThen argument.
     */
    Code compStmt(final Code andThen, final mil.Block breakBlock, final mil.Block contBlock) {     // PRINT exp
        return exp.compVar(new VarCont() {
            Code with(final Var pv)  {
                return new Bind(Wildcard.obj, new PrimCall(Prim.print, pv),
                       andThen);
            }
        });
    }
}
