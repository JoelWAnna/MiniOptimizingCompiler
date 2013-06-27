package mini;
import compiler.Failure;
import mil.*;

/** Represents a list of function definitions.
 */
class Fundefs {

    /** The first function definition in this list.
     */
    private Fundef def;

    /** A pointer to the remaining function definitions in this list.
     */
    private Fundefs next;

    /** Default constructor.
     */
    Fundefs(Fundef def, Fundefs next) {
        this.def = def;
        this.next = next;
    }

    /** Print an indented description of this program.
     */
    public static void indent(Fundefs defs) {
        IndentOutput out = new IndentOutput(System.out);
        for (; defs!=null; defs=defs.next) {
            defs.def.indent(out, 0);
        }
      }

    /** Output a description of the structure of this program in the
     *  dot format that is used by the AT&T GraphViz tools.
     */
    public static void toDot(Fundefs defs, DotOutput dot, int n) {
        for (; defs!=null; defs=defs.next) {
            n = defs.def.toDot(dot, n);
        }
    }

    /** Search for the definition of a function with a particular
     *  name in a given list of function definitions, returning a
     *  null result if no matching function is found.
     */
    static Fundef find(Id fun, Fundefs defs) {
        for (; defs!=null; defs=defs.next) {
            if (defs.def.isCalled(fun)) {
                return defs.def;
            }
        }
        return null;
    }

    /** Check the definitions for each function in a given list
     *  of function definitions and return a pointer to the main
     *  function.
     */
    static Fundef check(Context ctxt, Fundefs defs)
      throws Failure {
        Id     mainId  = new Id(Assembly.entry);
        Fundef mainFun = Fundefs.find(mainId, defs);
        if (mainFun==null) {
            throw new Failure("program does not define a " + mainId + " function");
        }
        if (mainFun.getFormals()!=null) {
            throw new Failure(mainId + " function should not have any arguments");
        }
        for (; defs!=null; defs=defs.next) {
            Fundef def = defs.def;
            Id     fun = def.getFun();
            if (find(fun, defs.next)!=null) {
                throw new Failure("program contains multiple definitions for function " + fun);
            }
            defs.def.check(ctxt);
        }
        return mainFun;
    }

    /** Generate code for a list of functions.
     */
    static void compile(Fundefs defs) {
        // Generate MIL code for each function definition.
        for (; defs!=null; defs=defs.next) {
            defs.def.compile();
        }
    }
}
