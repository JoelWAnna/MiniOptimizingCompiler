package mil;

public class BlockCall extends Call {

    private Block b;

    /** Default constructor.
     */
    public BlockCall(Block b) {
        this.b = b;
    }

    /** Test to see if two Tail expressions are the same.
     */
    public boolean sameTail(Tail that) { return that.sameBlockCall(this); }

    boolean sameBlockCall(BlockCall that) { return this.b==that.b && this.sameArgs(that); }

    /** Find the list of Defns that this Tail depends on.
     */
    public Defns dependencies(Defns ds) { // b(args)
        return b.dependencies(Atom.dependencies(args, ds));
    }

    /** Display a printable representation of this MIL construct
     *  on the standard output.
     */
    public void display() { display(b.getId(), "(",  args, ")");  
        if (allox!=null) {
          Allocator.display(allox);
        }
      }

    /** Construct a new Call value that is based on the receiver,
     *  without copying the arguments.
     */
    Call callDup() { return new BlockCall(b); }

    /** A special version of apply that works only on BlockCalls;
     *  used in places where type preservation of a BlockCall argument
     *  is required.  In particular, we don't use withArgs here because
     *  that loses type information, producing a Body from a BlockCall
     *  input.
     */
    public BlockCall applyBlockCall(AtomSubst s) { return (s==null) ? this : forceApplyBlockCall(s); }

    /** A special version of forceApply that works only on BlockCalls.
     */
    BlockCall forceApplyBlockCall(AtomSubst s) {
        BlockCall bc = new BlockCall(b);
        bc.args      = AtomSubst.apply(args, s);
        return bc;
    }

    public Val eval(ValEnv env)
      throws Fail { return b.call(ValEnv.lookup(args, env)); }

    /** Test to determine if this Tail or Code value is a BlockCall.
     */
    public BlockCall isBlockCall() { return this; }

    /** Generate a new version of this block call that branches to a
     *  block with a trailing invoke.
     */
    public BlockCall deriveWithInvoke() {
        BlockCall bc = new BlockCall(b.deriveWithInvoke());
        bc.withArgs(args);
        return bc;
    }

    /** Generate a new version of this block call by passing the
     *  specified argument to a derived block with a trailing enter.
     */
    public BlockCall deriveWithEnter(Atom arg) {
        BlockCall bc = new BlockCall(b.deriveWithEnter());
        int l        = args.length;
        Atom[] nargs = new Atom[l+1]; // extend args with arg
        for (int i=0; i<l; i++) {
            nargs[i] = args[i];
        }
        nargs[l] = arg;
        bc.withArgs(nargs);
        return bc;
    }

    /** Generate a new version of this block call by passing the specified
     *  continuation argument to a derived block with a trailing continuation
     *  invocation.
     */
    public BlockCall deriveWithCont(Atom cont) {
        BlockCall bc = new BlockCall(b.deriveWithCont());
        int l        = args.length;
        Atom[] nargs = new Atom[l+1]; // extend args with arg
        for (int i=0; i<l; i++) {
            nargs[i] = args[i];
        }
        nargs[l] = cont;
        bc.withArgs(nargs);
        return bc;
    }

    /** Heuristic to determine if this block is a good candidate for the casesOn().
     *  TODO: investigate better functions for finding candidates!
     */
    boolean contCand() { return b.contCand(); }

    public BlockCall deriveWithKnownCons(Allocator[] allocs) {
        //!System.out.print("deriveWithKnownCons for BlockCall: ");
        //!this.display();
        //!System.out.println();
            if (allocs.length!=args.length) {
              debug.Internal.error("argument list length mismatch in deriveWithKnownCons");
            }
            Block nb = b.deriveWithKnownCons(allocs);
            if (nb==null) {
        //!System.out.println("Declined to specialize this block!");
              return null;
            } else {
              BlockCall bc = new BlockCall(nb);
              bc.withArgs(specializedArgs(allocs));
        //!System.out.print("Rewrote knownCons call: ");
        //!this.display();
        //!System.out.print(" as: ");
        //!bc.display();
        //!System.out.println();
              return bc;
            }
          }

    /** Test for code that is guaranteed not to return.
     */
    boolean doesntReturn() { return b.doesntReturn(); }

    boolean detectLoops(Block src, Blocks visited) {     // Keep searching while we're still in the same SCC
        return (src.getScc()==b.getScc())
            && b.detectLoops(new Blocks(src, visited));
      }

    Code prefixInline(Block src, Var r, Code c) { return b.prefixInline(src, args, r, c); }

    /** Perform suffix inlining on this tail, which either replaces a block call
     *  with an appropriately renamed copy of the block's body, or else returns
     *  null if the tail is either not a block call, or if the code of the block
     *  is not suitable for inlining.
     */
    Code suffixInline(Block src) { return b.suffixInline(src, args); }

    /** Skip goto blocks in a Tail (for a ClosureDefn or TopLevel).
     *  TODO: can this be simplified now that ClosureDefns hold Tails rather than Calls?
     *
     *  TODO: couldn't we return an arbitrary Tail here, not just a Call?
     */
    public Tail inlineTail() {
        BlockCall bc   = this.inlineBlockCall();
        Tail      tail = bc.b.inlineTail(bc.args);
        return (tail==null) ? bc : tail;
      }

