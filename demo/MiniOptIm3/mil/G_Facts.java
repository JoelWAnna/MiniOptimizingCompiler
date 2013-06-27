package mil;

public class G_Facts {

    public G_Fact head;

    public G_Facts next;

    /** Default constructor.
     */
    public G_Facts(G_Fact head, G_Facts next) {
        this.head = head;
        this.next = next;
    }

    /** Return the length of a linked list of elements.
     */
    public static int length(G_Facts list) {
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
        System.out.println("");
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

    public G_Facts copyWithSubst(Atom[] args, Atom[] formals) {
        if (args == null || formals == null) return this;
        G_Facts copy = new G_Facts(head.copyWithSubst(args, formals), null);
        if (next != null)
                copy.next = next.copyWithSubst(args, formals);  
        return copy;            
}

    public G_Facts copy() {
        if (next != null)
                return new G_Facts(head.copy(), next.copy());   
        return new G_Facts(head.copy(), next);          
}

    public G_Facts kill(Atom a) {
        Var v = a.isVar();
        if (v == null)
                return this;
        if (head.t.contains(v)) {
                if (next != null)
                        return next.kill(v);
                return null;
        }
        if (next != null)
                return new G_Facts(head, next.kill(v));
        return new G_Facts(head, null);
}

    public G_Facts gen(G_Fact p) {
        if (head.merge(p)) {
                return this;
        }
        if (next != null) {
                return new G_Facts(head, next.gen(p));
        }
        next = new G_Facts(p, null);
        return this;
}

    public static G_Facts meets(Sets toMeet, boolean mode) {
        G_Facts met = null;
        boolean firstRound = true;
        // The interpretation of !mode is intersection
        Sets setsIter;
        for (setsIter = toMeet; setsIter != null; setsIter = setsIter.next) {
                G_Facts setA =  setsIter.head.a;
                
                if (firstRound) {
                        firstRound = false;
                        toMeet = toMeet.next;
                        if (toMeet == null) {
                                // only 1 set
                                if (setA != null) {
                                        met = setA.copy();
                                }
                                break;
                        }
                        else{   
                                G_Facts setB =  toMeet.head.a;
                                met = G_Facts.meets(setA, setB, mode);
                        }
                }
                else {
                        met = G_Facts.meets(met, setA, mode);
                }
        }
        return met;
}

    public static G_Facts meets(G_Facts toMeetA, G_Facts toMeetB, boolean union) {
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
        G_Facts met = null;
        G_Facts unionMet = null;
        G_Facts toMeetAHead = toMeetA = toMeetA.copy();
        G_Facts toMeetBHead = toMeetB = toMeetB.copy();
        
        
        G_Facts previousA = null;
        G_Facts previousB = null;

        while (toMeetA != null)
        {
                toMeetB = toMeetBHead;
                previousB = null;
                boolean found = false;
                while (toMeetB != null)
                {
                        if (toMeetA.head.equal(toMeetB.head)) {
                                        G_Fact toInsert = toMeetA.head.copy();
                                        toInsert.merge(toMeetB.head);
                                        met = new G_Facts(toInsert, met);
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
                        previousA = toMeetA;
                }
                toMeetA = toMeetA.next;
        }

        if (union) {
                while (toMeetAHead != null) {
                        met = new G_Facts(toMeetAHead.head.copy(), met);
                        toMeetAHead = toMeetAHead.next;
                }
                while (toMeetBHead != null) {
                        met = new G_Facts(toMeetBHead.head.copy(), met);
                        toMeetBHead = toMeetBHead.next;
                }
                
                
        }
        return met;
}
}
