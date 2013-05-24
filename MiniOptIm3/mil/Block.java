package mil;

public class Block extends Defn {

    private Code code;

    /** Default constructor.
     */
    public Block(Code code) {
        this.code = code;
    }

    private static int count = 0;

    private final String id = "b" + count++;

    private Var[] formals;

    public void setFormals(Var[] formals) {
        this.formals = formals;
    }

    /** Return the identifier that is associated with this definition.
     */
    public String getId() { return id; }

    /** Find the list of Defns that this Defn depends on.
     */
    public Defns dependencies() {  // b(args) = code
        return code.dependencies(null);
    }

    void displayDefn() {
        //System.out.println("doesntReturn = " + doesntReturn);
              Call.display(id, "(", formals, ")");
              System.out.println(" =");
              if (code==null) {
                  Code.indent();
                  System.out.println("null");
              } else {
                  code.display();
              }
          }

    public Val call(Val[] vals)
      throws Fail { return code.eval(ValEnv.extend(formals, vals, null)); }

    /** Apply constructor function simplifications to this program.
     */
    void cfunSimplify() { code = code.cfunSimplify(); }

    /** Stores the list of blocks that have been derived from this block.
     */
    private Blocks derived = null;

    /** Derive a new version of this block using a code sequence that invokes
     *  the final result before it returns instead of returning that value
     *  (presumably a thunk) to calling code where it is immediately invoked
     *  and then discarded.
     */
    public Block deriveWithInvoke() {
        // Look to see if we have already derived a suitable version of this block:
        for (Blocks bs = derived; bs!=null; bs=bs.next) {
            if (bs.head instanceof BlockWithInvoke) {
                return bs.head;
            }
        }
  
        // Generate a fresh block; we have to make sure that the new block is
        // added to the derived list before we begin generating code to ensure
        // that we do not end up with multiple (or potentially, infinitely
        // many copies of the new block).
        Block b   = new BlockWithInvoke(null);
        derived   = new Blocks(b, derived);
        b.formals = this.formals;
        b.code    = code.deriveWithInvoke();
        return b;
    }

    /** Derive a new version of this block using a code sequence that applies
     *  its final result to a specifed argument value instead of returning
     *  that value (presumably a closure) to the calling code where it is
     *  immediately entered and then discarded.
     */
    public Block deriveWithEnter() {
        // Look to see if we have already derived a suitable version of this block:
        for (Blocks bs = derived; bs!=null; bs=bs.next) {
            if (bs.head instanceof BlockWithEnter) {
                return bs.head;
            }
        }
  
        // Generate a fresh block; we have to make sure that the new block is
        // added to the derived list before we begin generating code to ensure
        // that we do not end up with multiple (or potentially, infinitely
        // many copies of the new block).
        Block b   = new BlockWithEnter(null);
        derived   = new Blocks(b, derived);
        Temp arg  = new Temp();
        int l     = formals.length; // extend formals with arg
        Var[] nfs = new Var[l+1];
        for (int i=0; i<l; i++) {
            nfs[i] = formals[i];
        }
        nfs[l]    = arg;
        b.formals = nfs;
        b.code    = code.deriveWithEnter(arg);
        return b;
    }

    /** Derive a new version of this block using a code sequence that passes
     *  its final result to a specifed continuation function instead of returning
     *  that value to the calling code.
     */
    public Block deriveWithCont() {
        // Look to see if we have already derived a suitable version of this block:
        for (Blocks bs = derived; bs!=null; bs=bs.next) {
            if (bs.head instanceof BlockWithCont) {
                return bs.head;
            }
        }
  
        // Generate a fresh block; we have to make sure that the new block is
        // added to the derived list before we begin generating code to ensure
        // that we do not end up with multiple (or potentially, infinitely
        // many copies of the new block).
        Block b   = new BlockWithCont(null);
        derived   = new Blocks(b, derived);
        Temp arg  = new Temp();     // represents continuation
        int l     = formals.length; // extend formals with arg
        Var[] nfs = new Var[l+1];
        for (int i=0; i<l; i++) {
            nfs[i] = formals[i];
        }
        nfs[l]    = arg;
        b.formals = nfs;
        b.code    = code.deriveWithCont(arg);
  
  //!System.out.println("Derived block is:");
  //!b.displayDefn();
        return b;
    }

