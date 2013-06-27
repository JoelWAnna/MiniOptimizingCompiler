package mini;
import compiler.Failure;
import mil.*;

/** Abstract syntax for if-then-else statements.
 */
class If extends Stmt {

    /** The test expression.
     */
    private Expr test;

    /** The true branch.
     */
    private Stmt ifTrue;

    /** The false branch.
     */
    private Stmt ifFalse;

    /** Default constructor.
     */
    If(Expr test, Stmt ifTrue, Stmt ifFalse) {
        this.test = test;
        this.ifTrue = ifTrue;
        this.ifFalse = ifFalse;
    }

    /** Print an indented description of this abstract syntax node,
     *  including a name for the node itself at the specified level
     *  of indentation, plus more deeply indented descriptions of
     *  any child nodes.
     */
    public void indent(IndentOutput out, int n) {
        out.indent(n, "If");
        test.indent(out, n+1);
        ifTrue.indent(out, n+1);
        ifFalse.indent(out, n+1);
    }

    /** Output a description of this node (with id n), returning the
     *  next available node id after this node and all of its children
     *  have been output.
     */
    public int toDot(DotOutput dot, int n) {
        return ifFalse.toDot(dot, n,
               ifTrue.toDot(dot, n,
               test.toDot(dot, n,
               dot.node("If", n))));
    }

    /** Check that this statement is valid, taking the current environment
     *  as an argument and returning a possibly modified environment as a
     *  result.
     */
    public VarEnv check(Fundef def, Context ctxt, boolean inLoop, VarEnv env)
      throws Failure {
        try {
            if (!test.typeOf(ctxt, env).equal(Type.BOOLEAN)) {
                ctxt.report(new Failure("Boolean test expected in if statement"));
            }
        } catch (Failure f) {
            // report any error that occured while checking the expression.
            ctxt.report(f);
        }
        // check true and false branches, discarding resulting environments:
        ifTrue.check(def, ctxt, inLoop, env);
        boolean tr = def.returns;
        ifFalse.check(def, ctxt, inLoop, env);
        def.returns = tr && def.returns;
        return env;
    }

    /** Generate code that will execute this statement and then continue
     *  with the code specified by the andThen argument.
     */
    Code compStmt(final Code andThen, final mil.Block breakBlock, final mil.Block contBlock) {        // IF test THEN ifTrue ELSE ifFalse
        return test.compVar(new VarCont() {
            Code with(final Var tv) {
                mil.Block join = new mil.Block(andThen);
                return ifthenelse(tv, ifTrue .compStmt(join.toCode(), breakBlock, contBlock),
                                      ifFalse.compStmt(join.toCode(), breakBlock, contBlock));
            }
        });
  
    }
}
