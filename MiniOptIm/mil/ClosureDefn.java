package mil;

public class ClosureDefn extends Defn {
    private Var arg;
    private Tail tail;

    /** Default constructor.
     */
    public ClosureDefn(Var arg, Tail tail) {
        this.arg = arg;
        this.tail = tail;
    }
    private static int count = 0;
    private final String id = "k" + count++;
    private Var[] stored = Var.noVars;
    void setStored(Var[] stored) {
        this.stored = stored;
    }

    /** Return the identifier that is associated with this definition.
     */
    public String getId() { return id; }

    /** Find the list of Defns that this Defn depends on.
     */
    public Defns dependencies() {  // k{stored} arg = tail
        return tail.dependencies(null);
    }
    void displayDefn() {
        Call.display(id, "{", stored, "} ");
        System.out.print(arg + " = ");
        tail.displayln();
    }
    public Val enter(Val[] vals, Val val)
      throws Fail {
        return tail.eval(new ValEnv(arg, val, ValEnv.extend(stored, vals, null)));
    }
    void cfunSimplify() { tail = tail.removeNewtypeCfun(); }
    public void inlining() {
        //!  System.out.println("==================================");
        //!  System.out.println("Going to try inlining on:");
        //!  displayDefn();
        //!  System.out.println();
               tail = tail.inlineTail();
        //!  System.out.println("And the result is:");
        //!  displayDefn();
        //!  System.out.println();
          }
    public void flow() {
        // TODO: Do something here ... ?
      }
    
    @Override
    public Defns propagateConstants(int maxArgReplacement, boolean unrollLoops) {
		// TODO: Implement this, only sibling class that it is implemented is Block.java
    	System.out.println("reached ClosureDefn propagateConstants of block" + id);
    	return null;
    }
   
    /** Compute a Tail that gives the result of entering this closure given the
     *  arguments that are stored in the closure and the extra argument that
     *  prompted us to enter this closure in the first place.
     */
    Tail withArgs(Atom[] args, Atom a) {
        return tail.forceApply(new AtomSubst(arg, a,
                                AtomSubst.extend(stored, args, null)));
      }
    public void analyzeCalls() { tail.analyzeCalls(); }
}