    /** Heuristic to determine if this block is a good candidate for the casesOn().
     *  TODO: investigate better functions for finding candidates!
     */
    boolean contCand() { return !(this instanceof BlockWithKnownCons) && getScc().contCand(); }

    public Block deriveWithKnownCons(Allocator[] allocs) {
        //!System.out.println("Looking for derive with Known Cons ");
        //!Allocator.display(allocs);
        //!System.out.println(" for the Block");
        //!this.display();
        //!System.out.println();
        
            // Do not create a specialized version of a simple block (i.e., a block
            // that contains only a single Done):
        //    if (code instanceof Done) {
        //System.out.println("Will not specialize this block: code is a single tail");
        //      return null;
        //  }
        //!code.display();
            if (this instanceof BlockWithKnownCons) {
        //!System.out.println("Will not specialize this block: starting point is a derived block");
              return null;
            }
        
        //!if (allocs!=null) return null; // disable deriveWithKnownCons
        
            // Look to see if we have already derived a suitable version of this block:
            for (Blocks bs = derived; bs!=null; bs=bs.next) {
              if (bs.head.hasKnownCons(allocs)) {
        //!System.out.println("Found a previous occurrence");
                return bs.head;
              }
            }
        
        //!System.out.println("Generating a new block");
            // Generate a fresh block; unlike the cases for trailing Enter and
            // Invoke, we're only going to create one block here whose code is
            // the same as the original block except that it adds a group of one
            // or more initializers.  So we'll initialize the block as follows
            // and add the initializers later.
            Block b = new BlockWithKnownCons(code.copy(), allocs);
            derived = new Blocks(b, derived);
        
            // Create lists of arguments for individual allocators
            Var[][] fss = new Var[allocs.length][];
            int len     = 0;
            for (int i=0; i<allocs.length; i++) {
              if (allocs[i]==null) {
                len++;
              } else {
                Top top = allocs[i].getTop();
                fss[i]  = Temp.makeTemps(allocs[i].getArity());
                len    += fss[i].length;
              }
            }
        
            // Generate list of formals for this block:
            Var[] nfs  = new Var[len];
            int   pos  = 0;
            for (int i=0; i<allocs.length; i++) {
              if (fss[i]==null) {
                nfs[pos++] = this.formals[i];
                len++;
              } else {
                int l = fss[i].length;
                for (int j=0; j<l; j++) {
                  nfs[pos++] = fss[i][j];
                }
              }
            }
            b.formals = nfs;
        
            // Create code for this block by prepending some initializers:
            for (int i=0; i<allocs.length; i++) {
              if (fss[i]!=null) {
                Top top = allocs[i].getTop();
        //!System.out.print("Allocator: "); allocs[i].display();
        //!System.out.println(" " +(top!=null ? (""+top) : "null"));
                Tail t  = (top!=null /*&& fss[i].length==0 */)
                           ? new Return(top)
                           : allocs[i].callDup().withArgs(fss[i]);
                b.code = new Bind(this.formals[i], t, b.code);
              }
            }
            
        //!System.out.println("New deriveWithKnownCons block:");
        //!b.displayDefn();
            return b;
          }

    boolean hasKnownCons(Allocator[] allocs) { return false; }

    /** Flag to identify blocks that "do not return".  In other words, if the
     *  value of this flag for a given block b is true, then we can be sure that
     *  (x <- b(args); c) == b(args) for any appropriate set of arguments args
     *  and any valid code sequence c.  There are two situations that can cause
     *  a block to "not return".  The first is when the block enters an infinite
     *  loop; such blocks may still be productive (such as the block defined by
     *  b(x) = (_ <- print((1)); b(x))), so we cannot assume that they will be
     *  eliminated by eliminateLoops().  The second is when the block's code
     *  sequence makes a call to a primitive call that does not return.
     */
    private boolean doesntReturn = false;

