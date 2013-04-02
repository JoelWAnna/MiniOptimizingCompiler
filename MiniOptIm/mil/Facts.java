package mil;


/** Lists of Facts are used to represent sets of "facts", each of
 *  which is a pair (v <- t) indicating that the variable v has most
 *  recently been bound by the specified tail t (which should be either
 *  an allocator or a pure primitive call).  We can use lists of
 *  facts like this to perform dataflow analysis and optimizations on
 *  Code sequences.
 */
public class Facts {
    private Var v;
    private Tail t;
    private Facts next;

    /** Default constructor.
     */
    public Facts(Var v, Tail t, Facts next) {
        this.v = v;
        this.t = t;
        this.next = next;
    }

    /** Remove any facts that are killed as a result of binding the variable v.
     */
    public static Facts kills(Var v, Facts facts) {
        Facts prev = null;
        Facts fs   = facts;
        while (fs!=null) {
          // A binding for the variable v kills any fact (w <- t) that mentions v.
          if (fs.v==v || fs.t.contains(v)) {
            if (prev==null) { // kill this fact
              facts     = fs = fs.next;
            } else {
              prev.next = fs = fs.next;
            }
          } else {            // keep this fact
            prev = fs;
            fs   = fs.next;
          }
        }
        return facts;
      }

    /** Look for a fact about a specific variable.
     */
    public static Tail lookupFact(Var v, Facts facts) {
        for (; facts!=null; facts=facts.next) {
          if (facts.v==v) {
            return facts.t;
          }
        }
        return null;
      }

    /** Look for a previous computation of the specified tail in the current
     *  set of facts; note that the set of facts should only contain pure
     *  computations (allocators and pure primitive calls)..
     */
    public static Var find(Tail t, Facts facts) {
        for (; facts!=null; facts=facts.next) {
          if (facts.t.sameTail(t)) {
            return facts.v;
          }
        }
        return null;
      }
}
