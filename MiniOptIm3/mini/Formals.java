package mini;
import compiler.Failure;
import mil.*;

/** Represents a list of formal parameters in a function definition.
 */
class Formals {

    /** Holds the first formal parameter in this list.
     */
    private Formal formal;

    /** Holds the list containing the remaining formal parameters.
     */
    private Formals next;

    /** Default constructor.
     */
    Formals(Formal formal, Formals next) {
        this.formal = formal;
        this.next = next;
    }

    /** Return the formal argument at the front of this list.
     */
    public Formal getFormal() {
        return formal;
    }

    /** Return a list containing the rest of the formal parameters.
     */
    public Formals getNext() {
        return next;
    }

    /** Build an environment that corresponds to a list of formal
     *  parameters.
     */
    static VarEnv buildEnv(Formals formals) {
        if (formals==null) {
            return null;
        } else {
            return formals.formal.extendEnv(buildEnv(formals.next));
        }
    }

    /** Return the length of a linked list of elements.
     */
    public static int length(Formals list) {
        int len = 0;
        for (; list!=null; list=list.next) {
            len++;
        }
        return len;
    }
}
