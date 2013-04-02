package mil;

public class Vars {
    public Var head;
    public Vars next;

    /** Default constructor.
     */
    public Vars(Var head, Vars next) {
        this.head = head;
        this.next = next;
    }

    /** Test for membership in a list.
     */
    public static boolean isIn(Var val, Vars list) {
        for (; list!=null; list=list.next) {
            if (list.head==val) {
                return true;
            }
        }
        return false;
    }

    /** Return the length of a linked list of elements.
     */
    public static int length(Vars list) {
        int len = 0;
        for (; list!=null; list=list.next) {
            len++;
        }
        return len;
    }
    public static String toString(Vars vs) {
        StringBuffer b = new StringBuffer("{");
        if (vs!=null) {
            b.append(vs.head.toString());
            while ((vs=vs.next)!=null) {
                b.append(",");
                b.append(vs.head.toString());
            }
        }
        b.append("}");
        return b.toString();
    }
    public static Var[] toArray(Vars vs) {
        Var[] va = new Var[Vars.length(vs)];
        for (int i=0; vs!=null; vs=vs.next) {
            va[i++] = vs.head;
        }
        return va;
    }
    public static Vars add(Var v, Vars vs) {
        return Vars.isIn(v,vs) ? vs : new Vars(v, vs);
    }
    public static Vars add(Var[] vararray, Vars vs) {
        for (int i=0; i<vararray.length; i++) {
            vs = add(vararray[i], vs);
        }
        return vs;
    }
    public static Vars add(Vars us, Vars vs) {
        for (; us!=null; us=us.next) {
            vs = add(us.head, vs);
        }
        return vs;
    }

    /** Add the arguments in an array of atoms to a given list of variables.
     */
    public static Vars add(Atom[] args, Vars vs) {
        for (int i=0; i<args.length; i++) {
            vs = args[i].add(vs);
        }
        return vs;
    }
    public static Vars remove(Var v, Vars vs) {
        Vars prev = null;
        for (Vars us=vs; us!=null; us=us.next) {
            if (us.head==v) {
                if (prev==null) {
                    return us.next;       // remove first element
                } else {
                    prev.next = us.next;  // remove later element
                    return vs;            // and return modified list
                }
            }
            prev = us;
        }
        return vs;  // variable not listed
    }
    public static Vars remove(Var[] vararray, Vars vs) {
        for (int i=0; i<vararray.length; i++) {
            vs = remove(vararray[i], vs);
        }
        return vs;
    }
    public static Vars remove(Vars us, Vars vs) {
        for (; us!=null; us=us.next) {
            vs = remove(us.head, vs);
        }
        return vs;
    }
}
