package mini;

import compiler.Failure;
import mil.*;

/** Represents a single function definition.
 */
class Fundef {

    /** The return type for this function definition.
     *  (A null return type signals a void function).
     */
    private Type type;

    /** The name of the function that is defined here.
     */
    private Id fun;

    /** The list of formal parameters for this function definition.
     */
    private Formals formals;

    /** The list of statements that make up the body of this function.
     */
    private Stmts body;

    /** Default constructor.
     */
    Fundef(Type type, Id fun, Formals formals, Stmts body) {
        this.type = type;
        this.fun = fun;
        this.formals = formals;
        this.body = body;
    }

    /** Return the name of this function definition.
     */
    Id getFun() {
        return fun;
    }

    /** Return the list of formal parameters associated with this
     *  function definition.
     */
    Formals getFormals() {
        return formals;
    }

    /** Print an indented description of this abstract syntax node,
     *  including a name for the node itself at the specified level
     *  of indentation, plus more deeply indented descriptions of
     *  any child nodes.
     */
    public void indent(IndentOutput out, int n) {
        out.indent(n, "Fundef "
                    + (type==null ? "void" : type.toString())
                    + " " + fun + "(..)");
        out.indent(n+1, "Formals");
        for (Formals fs=formals; fs!=null; fs=fs.getNext()) {
          fs.getFormal().indent(out, n+2);
        }
        out.indent(n+1, "Stmts");
        body.indent(out, n+2);
    }

    /** Output a description of this node (with id n), returning the
     *  next available node id after this node and all of its children
     *  have been output.
     */
    public int toDot(DotOutput dot, int n) {
        int f = dot.root((type==null ? "void" : type.toString())
                            + " " + fun + "(..)", n);
        dot.join(n, f);
        int c = dot.node("Formals", f);
        for (Formals fs=formals; fs!=null; fs=fs.getNext()) {
           dot.join(f, c);
           c = fs.getFormal().toDot(dot, c);
        }
        return body.toDot(dot, n, c);
      }

    /** Test to see if this function's name matches the
     *  specified identifier.
     */
    boolean isCalled(Id id) {
        return fun.equals(id);
    }

    /** Type check a call to this function with a given list
     *  of arguments.
     */
    Type checkCall(Args args, Context ctxt, VarEnv env)
      throws Failure {
        Formals fs = formals;
        while (fs!=null && args!=null) {
            Type argType = args.getArg().typeOf(ctxt, env);
            Formal f     = fs.getFormal();
            if (!argType.equal(f.getType())) {
                throw new Failure("wrong type for argument "
                                    + f.getName());
            }
            fs   = fs.getNext();
            args = args.getNext();
        }
        if (args!=null) {
            throw new Failure("Call to function " + fun
                              + " has too many arguments");
        } else if (fs!=null) {
            throw new Failure("Call to function " + fun
                              + " has too few arguments");
        }
        return type;
    }

    /** Flag to indicate if this function returns.
     */
    public boolean returns = false;

    /** Determine whether the specified expression is valid as a
     *  return result for this function; the expression can/should
     *  be null for a void function.
     */
    VarEnv checkReturn(Expr expr, Context ctxt, VarEnv env)
      throws Failure {
        if (expr!=null) {
            if (type==null) {
                throw new Failure("void function should not return a value");
            } else if (!expr.typeOf(ctxt, env).equal(type)) {
                throw new Failure("return expression has wrong type");
            }
        } else if (type!=null) {
            throw new Failure("return value is required");
        }
        returns = true;
        return env;
    }

    /** Check this function definition in the given context.
     */
    void check(Context ctxt)
      throws Failure {
        if (body!=null) {
            body.check(this, ctxt, false, Formals.buildEnv(formals));
            if (!returns && type!=null) {
                throw new Failure("function does not return");
            }
        }
    }

    /** The entry point to each function is represented by a block in MIL.
     */
    private mil.Block entry = new mil.Block();

    /** Code sequence representing the return from a void function.
     *  TODO: it would be good to eliminate even the 0 here ...
     */
    public static final Code retvoid = new Done(new mil.Return(Wildcard.obj));

    /** Compile the body of this function in to MIL code.
     */
    void compile() {
        // Count the number of formal parameters:
        Var[] vs = new Var[Formals.length(formals)];
        int   i  = 0;
        for (Formals fs=formals; fs!=null; fs=fs.getNext()) {
            vs[i++] = fs.getFormal().getParam();
        }
        entry.setFormals(vs);
  
        // Compile the body:
        if (body!=null) {
            System.out.println("Function " + fun + " implemented by " + entry.getId());
            entry.setCode(body.compStmt(retvoid, null, null));
        } else {
            // TODO: figure out if this is an appropriate way to handle extern functions
            entry.setCode(retvoid);
        }
    }

    /** Construct a MIL program with this function as its entry point.
     */
    MILProgram entryToMIL() {
        MILProgram prog = new MILProgram();
        prog.addEntry(entry);
        // Initial reachability test
        prog.shake();
        // Perform a data flow analysis to compute the correct set of
        // parameters for every block.
        prog.computeLiveVars();
        // Rewrite each of the blocks and any trailing calls (i.e., gotos)
        // to use the newly computed argument lists.
        prog.fixTrailingBlockCalls();
        return prog;
    }

    /** Construct a call to the function defined here with a
     *  given set of arguments.
     */
    Tail makeCall(Atom[] args) {
        return new BlockCall(entry).withArgs(args);
     }
}
