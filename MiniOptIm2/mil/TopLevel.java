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

    /** Apply constructor function simplifications to this program.
     */
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

    /** Reset the bitmap and count for the used arguments of this definition,
     *  where relevant.
     */
    void clearUsedArgsInfo() { /* nothing to do in this case */ }

    /** Count the number of unused arguments for this definition using the current
     *  unusedArgs information for any other items that it references.
     */
    int countUnusedArgs() { return 0; }

    /** Rewrite this program to remove unused arguments in block calls.
     */
    void removeUnusedArgs() {
        tail.removeUnusedArgs();
    }

    public Tail lookupFact(Top top) { return tail.lookupFact(top); }

    public void flow() {
        // TODO: Do something here ... ?
    }

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

    Atom shortTopLevel(Atom d) { return tail.shortTopLevel(d); }

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

    /** Compute a summary for this definition (if it is a block) and then look for
     *  a previously encountered block with the same code in the given table.
     *  Return true if a duplicate was found.
     */
    boolean summarizeBlocks(Blocks[] table) { return false; }

    void eliminateDuplicates() {
        tail.eliminateDuplicates();
     }
}
