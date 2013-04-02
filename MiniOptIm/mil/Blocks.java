package mil;

public class Blocks {
    public Block head;
    public Blocks next;

    /** Default constructor.
     */
    public Blocks(Block head, Blocks next) {
        this.head = head;
        this.next = next;
    }

    /** Test for membership in a list.
     */
    public static boolean isIn(Block val, Blocks list) {
        for (; list!=null; list=list.next) {
            if (list.head==val) {
                return true;
            }
        }
        return false;
    }
}
