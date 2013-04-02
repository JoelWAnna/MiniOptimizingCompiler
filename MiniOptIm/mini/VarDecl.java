package mini;

import compiler.Failure;
import mil.*;

/** A representation for variable declarations.
 */
public class VarDecl extends Stmt {

    /** The type of all of the variables that are introduced in this
     *  declaration.
     */
    private Type type;

    /** A list of variable introductions, each of which names a variable,
     *  together with a possible initializer.
     */
    private VarIntro[] intros;

    /** Records the number of slots in the intros array that are in use.
     */
    private int used;

    /** Construct a variable declaration with a given type and
     *  a specific variable introduction.
     */
    public VarDecl(Type type, VarIntro intro) {
        this.type   = type;
        this.intros = new VarIntro[] { intro };
        this.used   = 1;
    }

    /** Extend this variable declaration with an additional variable
     *  introduction.
     */
    public VarDecl addIntro(VarIntro intro) {
        if (used>=intros.length) {
            VarIntro[] newIntros = new VarIntro[2*intros.length];
            for (int i=0; i<intros.length; i++) {
                newIntros[i] = intros[i];
            }
            intros = newIntros;
        }
        intros[used++] = intro;
        return this;
    }

    /** Print an indented description of this abstract syntax node,
     *  including a name for the node itself at the specified level
     *  of indentation, plus more deeply indented descriptions of
     *  any child nodes.
     */
    public void indent(IndentOutput out, int n) {
        out.indent(n, "VarDecl");
        out.indent(n+1, type.toString());
        for (int i=0; i<intros.length; i++) {
           intros[i].indent(out, n+1);
        }
    }

    /** Output a description of this node (with id n), including a
     *  link to its parent node (with id p) and returning the next
     *  available node id.
     */
    public int toDot(DotOutput dot, int p, int n) {
        dot.join(p,n);
        p = n;
        n = dot.node("VarDecl: " + type, n);
        for (int i=0; i<intros.length; i++) {
            n = intros[i].toDot(dot, p, n);
        }
        return n;
    }

    /** Output a description of this node (with id n), returning the
     *  next available node id after this node and all of its children
     *  have been output.
     */
    public int toDot(DotOutput dot, int n) {
        return toDot(dot, n, dot.node("VarDecl "+type, n));
    }

    /** Check that this statement is valid, taking the current environment
     *  as an argument and returning a possibly modified environment as a
     *  result.
     */
    public VarEnv check(Fundef def, Context ctxt, boolean inLoop, VarEnv env)
      throws Failure {
        for (int i=0; i<used; i++) {
            env = intros[i].check(def, type, ctxt, env);
        }
        def.returns = false;
        return env;
    }

    /** Generate code that will execute this statement and then continue
     *  with the code specified by the andThen argument.
     */
    Code compStmt(final Code andThen, final mil.Block breakBlock, final mil.Block contBlock) {   // type intros;
        Code code = andThen;  // can't use andThen because it is final
        for (int i=used; i>0; ) {  // run backwards through intros
           code = intros[--i].compStmt(code, null, null);
        }
        return code;
    }
}