    /** Return flag, computed by previous dataflow analysis, to indicate
     *  if this block does not return.
     */
    boolean doesntReturn() {
        return doesntReturn;
    }

    /** Reset the doesntReturn flag, if there is one, for this definition
     *  ahead of a returnAnalysis().  For this analysis, we use true as
     *  the initial value, reducing it to false if we find a path that
     *  allows a block's code to return.
     */
    void resetDoesntReturn() { this.doesntReturn = true; }

    /** Apply return analysis to this definition, returning true if this
     *  results in a change from the previously computed value.
     */
    boolean returnAnalysis() {
        boolean newDoesntReturn = code.doesntReturn();
        if (newDoesntReturn != doesntReturn) {
            doesntReturn = newDoesntReturn;
            return true; // signal that a change was detected
        }
        return false; // no change
    }

    void cleanup() { code = code.cleanup(this); }

    boolean detectLoops(Blocks visited) {
        // Check to see if this block calls code for an already visited block:
        if (Blocks.isIn(this, visited) || code.detectLoops(this, visited)) {
          MILProgram.report("detected an infinite loop in block " + getId());
          code = new Done(PrimCall.loop);
          return true;
        }
        return false;
      }

    /** Apply inlining to the code in this definition.
     */
    public void inlining() {
        //!System.out.println("==================================");
        //!System.out.println("Going to try inlining on:");
        //!displayDefn();
        //!System.out.println();
            code = code.inliningBody(this);
        //!System.out.println("And the result is:");
        //!displayDefn();
        //!System.out.println();
          }

    public static final int INLINE_LINES_LIMIT = 4;

    boolean canPrefixInline(Block src) {
        if (this.getScc()!=src.getScc()) {         // Restrict to different SCCs
          int n = code.prefixInlineLength(0);
          return n>0 && (occurs==1 || n<=INLINE_LINES_LIMIT);
        }
        return false;
      }

    /** Attempt to inline the code for this block onto the front of another
     *  block of code.  Assumes that the final result computed by this block
     *  will be bound to the variable r, and that the computation will proceed
     *  with the code specified by rest.  The src value specifies the block
     *  in which the original BlockCall appeared while args specifies the set
     *  of arguments that were passed in at that call.  A null return indicates
     *  that no inlining was performed.
     */
    Code prefixInline(Block src, Atom[] args, Var r, Code rest) {
        if (canPrefixInline(src)) {
          MILProgram.report("prefixInline succeeded for call to block " + getId()
                             + " from block " + src.getId());
          return code.prefixInline(AtomSubst.extend(formals, args, null), r, rest);
        }
        return null;
      }

    /** Attempt to construct an inlined version of the code in this block that can be
     *  placed at the end of a Code sequence.  Assumes that a BlockCall to this block
     *  with the given set of arguments was included in the specified src Block.  A
     *  null return indicates that no inlining was performed.
     */
    Code suffixInline(Block src, Atom[] args) {
        //!System.out.println("Should we inline:");
        //!displayDefn();
        //!System.out.println();
        //!System.out.println("As part of the block:");
        //!if (src==null) System.out.println("Null block"); else src.displayDefn();
        //!System.out.println("?");
              if (canSuffixInline(src)) {
        //!System.out.println("YES");
                  MILProgram.report("suffixInline succeeded for call to block " + getId()
                                     + " from block " + src.getId());
                  return code.apply(AtomSubst.extend(formals, args, null));
              }
        //!System.out.println("NO");
              return null;
          }

