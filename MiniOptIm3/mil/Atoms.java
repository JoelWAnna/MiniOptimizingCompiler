package mil;

public class Atoms {

    public Atom head;

    public Atoms next;

    /** Default constructor.
     */
    public Atoms(Atom head, Atoms next) {
        this.head = head;
        this.next = next;
    }

    public Atoms copy() {
        if (next != null)
                return new Atoms(head, next.copy());
        return new Atoms(head, next);
}
}
