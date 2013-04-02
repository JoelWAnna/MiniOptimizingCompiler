package mil;


/** AtomSubst value represent substitutions of Atoms for Vars as simple
 *  linked list structures.
 */
public class AtomSubst {
    private Var v;
    private Atom a;
    private AtomSubst rest;

    /** Default constructor.
     */
    public AtomSubst(Var v, Atom a, AtomSubst rest) {
        this.v = v;
        this.a = a;
        this.rest = rest;
    }

    /** Extend a substitution with bindings given by a pair of arrays.
     */
    public static AtomSubst extend(Var[] vs, Atom[] as, AtomSubst s) {
        if (vs.length != as.length) {
            debug.Internal.error("AtomSubst.extend: variable/atom counts do not match.");
        }
        for (int i=0; i<as.length; i++) {
            s = new AtomSubst(vs[i], as[i], s);
        }
        return s;
    }

    /** Remove any previous binding for the variable from the
     *  given substitution, using destructive updates.
     *
     *  TODO: is this sufficient if one of the remaining bindings still mentions w?
     */
    public static AtomSubst remove(Var w, AtomSubst s) {
        if (s==null) {
            return null;
        } else if (s.v.isVar(w)) {
            return s.rest;
        } else {
            s.rest = remove(w, s.rest);
            return s;
        }
    }

    /** Apply the given substitution to the specified variable.
     */
    public static Atom apply(Var w, AtomSubst s) {
        for (; s!=null; s=s.rest) {
            if (s.v.isVar(w)) {
                return s.a;
            }
        }
        return w;
    }

    /** Apply the given substitution to a vector of atoms.
     */
    public static Atom[] apply(Atom[] args, AtomSubst s) {
        int    n     = args.length;
        Atom[] nargs = new Atom[n];
        for (int i=0; i<n; i++) {
            nargs[i] = args[i].apply(s);
        }
        return nargs;
    }
}