    /** We allow a block to be inlined if the original call is in a different
     *  block, the code for the block ends with a Done, and either there is
     *  only one reference to the block in the whole program, or else the
     *  length of the code sequence is at most INLINE_LINES_LIMIT lines long.
     */
    boolean canSuffixInline(Block src) {
        if (occurs==1 || code.isDone()!=null) {    // Inline single occurrences and trivial
  //!System.out.println("Single occurrence!");
            return true;                           // blocks (safe, as a result of removing loops)
        } else if (this.getScc()==src.getScc()) {  // But otherwise don't inline blocks in same SCC
  //!System.out.println("Same SCC!");
            return false;
        } else {
            int n = code.suffixInlineLength(0);    // Inline short code blocks
  //!System.out.println("inline length = " + n);
            return n>0 && n<=INLINE_LINES_LIMIT;
        }
    }

    /** Test to see if a call to this block with specific arguments can be
     *  replaced with a Call.
     */
    public Tail inlineTail(Atom[] args) {
        Tail tail = code.isDone();
        if (tail!=null) {
          tail = tail.forceApply(AtomSubst.extend(formals, args, null));
        }
        return tail;
      }

    /** Test to determine whether this is a goto block.  The specified list of
     *  arguments indicates the parameter list that is used on entry to this
     *  block.
     */
    public BlockCall inlineBlockCall(Atom[] args) {
        BlockCall bc = code.isGotoBlockCode(this);
        if (bc!=null) {
          bc = bc.forceApplyBlockCall(AtomSubst.extend(formals, args, null));
        }
        return bc;
      }

    void liftAllocators() { code.liftAllocators(); }

    /** A bitmap that identifies the used arguments of this definition.  The base case,
     *  with no used arguments, can be represented by a null array.  Otherwise, it will
     *  be a non null array, the same length as the list of true in positions corresponding
     *  to arguments that are known to be used and false in all other positions.
     */
    private boolean[] usedArgs = null;

    /** Counts the total number of used arguments in this definition; this should match
     *  the number of true bits in the usedArgs array.
     */
    private int numUsedArgs = 0;

    /** Reset the bitmap and count for the used arguments of this definition,
     *  where relevant.
     */
    void clearUsedArgsInfo() {
        usedArgs    = null;
        numUsedArgs = 0;
    }

    /** Count the number of unused arguments for this definition using the current
     *  unusedArgs information for any other items that it references.
     */
    int countUnusedArgs() { return countUnusedArgs(formals); }

    /** Count the number of unused arguments for this definition.  A count of zero
     *  indicates that all arguments are used.
     */
    int countUnusedArgs(Var[] bound) {
        int unused = bound.length - numUsedArgs;      // count # of unused args
        if (unused > 0) {                             // skip if no unused args
            Vars vs = usedVars();                     // find vars used in body
            for (int i=0; i<bound.length; i++) {      // scan argument list
                if (usedArgs==null || !usedArgs[i]) { // skip if already known to be used
                    if (Vars.isIn(bound[i], vs) && !duplicated(i, bound)) {
                        if (usedArgs==null) {         // initialize usedArgs for first use
                            usedArgs = new boolean[bound.length];
                        }
                        usedArgs[i] = true;           // mark this argument as used
                        numUsedArgs++;                // update counts
                        unused--;
                    }
                }
            }
        }
        return unused;
    }

    /** A utility function that returns true if the variable at position i
     *  in the given array also appears in some earlier position in the array.
     *  (If this condition applies, then we can mark the later occurrence as
     *  unused; there is no need to pass the same variable twice.)
     */
    private static boolean duplicated(int i, Var[] bound) {
        // Did this variable appear in an earlier position?
        for (int j=0; j<i; j++) {
            if (bound[j]==bound[i]) {
  //!System.out.println("**************");
  //!System.out.println("**************");
  //!System.out.println("**************");
  //!System.out.println("**************");
                return true;
            }
        }
        return false;
    }

    /** Find the list of variables that are used in this definition.  Variables that
     *  are mentioned in BlockCalls, ClosAllocs, or CompAllocs are only included if
     *  the corresponding flag in usedArgs is set.
     */
    Vars usedVars() { return code.usedVars(); }

