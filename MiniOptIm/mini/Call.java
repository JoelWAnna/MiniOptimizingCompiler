package mini;

import compiler.Failure;
import mil.*;

/** Represents a function call.
 */
class Call extends Expr {

    /** The name of the function to call.
     */
    private Id name;

    /** The arguments for this call.
     */
    private Args args;

    /** Default constructor.
     */
    Call(Id name, Args args) {
        this.name = name;
        this.args = args;
    }

    /** Print an indented description of this abstract syntax node,
     *  including a name for the node itself at the specified level
     *  of indentation, plus more deeply indented descriptions of
     *  any child nodes.
     */
    public void indent(IndentOutput out, int n) {
        out.indent(n, "Call " + name + "(..)");
        for (Args as=args; as!=null; as=as.getNext()) {
           as.getArg().indent(out, n+1);
        }
    }

    /** Output a description of this node (with id n), returning the
     *  next available node id after this node and all of its children
     *  have been output.
     */
    public int toDot(DotOutput dot, int n) {
        int c = dot.node("Call " + name + "(..)", n);
        for (Args as=args; as!=null; as=as.getNext()) {
           c = as.getArg().toDot(dot, n, c);
        }
        return c;
    }

    /** Record the function that is being invoked by this call.
     */
    private Fundef def = null;

    /** Return the type of value that will be produced when this
     *  expression is evaluated.
     */
    public Type typeOf(Context ctxt, VarEnv env)
      throws Failure {
        Type result = typeCall(ctxt, env);
        if (result==null) {
            throw new Failure("Function " + name + " does not return a value");
        }
        return result;
    }

    /** Type check a function call, returning either the return type
     *  or null if this is a void function.
     */
    Type typeCall(Context ctxt, VarEnv env)
      throws Failure {
        // Look for the definition of this function
        def = ctxt.find(name);
        if (def==null) {
            throw new Failure("Cannot find function \"" + name + "\"");
        }
        return def.checkCall(args, ctxt, env);
    }

    /** Type check this expression in places where it is used as a statement.
     *  We override this method in Call to deal with methods that
     *  return void.
     */
    void checkExpr(Context ctxt, VarEnv env)
      throws Failure {
        typeCall(ctxt, env);
    }

    /** Generate code for a function call.
     */
    Code compileCall(final Args args, final Atom[] milargs, final int i, final TailCont kt) {
        if (args==null) {
            return kt.with(def.makeCall(milargs));
        } else {
            return args.getArg().compVar(new VarCont() {
                       Code with(final Var v) {
                           milargs[i] = v;
                           return compileCall(args.getNext(), milargs, i+1, kt);
                       }
                   });
        }
    }

    /** Compile an expression into a tail that is passed as an argument
     *  to the specified continuation.
     */
    public Code compTail(final TailCont kt) { // f(a1,...,an)
        return compileCall(args, new Atom[Args.length(args)], 0, kt);
    }
}
