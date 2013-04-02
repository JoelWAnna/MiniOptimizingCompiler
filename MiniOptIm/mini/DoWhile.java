package mini;

import compiler.Failure;
import mil.*;

/** Abstract syntax for do while statements.
 */
class DoWhile extends Stmt {

    /** The body of this loop.
     */
    private Stmt body;

    /** The test expression.
     */
    private Expr test;

    /** Default constructor.
     */
    DoWhile(Stmt body, Expr test) {
        this.body = body;
        this.test = test;
    }

    /** Print an indented description of this abstract syntax node,
     *  including a name for the node itself at the specified level
     *  of indentation, plus more deeply indented descriptions of
     *  any child nodes.
     */
    public void indent(IndentOutput out, int n) {
        out.indent(n, "DoWhile");
        body.indent(out, n+1);
        test.indent(out, n+1);
    }

    /** Output a description of this node (with id n), returning the
     *  next available node id after this node and all of its children
     *  have been output.
     */
    public int toDot(DotOutput dot, int n) {
        return test.toDot(dot, n,
               body.toDot(dot, n,
               dot.node("DoWhile", n)));
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
        // if the body returns (and sets def.returns.true), then so does the do while
        return env;
    }

    /** Generate code that will execute this statement and then continue
     *  with the code specified by the andThen argument.
     */
    Code compStmt(final Code andThen, final mil.Block breakBlock, final mil.Block contBlock) {   // DO body WHILE (test);
        final mil.Block btest  = new mil.Block();
        final mil.Block bdone  = new mil.Block(andThen);
        final mil.Block bstart = new mil.Block(body.compStmt(btest.toCode(), bdone, btest));
        btest.setCode(test.compVar(new VarCont() {
            Code with(final Var tv) {
                return ifthenelse(tv, bstart.toCode(), bdone.toCode());
            }
        }));
        return bstart.toCode();
    }
}