    /** Find the list of variables that are used in a call to this definition,
     *  taking account of the usedArgs setting so that we only include variables
     *  appearing in argument positions that are known to be used.
     */
    Vars usedVars(Atom[] args, Vars vs) {
        if (usedArgs!=null) {       // ignore this call if no args are used
          for (int i=0; i<args.length; i++) {
            if (usedArgs[i]) {      // ignore this argument if the flag is not set
              vs = args[i].add(vs);
            }
          }
        }
        return vs;
      }

    /** Use information about which and how many argument positions are used to
     *  trim down an array of variables (i.e., the formal parameters of a Block
     *  or the stored fields of a ClosureDefn).
     */
    Var[] removeUnusedVars(Var[] vars) {
        if (numUsedArgs==vars.length) {   // All arguments used, no rewrite needed
            return vars;
        } else if (usedArgs==null) {      // Drop all arguments; none are needed
            MILProgram.report("removing all arguments from " + getId());
            return Var.noVars;
        } else {                          // Select the subset of needed arguments
            Var[] newVars = new Var[numUsedArgs];
            for (int i=0, j=0; i<vars.length; i++) {
                if (usedArgs[i]) {
                    newVars[j++] = vars[i];
                } else {
                    MILProgram.report("removing unused argument " + vars[i]
                                                          + " from " + getId());
                }
            }
            return newVars;
        }
    }

    /** Update an argument list by removing unused arguments.
     */
    void removeUnusedArgs(Call call, Atom[] args) {
        if (numUsedArgs!=args.length) {   // Only rewrite if some arguments are unused
           Atom[] newArgs = new Atom[numUsedArgs];
           for (int i=0, j=0; j<numUsedArgs; i++) {
               if (usedArgs[i]) {         // copy used arguments
                   newArgs[j++] = args[i];
               }
           }
           call.withArgs(newArgs);        // set new argument list for this call
        }
    }

    /** Rewrite this program to remove unused arguments in block calls.
     */
    void removeUnusedArgs() {
        formals = removeUnusedVars(formals); // remove unused formal parameters
        code.removeUnusedArgs();             // update calls in code sequence
    }

    public void flow() {
        code = code.flow(null, null);
        code.liveness();
    }

    /** Test to determine whether there is a way to short out a Match
     *  from a call to this block with the specified arguments, and
     *  given the set of facts that have been computed.  We start by
     *  querying the code in the Block to determine if it starts with
     *  a Match; if not, then the optimization will not apply and a null
     *  result is returned.
     */
    BlockCall shortMatch(Atom[] args, Facts facts) { return code.shortMatch(formals, args, facts); }

    /** Calculate a summary value for a list of Atom values, typically the arguments
     *  in a Call.
     */
    int summary(Atom[] args) {
        int sum = summary();
        for (int i=0; i<args.length; i++) {
            sum = 53*sum + args[i].summary();
        }
        return sum;
    }

    /** Test to see if two Block values are alpha equivalent.
     */
    boolean alphaBlock(Block that) {
        if (this.formals.length!=that.formals.length) {
            return false;
        }
        Vars thisvars = null;
        Vars thatvars = null;
        for (int i=0; i<formals.length; i++) {
           thisvars = new Vars(this.formals[i], thisvars);
           thatvars = new Vars(that.formals[i], thatvars);
        }
        return this.code.alphaCode(thisvars, that.code, thatvars);
    }

    /** Holds the most recently computed summary value for this block.
     */
    private int summary;

    /** Points to a different block with equivalent code, if one has been
     *  identified.  A null value indicates that there is no replacement
     *  block.
     */
    private Block replaceWith = null;

    /** Look for a previously summarized version of this block in the table.
     *  Return true if a duplicate was found.
     */
    boolean findIn(Blocks[] table) {
        int idx = this.summary % table.length;
        if (idx<0) idx += table.length;
   
        for (Blocks bs = table[idx]; bs!=null; bs=bs.next) {
            if (bs.head.summary==this.summary && bs.head.alphaBlock(this)) {
                this.replaceWith = bs.head;
                MILProgram.report("Replacing " + this.getId() + " with " + bs.head.getId());
                return true;
            }
        }
        this.replaceWith = null;    // There is no replacement for this block
        table[idx]       = new Blocks(this, table[idx]);
        return false;
     }

