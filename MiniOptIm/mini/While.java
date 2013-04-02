package mini;

import compiler.Failure;
import mil.*;

/** Abstract syntax for while statements.
 */
class While extends Stmt {

    /** The test expression.
     */
    private Expr test;

    /** The body of this loop.
     */
    private Stmt body;

    /** Default constructor.
     */
    While(Expr test, Stmt body) {
        this.test = test;
        this.body = body;
    }

    /** Print an indented description of this abstract syntax node,
     *  including a name for the node itself at the specified level
     *  of indentation, plus more deeply indented descriptions of
     *  any child nodes.
     */
    public void indent(IndentOutput out, int n) {
        out.indent(n, "While");
        test.indent(out, n+1);
        body.indent(out, n+1);
    }

    /** Output a description of this node (with id n), returning the
     *  next available node id after this node and all of its children
     *  have been output.
     */
    public int toDot(DotOutput dot, int n) {
        return body.toDot(dot, n,
               test.toDot(dot, n,
               dot.node("While", n)));
    }

    /** Check that this statement is valid, taking the current environment
     *  as an argument and returning a possibly modified environment as a
     *  result.
     */
    public VarEnv check(Fundef def, Context ctxt, boolean inLoop, VarEnv env)
      throws Failure {
        try {
            if (!test.typeOf(ctxt, env).equal(Type.BOOLEAN)) {
                ctxt.report(new Failure("Boolean test expected in while loop"));
            }
        } catch (Failure f) {
            // report any error that occured while checking the expression.
            ctxt.report(f);
        }
        // check body, but discard any modified environment that it produces.
        body.check(def, ctxt, true, env);
        def.returns = false;
        return env;
    }

    /** Generate code that will execute this statement and then continue
     *  with the code specified by the andThen argument.
     */
    Code compStmt(final Code andThen, final mil.Block breakBlock, final mil.Block contBlock) {     // WHILE (test) body
        final mil.Block bstart = new mil.Block();
        final mil.Block bdone  = new mil.Block(andThen);
        Code            start  = test.compVar(new VarCont() {
            Code with(final Var tv) {
                return ifthenelse(tv,
                                  body.compStmt(bstart.toCode(), bdone, bstart),
                                  andThen);
            }
        });
        bstart.setCode(start);
        return bstart.toCode();
    }
}
