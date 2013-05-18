package mil;

/** Represents a block that was derived by adding a trailing invocation
 *  of a new continuation argument to its code sequence.
 */
public class BlockWithCont extends Block {

    /** Default constructor.
     */
    public BlockWithCont(Code code) {
        super(code);
    }
}
