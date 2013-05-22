package mil;

public class Pair {

    public Tail t;

    public Atoms atoms;

    /** Default constructor.
     */
    public Pair(Tail t, Atoms atoms) {
        this.t = t;
        this.atoms = atoms;
    }

    public boolean equal(Pair other) {
        if (this.t.sameTail(other.t))
        {
                return true;
        }
        else {
                System.out.println("this");
                t.display();
                System.out.println("NEQ");
                other.t.display();
                System.out.println("that");
                return false;
        }
}

    public boolean merge(Pair other) {
        if (this.equal(other)) {
                Atoms current = other.atoms;
                while (current != null)
                {
                        boolean found = false;
                        Atoms old = atoms;
                        while (old != null) {
                                if (old.head.sameAtom(current.head)) {
                                        found = true;
                                        break;
                                }
                                old = old.next;
                        }
                if (!found)
                        atoms = new Atoms(current.head, atoms);
                current = current.next;
                }
                return true;
        }
        return false;
}

    public Pair copy() {
        return new Pair(t, atoms.copy());
        }

    public Pair copyWithSubst(Atom[] args, Var[] formals) {
        return  new Pair(t.copyWithSubst(args, formals), atoms);        
                }
}
