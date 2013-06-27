package mil;

public class Sets {

    public Set head;

    public Sets next;

    /** Default constructor.
     */
    public Sets(Set head, Sets next) {
        this.head = head;
        this.next = next;
    }

    public void print() {
        int i = 0;
        print(i);
}

    public void print(int i) {
        System.out.println("Set index :" + i);
        head.a.print();
        if (next != null)
                next.print(i+1);
        }
}
