package mil;

public class G_Fact {

    public Tail t;

    public Atoms atoms;

    /** Default constructor.
     */
    public G_Fact(Tail t, Atoms atoms) {
        this.t = t;
        this.atoms = atoms;
    }

    public boolean equal(G_Fact other) {
        if (this.t.sameTail(other.t))
        {
                return true;
        }
        else {
                System.out.println("this");
                t.display();
                System.out.print(" NEQ ");
                other.t.display();
                System.out.println("\nthat");
                return false;
        }
}

    public boolean merge(G_Fact other) {
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

    public G_Fact copy() {
        return new G_Fact(t, atoms.copy());
        }

    public G_Fact copyWithSubst(Atom[] args, Atom[] formals) {
        return  new G_Fact(t.copyWithSubst(args, formals), atoms);      
                }
}
