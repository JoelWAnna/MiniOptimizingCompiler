package mini;
import compiler.Failure;
import mil.*;

/** Represents a list of expressions passed as the arguments to a
 *  function call.
 */
class Args {

    /** Holds this argument expression.
     */
    private Expr arg;

    /** Points to the list containing the rest of the arguments.
     */
    private Args next;

    /** Default constructor.
     */
    Args(Expr arg, Args next) {
        this.arg = arg;
        this.next = next;
    }

    /** Return the argument at the front of this list.
     */
    public Expr getArg() {
        return arg;
    }

    /** Return a list containing the rest of the function's arguments.
     */
    public Args getNext() {
        return next;
    }

    /** Return the length of a linked list of elements.
     */
    public static int length(Args list) {
        int len = 0;
        for (; list!=null; list=list.next) {
            len++;
        }
        return len;
    }
}