    /** Compute a summary for this definition (if it is a block) and then look for
     *  a previously encountered block with the same code in the given table.
     *  Return true if a duplicate was found.
     */
    boolean summarizeBlocks(Blocks[] table) { summary = code.summary(); return findIn(table); }

    void eliminateDuplicates() {
        code.eliminateDuplicates();
    }

    Block replaceWith() { return replaceWith==null ? this : replaceWith; }

    public void analyzeCalls() { code.analyzeCalls();
        }

    /** Allocate a block without any initial code.  This allows us to
     *  create looping structures between blocks, assuming that the code
     *  for this particular block will be filled in later by using the
     *  setCode() method.
     */
    public Block() { this(null); }

    /** Provide a way to set the code for a Block that was initialized
     *  without specifying what code sequence it should contain.
     */
    public void setCode(Code code) {
        this.code = code;
    }

    /** Generate a code sequence that makes a tail call to this block.
     */
    public Code toCode() {
        return new Done(new BlockCall(this));
    }

    /** Records the set of live variables at the top of this block.
     */
    private Vars liveVars = null;

    /** Return the set variables that are live at the top of this block.
     */
    Vars getLiveVars() {
        return liveVars;
    }

    /** Calculate the set of live variables for the code in a block,
     *  returning true if the block is recursive and new variables have
     *  been added to the liveVars list for the block during this call.
     *  For any other type of Defn, we just return false.
     */
    boolean computeLiveVars() {
        Vars vs = code.liveVars();
        if (getScc().isRecursive()) {
            boolean changed = false;
            for (; vs!=null; vs=vs.next) {
                if (!Vars.isIn(vs.head, liveVars)) {
                    liveVars = new Vars(vs.head, liveVars);
                    changed  = true;
                }
            }
            return changed;
        } else {
            liveVars = vs;
            return false;
        }
    }

    /** Rewrite trailing block calls (i.e., block calls at the end of code
     *  sequences) to use the lists of arguments that have previously been
     *  computed by the live variables analysis.
     */
    void fixTrailingBlockCalls() {
        code.fixTrailingBlockCalls();
    }

    /** Calculate a list of arguments for entering this block at the end of
     *  a code sequence, possibly setting the formals for the block from the
     *  set of live variables that were computed by the preceding liveness
     *  analysis.
     */
    Atom[] formalsToArgs() {
        if (formals==null) {
            // No formals?  Then initialize from computed live variables
            formals = new Var[Vars.length(liveVars)];
            for (int i=0; liveVars!=null; liveVars=liveVars.next) {
                formals[i++] = liveVars.head;
            }
        } else if (liveVars!=null) {
            // Formals already?  Check that they cover all of the inferred
            // live variables!
            for (; liveVars!=null; liveVars=liveVars.next) {
                if (!Var.isIn(liveVars.head, formals)) {
                    debug.Internal.error("Free variable " + liveVars.head
                                            + " is not included in formals");
                }
            }
        }
  
        // Make a fresh copy of the formals as a list of atoms, suitable
        // for use in a BlockCall:
        Atom[] args = new Atom[formals.length];
        for (int i=0; i<formals.length; i++) {
            args[i] = formals[i];
        }
        return args;
    }

    public void setNextAnticipatedIns() {
        anticipated_In_set = anticipated_Next_Ins;
        }

