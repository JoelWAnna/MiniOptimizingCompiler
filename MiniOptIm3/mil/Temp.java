package mil;

public class Temp extends Var {

    private String id;

    /** Default constructor.
     */
    public Temp(String id) {
        this.id = id;
    }

    private static int count = 0;

    public Temp() {
        this("t"+count++);
    }

    /** Generate a printable description of this atom.
     */
    public String toString() { return id; }

    /** Create a list of new variables corresponding to a given list of identifiers.
     */
    public static Var[] makeTemps(String[] ids) {
        int   n  = ids.length;
        Var[] vs = new Var[n];
        for (int i=0; i<n; i++) {
            vs[i] = new Temp(ids[i]);
        }
        return vs;
    }

    /** Create a list of new variables of a given length.
     */
    public static Var[] makeTemps(int n) {
        Var[] vs = new Var[n];
        for (int i=0; i<n; i++) {
            vs[i] = new Temp();
        }
        return vs;
    }

    /** Add this atom as an argument variable to the given list; only local
     *  variables and temporaries are treated as argument variables because
     *  wildcards are ignored and all other atoms can be accessed as constants.
     */
    Vars add(Vars vs) { return Vars.add(this, vs); }
}
