package mil;

public class BlockCalls {

    public BlockCall head;

    public BlockCalls next;

    /** Default constructor.
     */
    public BlockCalls(BlockCall head, BlockCalls next) {
        this.head = head;
        this.next = next;
    }
}
