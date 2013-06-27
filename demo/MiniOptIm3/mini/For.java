package mini;
import compiler.Failure;
import mil.*;

/** Abstract syntax for "for" loops.
 */
class For extends Stmt {

    /** The initializer for the loop.
     */
    private ForInit init;

    /** The test expression for the loop (or null if there
     *  is no test expression).
     */
    private Expr test;

    /** The step expression for the loop (or null if there
     *  is no step expression).
     */
    private Expr step;

    /** The body of the loop.
     */
    private Stmt body;

    /** Default constructor.
     */
    For(ForInit init, Expr test, Expr step, Stmt body) {
        this.init = init;
        this.test = test;
        this.step = step;
        this.body = body;
    }

    /** Print an indented description of this abstract syntax node,
     *  including a name for the node itself at the specified level
     *  of indentation, plus more deeply indented descriptions of
     *  any child nodes.
     */
    public void indent(IndentOutput out, int n) {
        out.indent(n, "For");
        init.indent(out, n+1);
        if (test==null) {
            out.indent(n+1, "No test");
        } else {
            test.indent(out, n+1);
        }
        if (step==null) {
            out.indent(n+1, "No step");
        } else {
            step.indent(out, n+1);
        }
        body.indent(out, n+1);
    }

    /** Output a description of this node (with id n), returning the
     *  next available node id after this node and all of its children
     *  have been output.
     */
    public int toDot(DotOutput dot, int n) {
        int c = init.toDot(dot, n, dot.node("For", n));
        if (test==null) {
            dot.join(n, c);
            c = dot.node("No test", c);
        } else {
            c = test.toDot(dot, n, c);
        }

        if (step==null) {
            dot.join(n, c);
            c = dot.node("No step", c);
        } else {
            c = step.toDot(dot, n, c);
        }

        return body.toDot(dot, n, c);
    }

    /** Check that this statement is valid, taking the current environment
     *  as an argument and returning a possibly modified environment as a
     *  result.
     */
    public VarEnv check(Fundef def, Context ctxt, boolean inLoop, VarEnv env)
      throws Failure {
        // Find the environment that is produced by the initializer
        VarEnv ienv = init.check(def, ctxt, env);
  
        // Check that the test is a Boolean
        try {
            if (test!=null && !test.typeOf(ctxt, ienv).equal(Type.BOOLEAN)) {
                ctxt.report(new Failure("Boolean test expected in while loop"));
            }
        } catch (Failure f) {
            // report any error that occured while checking the expression.
            ctxt.report(f);
        }
  
        // Check that the step is a valid expression
        if (step!=null) {
            step.typeOf(ctxt, ienv);
        }
  
        // check body, but discard any modified environment that it produces.
        body.check(def, ctxt, true, ienv);
  
        def.returns = false;
        return env;
    }

    /** Generate code that will execute this statement and then continue
     *  with the code specified by the andThen argument.
     */
    Code compStmt(final Code andThen, final mil.Block breakBlock, final mil.Block contBlock) {      // FOR (init; test; step) body
        final mil.Block btest = new mil.Block();
        final mil.Block bdone = new mil.Block(andThen);
        final mil.Block bstep = (step==null)
                              ? btest
                              : new mil.Block(step.compTail(new TailCont() {
                                    Code with(Tail t) {
                                        return new Bind(Wildcard.obj, t, btest.toCode());
                                    }
                                }));
        final mil.Code  bcode = body.compStmt(bstep.toCode(), bdone, bstep);
        btest.setCode((test==null)
                     ? bcode
                     : test.compVar(new VarCont() {
                           Code with(final Var tv) {
                               return ifthenelse(tv, bcode, bdone.toCode());
                           }
                       }));
        return init.compInit(btest.toCode());
    }
}
