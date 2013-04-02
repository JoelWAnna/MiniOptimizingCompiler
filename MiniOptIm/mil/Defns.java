package mil;

public class Defns {
    public Defn head;
    public Defns next;

    /** Default constructor.
     */
    public Defns(Defn head, Defns next) {
        this.head = head;
        this.next = next;
    }

    /** Depth-first search the forward dependency graph. Returns a list with the
     *  same Xs in reverse order of finishing times.  (In other words, the last
     *  node that we finish visiting will be the first node in the result list.)
     */
    public static Defns searchForward(Defns xs, Defns result) {
        for (; xs!=null; xs=xs.next) {
          result = xs.head.forwardVisit(result);
        }
        return result;
      }

    /** Depth-first search the reverse dependency graph, using the list
     *  of bindings that was obtained in the forward search, with the
     *  latest finishers first.
     */
    public static DefnSCCs searchReverse(Defns xs) {
        DefnSCCs sccs = null;
        for (; xs!=null; xs=xs.next) {
          if (xs.head.getScc()==null) {
            DefnSCC scc = new DefnSCC();
            sccs      = new DefnSCCs(scc, sccs);
            xs.head.reverseVisit(scc);
          }
        }
        return sccs;
      }

    /** Calculate the strongly connected components of a list of Xs that
     *  have been augmented with dependency information.
     */
    public static DefnSCCs scc(Defns xs) {
        /*
              // Compute the transpose (i.e., fill in the callers fields)
              for (X\s bs=xs; bs!=null; bs=bs.next) {
                for (X\s cs=bs.head.callees; cs!=null; cs=cs.next) {
                  cs.head.callers = new X\s(bs.head, cs.head.callers);
                }
              }
        
              debug.Log.println("Beginning SCC algorithm");
              for (X\s bs=xs; bs!=null; bs=bs.next) {
                debug.Log.print(bs.head.getId() + ": callees {");
                String punc = "";
                for (X\s cs = bs.head.callees; cs!=null; cs=cs.next) {
                  debug.Log.print(punc);
                  punc = ", ";
                  debug.Log.print(cs.head.getId());
                }
                debug.Log.print("}, callers {");
                punc = "";
                for (X\s cs = bs.head.callers; cs!=null; cs=cs.next) {
                  debug.Log.print(punc);
                  punc = ", ";
                  debug.Log.print(cs.head.getId());
                }
                debug.Log.println("}");
              }
        */
        
              // Run the two depth-first searches of the main algorithm.
              return searchReverse(searchForward(xs, null));
            }

    /** Test for membership in a list.
     */
    public static boolean isIn(Defn val, Defns list) {
        for (; list!=null; list=list.next) {
            if (list.head==val) {
                return true;
            }
        }
        return false;
    }

    /** Return the length of a linked list of elements.
     */
    public static int length(Defns list) {
        int len = 0;
        for (; list!=null; list=list.next) {
            len++;
        }
        return len;
    }
}
