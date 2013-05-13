package mil;

public class Pairs {

    public Pair head;

    public Pairs next;

    /** Default constructor.
     */
    public Pairs(Pair head, Pairs next) {
        this.head = head;
        this.next = next;
    }

    /** Return the length of a linked list of elements.
     */
    public static int length(Pairs list) {
        int len = 0;
        for (; list!=null; list=list.next) {
            len++;
        }
        return len;
    }

    public void print(boolean inset, String id) {
        if (inset) {
                System.out.println("Inset for block " + id);
        }
        else {
                System.out.println("Outset for block " + id);
        }
        print();
}

    public void print() {
        head.t.display();
        System.out.print("->");
        Atoms current = head.atoms;
        System.out.print(current.head.toString());
        current = current.next;
        while (current != null) {
                System.out.print("," + current.head.toString());
                current = current.next;
                }
        System.out.println("");
        if (next != null)
                next.print();
}

    public Pairs copy() {
        if (next != null)
                return new Pairs(head.copy(), next.copy());     
        return new Pairs(head.copy(), next);            
}

    public Pairs kill(Atom a) {
        Var v = a.isVar();
        if (v == null)
                return this;
        if (head.t.contains(v)) {
                if (next != null)
                        return next.kill(v);
                return null;
        }
        if (next != null)
                return new Pairs(head, next.kill(v));
        return new Pairs(head, null);
}

    public Pairs gen(Pair p) {
        if (head.merge(p)) {
                return this;
        }
        if (next != null) {
                return new Pairs(head, next.gen(p));
        }
        return new Pairs(p, null);
}

    public static Pairs meets(Pairs toMeetA, Pairs toMeetB, boolean union) {
        if (toMeetA == null) {
                if (toMeetB == null || !union) {
                        return null;
                }
                return toMeetB.copy();
        }
        if (toMeetB == null) {
                if (toMeetA == null || !union) {
                        return null;
                }
                return toMeetA.copy();
        }
        // toMeetA != null && toMeetB != null
        Pairs met = null;

        Pairs toMeetAHead = toMeetA = toMeetA.copy();
        Pairs toMeetBHead = toMeetB = toMeetB.copy();
        
        
        Pairs previousA = null;
        Pairs previousB = null;

        while (toMeetA != null)
        {
                toMeetB = toMeetBHead;
                previousB = null;
                boolean found = false;
                while (toMeetB != null)
                {
                        if (toMeetA.head.equal(toMeetB.head)) {
                                        Pair toInsert = toMeetA.head.copy();
                                        toInsert.merge(toMeetB.head);
                                        met = new Pairs(toInsert, met);
                                        found = true;
                                        if (previousA == null) {
                                                toMeetAHead = toMeetA.next;
                                        }
                                        else {
                                                previousA.next = toMeetA.next;
                                        }
                                        if (previousB == null) {
                                                toMeetBHead = toMeetB.next;
                                        }
                                        else {
                                                previousB.next = toMeetB.next;
                                        }
                                        break;
                        }
                        else {
                                previousB = toMeetB;
                        }
                        toMeetB = toMeetB.next;
                }
                if (!found) {
                        if (union) {
                                met = new Pairs(toMeetA.head.copy(), met);
                        }
                        previousA = toMeetA;
                }
                toMeetA = toMeetA.next;
        }

        if (union) {
                while (toMeetAHead != null) {
                        met = new Pairs(toMeetAHead.head.copy(), met);
                        toMeetAHead = toMeetAHead.next;
                }
                while (toMeetBHead != null) {
                        met = new Pairs(toMeetBHead.head.copy(), met);
                        toMeetBHead = toMeetBHead.next;
                }
        }
        return met;
}
}
