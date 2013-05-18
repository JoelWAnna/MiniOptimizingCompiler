package mini;
import compiler.Failure;
import mil.*;

/** Represents a variable environment, mapping identifiers
 *  to corresponding types.
 */
public class VarEnv {

    /** The identifier that is described by this environment
     *  entry.
     */
    private Id id;

    /** The type for the identifier in this environment entry.
     */
    private Type type;

    /** The corresponding variable in the generated MIL code.
     */
    private Var var;

    /** A pointer to the remaining environment entries.
     */
    private VarEnv next;

    /** Default constructor.
     */
    public VarEnv(Id id, Type type, Var var, VarEnv next) {
        this.id = id;
        this.type = type;
        this.var = var;
        this.next = next;
    }

    /** Return the type associated with this environment.
     */
    public Type getType() {
        return type;
    }

    /** Return the variable associated with this environment.
     */
    public Var getVar() {
        return var;
    }

    /** Search an environment for a specified variable name, returning
     *  null if no such entry is found, or else returning a pointer to
     *  the first matching VarEnv object in the list.
     */
    public static VarEnv find(Id id, VarEnv env) {
        while (env!=null && !env.id.equals(id)) {
            env = env.next;
        }
        return env;
    }
}
