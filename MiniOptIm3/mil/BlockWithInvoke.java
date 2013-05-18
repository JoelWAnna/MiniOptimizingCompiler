package mil;

/** Represents a block that was derived by adding a trailing invoke to
 *  its code sequence.
 */
public class BlockWithInvoke extends Block {

    /** Default constructor.
     */
    public BlockWithInvoke(Code code) {
        super(code);
    }
}
