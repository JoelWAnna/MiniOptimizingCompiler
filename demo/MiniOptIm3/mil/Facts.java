package mil;

/** Lists of Facts are used to represent sets of "facts", each of
 *  which is a pair (v = t) indicating that the variable v has most
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
     *  The returned list will be the same as the input list vs if, and only if
     *  there are no changes to the list of facts.  In particular, this implies
     *  that we will not use any destructive updates, but it also allows us to
     *  avoid unnecessarily reallocating copies of the same list when there are
     *  no changes, which we expect to be the common case.
     */
    public static Facts kills(Var v, Facts facts) {
        if (facts!=null) {
            Facts fs = Facts.kills(v, facts.next);
            // A binding for the variable v kills any fact (w = t) that mentions v.
            if (facts.v==v || facts.t.contains(v)) {
                // Head item in facts is killed by binding v, so do not include
                // it in the return result:
                return (facts==fs) ? facts.next : fs;
            } else if (fs!=facts) {
                // Some items in facts.next were killed, but the head item
                // in facts is not, so we create a new list that retains the
                // head fact together with the facts left in fs:
                return new Facts(facts.v, facts.t, fs);
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
