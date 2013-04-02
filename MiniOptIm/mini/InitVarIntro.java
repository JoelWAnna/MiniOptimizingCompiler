package mini;

import compiler.Failure;
import mil.*;

/** Represents a variable introduction with a single variable
 *  name and an initializer.
 */
public class InitVarIntro extends VarIntro {

    /** An initializer for the variable introduced here.
     */
    private Expr init;

    /** Default constructor.
     */
    public InitVarIntro(Id id, Expr init) {
        super(id);
        this.init = init;
    }

    /** Print an indented description of this abstract syntax node,
     *  including a name for the node itself at the specified level
     *  of indentation, plus more deeply indented descriptions of
     *  any child nodes.
     */
    public void indent(IndentOutput out, int n) {
        out.indent(n, "InitVarIntro");
        id.indent(out, n+1);
        init.indent(out, n+1);
    }

    /** Output a description of this node (with id n), returning the
     *  next available node id after this node and all of its children
     *  have been output.
     */
    public int toDot(DotOutput dot, int n) {
        return init.toDot(dot, n,
               dot.node("InitVarIntro: " + id, n));
    }

    /** Check this variable introduction, given the type for
     *  the associated variable declaration and the initial
     *  environment, and then return the modified environment.
     */
    public VarEnv check(Fundef def, Type type, Context ctxt, VarEnv env) {
        // Check that initializer has correct type:
        try {
            if (!init.typeOf(ctxt, env).equal(type)) {
                ctxt.report(new Failure("initializer has wrong type"));
            }
        } catch (Failure f) {
            ctxt.report(f);
        }
        return ve = new VarEnv(id, type, new LocalTemp(id.toString()), env);
    }

    /** Generate code that will execute this statement and then continue
     *  with the code specified by the andThen argument.
     */
    Code compStmt(final Code andThen, final mil.Block breakBlock, final mil.Block contBlock) {  // var = init
        return init.compTail(new TailCont() {
            Code with(final Tail t)  {
                return new Bind(ve.getVar(), t, andThen);
            }
        });
    }
}
