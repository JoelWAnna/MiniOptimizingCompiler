package mil;

public class DefnSCCs {
    public DefnSCC head;
    public DefnSCCs next;

    /** Default constructor.
     */
    public DefnSCCs(DefnSCC head, DefnSCCs next) {
        this.head = head;
        this.next = next;
    }
}
