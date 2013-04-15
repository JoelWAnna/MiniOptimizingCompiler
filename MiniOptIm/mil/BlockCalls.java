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

    /** Test for membership in a list.
     */
    public static boolean isIn(BlockCall val, BlockCalls list) {
        for (; list!=null; list=list.next) {
            if (list.head==val) {
                return true;
            }
        }
        return false;
    }

    /** Return the length of a linked list of elements.
     */
    public static int length(BlockCalls list) {
        int len = 0;
        for (; list!=null; list=list.next) {
            len++;
        }
        return len;
    }
}