    public void computeAnticipatedInMeets() {
        debug.Log.println("computeAnticipatedInMeets At block " + id);
        boolean firstRound = true;
        boolean union = true;
        boolean mode = !union; // The interpretation of !union is intersection
        Sets insIter;
        for (insIter = incomingOut_Sets; insIter != null; insIter = insIter.next) {
                G_Facts caller =  insIter.head.a;
                
                if (firstRound) {
                        firstRound = false;
                        incomingOut_Sets = incomingOut_Sets.next;
                        if (incomingOut_Sets == null) {
                                // only 1 caller to this block
                                if (caller != null) {
                                        anticipated_Out_set = caller.copy();
                                }
                                break;
                        }
                        else{   
                                G_Facts nextCaller =  incomingOut_Sets.head.a;
                                anticipated_Out_set = G_Facts.meets(caller, nextCaller, mode);
                        }
                }
                else {
                        anticipated_Out_set = G_Facts.meets(anticipated_Out_set, caller, mode);
                }
        }
        incomingOut_Sets = null;
        }

    public int Calculate_Anticipated_Expr() {
        debug.Log.println("Calculate_Anticipated_Expr At block " + id);
        boolean union = true;
        //printInsOuts();
        anticipated_Next_Ins = code.inset(anticipated_Out_set, id);
        //printInsOuts();
        int oldlen = G_Facts.length(anticipated_In_set);
        if ((oldlen != G_Facts.length(anticipated_Next_Ins) )
                || (oldlen != G_Facts.length(G_Facts.meets(anticipated_Next_Ins, anticipated_In_set, !union)))
                ){
                return 1;
        }       

        return 0;
        }

    public void clearAnticipatedInsOuts() { incomingOut_Sets = null; anticipated_Out_set = anticipated_In_set = null; }

    public void printAnticipatedInsOuts() {
        if (anticipated_Out_set != null) {
        anticipated_Out_set.print(true, id);
        }
        if (anticipated_In_set != null) {
                anticipated_In_set.print(false, id);
        }
}

    public Sets incomingOut_Sets;

    public G_Facts anticipated_Out_set;

    public G_Facts anticipated_In_set;

    public G_Facts anticipated_Next_Ins;

    public void setNextOuts() {
        avail_Out_Set = avail_Next_Out;
        }

    public void computeInMeets() {
        debug.Log.println("ComputeInMeets At block " + id);
        boolean firstRound = true;
        boolean union = true;
        boolean mode = !union; // The interpretation of !union is intersection
        Sets insIter;
        for (insIter = incoming_Sets; insIter != null; insIter = insIter.next) {
                G_Facts caller =  insIter.head.a;
                
                if (firstRound) {
                        firstRound = false;
                        incoming_Sets = incoming_Sets.next;
                        if (incoming_Sets == null) {
                                // only 1 caller to this block
                                if (caller != null) {
                                        avail_In_Set = caller.copy();
                                }
                                break;
                        }
                        else{   
                                G_Facts nextCaller =  incoming_Sets.head.a;
                                avail_In_Set = G_Facts.meets(caller, nextCaller, mode);
                        }
                }
                else {
                        avail_In_Set = G_Facts.meets(avail_In_Set, caller, mode);
                }
        }
        incoming_Sets = null;
        }

    public int Calculate_Avail_Expr() {
        debug.Log.println("Calculate_Avail_Expr At block " + id);
        boolean union = true;
        //printInsOuts();
        avail_Next_Out = code.outset(avail_In_Set, id);
        //printInsOuts();
        int oldlen = G_Facts.length(avail_Out_Set);
        if ((oldlen != G_Facts.length(avail_Next_Out) )
                || (oldlen != G_Facts.length(G_Facts.meets(avail_Next_Out, avail_Out_Set, !union)))
                ){
                return 1;
        }       

        return 0;
        }

    public void clearInsOuts() { incoming_Sets = null; avail_In_Set = avail_Out_Set = null; }

    public void printInsOuts() {
        if (avail_In_Set != null) {
        avail_In_Set.print(true, id);
        }
        if (avail_Out_Set != null) {
                avail_Out_Set.print(false, id);
        }
}

    public Sets incoming_Sets;

    public G_Facts avail_In_Set;

    public G_Facts avail_Out_Set;

    public G_Facts avail_Next_Out;

    public Var[] getFormals() {
        return formals;
    }
}