    /** Rewrite a BlockCall, if possible, to skip over goto blocks.
     */
    public BlockCall inlineBlockCall() {
        BlockCall bc = b.inlineBlockCall(args);
        if (bc!=null) {
          MILProgram.report("eliminateGoto succeeded on call to "+bc.b.getId());
          return bc;
        }
        return this;
      }

    /** Test to determine whether this Code/Tail in a specified src Block
     *  constitutes a goto block.  For this to be valid, the code must be
     *  an immediate BlockCall to a different block.
     */
    public BlockCall isGotoBlockCode(Block src) { return (b!=src) ? this : null; }

    /** Find the variables that are used in this Tail expression, adding them to
     *  the list that is passed in as a parameter.  Variables that are mentioned
     *  in BlockCalls, ClosAllocs, or CompAllocs are only included if the
     *  corresponding flag in usedArgs is set; all of the arguments in other types
     *  of Call (i.e., PrimCalls and DataAllocs) are considered to be "used".
     */
    Vars usedVars(Vars vs) { return b.usedVars(args, vs); }

    /** Rewrite this program to remove unused arguments in block calls.
     */
    void removeUnusedArgs() { b.removeUnusedArgs(this, args); }

    public Code rewrite(Facts facts) {
        BlockCall bc = rewriteBlockCall(facts);
        return (bc==this) ? null : new Done(bc);  // TODO: worried about this == test
      }

    BlockCall rewriteBlockCall(Facts facts) {
        // Look for an opportunity to short out a Match if this block branches
        // to a Match for a variable that has a known DataAlloc value in the
        // current set of facts.
        BlockCall bc = this.shortMatch(facts);
        bc = (bc==null) ? this : bc.inlineBlockCall();
    
        // Look for an opportunity to specialize on known constructors:
        Allocator[] allocs = bc.collectAllocs(facts); // null;
        this.allox = allocs; // TODO: temporary, for inspection of results.
        if (allocs!=null) {
          BlockCall bc1 = bc.deriveWithKnownCons(allocs);
          if (bc1!=null) {
            bc = bc1;
            MILProgram.report("deriving specialized block for BlockCall to block " + b.getId());
    //!System.out.print("deriveWithKnownCons for BlockCall: ");
    //!this.display();
    //!System.out.print(" -> ");
    //!bc.display();
    //!System.out.println();
          }
        }
    
        // when we find, b16(t109, id, t110){-, k35{}, -}, it isn't
        // necessarily a good idea to create a specialized block if the
        // id <- k35{} line appears in this block (i.e., if id is local,
        // not a top level value) and id is used elsewhere in the block.
    
        return bc;
      }

    private Allocator[] allox;

    BlockCall shortMatch(Facts facts) { return b.shortMatch(args, facts); }

    /** Compute an integer summary for a fragment of MIL code with the key property
     *  that alpha equivalent program fragments have the same summary value.
     */
    int summary() { return b.summary(args)*33; }

    /** Test to see if two Tail expressions are the alpha equivalent.
     */
    boolean alphaTail(Vars thisvars, Tail that, Vars thatvars) { return that.alphaBlockCall(thatvars, this, thisvars); }

    /** Test two items for alpha equivalence.
     */
    boolean alphaBlockCall(Vars thisvars, BlockCall that, Vars thatvars) { return this.b==that.b && this.alphaArgs(thisvars, that, thatvars); }

    void eliminateDuplicates() { b = b.replaceWith(); }

    public void analyzeCalls() { b.called(); }

    /** Compute the set of live variables that appear in this Tail, adding
     *  each one to the list that is passed in as a parameter if it is not
     *  already included.
     */
    Vars liveVars(Vars vs) {
        return Vars.add(b.getLiveVars(), super.liveVars(vs));
    }

    /** Rewrite trailing block calls (i.e., block calls at the end of code
     *  sequences) to use the lists of arguments that have previously been
     *  computed by the live variables analysis.
     */
    void fixTrailingBlockCalls() {
        if (args==null) {
            withArgs(b.formalsToArgs());
        }
        // else { debug.Internal.error("tail block call with non null arguments"); }
    }

    public G_Facts addOuts(G_Facts outs) {
        
                //G_Facts renamed = outs.copy();
                G_Facts renamed = outs;
                if (outs != null) {
                        renamed = outs.copyWithSubst(args, b.getFormals());
                }
                b.incomingOut_Sets = new Sets(new Set(renamed), b.incomingOut_Sets);
                if (b.anticipated_In_set != null)
                        return b.anticipated_In_set.copyWithSubst(b.getFormals(), args);
                return outs;
        }

    public G_Facts addIns(G_Facts ins) {
        
                //G_Facts renamed = ins.copy();
                G_Facts renamed = ins;
                if (ins != null) {
                        renamed = ins.copy();//WithSubst(args, b.getFormals());
                }
                b.incoming_Sets = new Sets(new Set(renamed), b.incoming_Sets);
                return b.avail_Out_Set;
        }
}
