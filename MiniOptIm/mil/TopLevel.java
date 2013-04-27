package mil;

public class TopLevel extends Defn {
    private String id;

    /** Default constructor.
     */
    public TopLevel(String id) {
        this.id = id;
    }
    private Tail tail;

    /** Set the value associated with a top-level definition.
     */
    void setTopLevel(Tail tail) {
        if (this.tail!=null) {
            debug.Internal.error("Attempt to set a second top-level value for a TopLevel variable");
        }
        this.tail = tail;
    }

    /** Return the identifier that is associated with this definition.
     */
    public String getId() { return id; }

    /** Test to determine whether this Defn is for a top level value with
     *  the specified name.
     */
    public boolean defines(String id) { return id.equals(this.id); }

    /** Test to determine if this item represents a constructor function.
     */
    public Cfun isCfun() { return null; }

    /** Find the list of Defns that this Defn depends on.
     */
    public Defns dependencies() {  // id <- tail
        return tail.dependencies(null);
    }
    void displayDefn() {
        System.out.print(id + " <- ");
        if (tail==null) {
            System.out.println("null");
        } else {
            System.out.println();
            Code.indent();
            tail.displayln();
        }
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
    public Tail lookupFact(Top top) { return tail.lookupFact(top); }
    public void flow() {
        // TODO: Do something here ... ?
      }
    @Override
    public Defns propagateConstants(int maxArgReplacement, boolean unrollLoops) {
		// TODO: Implement this, only sibling class that it is implemented is Block.java
    	System.out.println("reached TopLevel propagateConstants of block" + id);
    	return null;
    }
    Atom shortTopLevel(Atom d) { return tail.shortTopLevel(d); }
    public Tail invokesTopLevel() {
        MILProgram.report("replacing invoke " + getId() + " with block call");
        return this.toBlockCall().deriveWithInvoke();
      }

    /** Return a BlockCall for a TopLevel value, possibly introducing a new
     *  (zero argument) block to hold the original code for the TopLevel value.
     */
    public BlockCall toBlockCall() {
        BlockCall bc = tail.isBlockCall();
        if (bc==null) {
          Block b = new Block(new Done(tail));
          b.setFormals(new Var[0]);  // TODO: lift this to global constant
          bc = new BlockCall(b);
          bc.withArgs(new Atom[0]);
          tail = bc;
        }
        return bc;
      }

    /** Test to determine whether this Code/Tail value corresponds to a
     *  closure allocator, returning either a ClosAlloc value, or else
     *  a null result.
     */
    ClosAlloc lookForClosAlloc() { return tail.lookForClosAlloc(); }

    /** Test to see if this Code/Tail is a CompAlloc (that is, if it has
     *  the form m[x..] for some m and x..).
     */
    CompAlloc lookForCompAlloc() { return tail.lookForCompAlloc(); }
    public void analyzeCalls() { tail.analyzeCalls(); }
}
