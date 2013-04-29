package mil;

public class BlockCall extends Call {
    private Block b;

    /** Default constructor.
     */
    public BlockCall(Block b) {
        this.b = b;
    }

    /** Test to see if two Tails are the same.
     */
    public boolean sameTail(Tail that) { return that.isBlockCall(this); }
    boolean isBlockCall(BlockCall that) { return this.b==that.b && this.sameArgs(that); }

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
    boolean detectLoops(Block src, Blocks visited) {     // Keep searching while we're still in the same SCC
        return (src.getScc()==b.getScc())
            && b.detectLoops(new Blocks(src, visited));
      }
    Code prefixInline(Block src, Var r, Code c) { return b.prefixInline(src, args, r, c); }

    /**
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
    public BlockCall deriveWithInvoke() {
        BlockCall bc = new BlockCall(b.deriveWithInvoke());
        bc.withArgs(args);
        return bc;
      }

    /** Test to determine if this Tail or Code value is a BlockCall.
     */
    public BlockCall isBlockCall() { return this; }
    public BlockCall deriveWithEnter(Atom arg) {
        BlockCall bc = new BlockCall(b.deriveWithEnter());
        int l        = args.length;
        Atom[] nargs = new Atom[l+1]; // extend args with arg
        for (int i=0; i<l; i++) {
          nargs[i] = args[i];
        }
        nargs[l] = arg;
        bc.withArgs(nargs);
    //!System.out.print("Rewrote call: ");
    //!this.display();
    //!System.out.print(" as: ");
    //!bc.display();
    //!System.out.println();
        return bc;
      }
    public BlockCall deriveWithCont(Atom cont) {
        BlockCall bc = new BlockCall(b.deriveWithCont());
        int l        = args.length;
        Atom[] nargs = new Atom[l+1]; // extend args with arg
        for (int i=0; i<l; i++) {
          nargs[i] = args[i];
        }
        nargs[l] = cont;
        bc.withArgs(nargs);
    //!System.out.print("Rewrote call: ");
    //!this.display();
    //!System.out.print(" as: ");
    //!bc.display();
    //!System.out.println();
        return bc;
      }
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
        
              // Compute the number of actual arguments that are needed:
              int len = 0;
              for (int i=0; i<allocs.length; i++) {
                len += (allocs[i]==null ? 1 : allocs[i].getArity());
              }
        
              // Fill in the actual arguments:
              Atom[]    nargs = new Atom[len];
              int       pos   = 0;
              for (int i=0; i<args.length; i++) {
                if (allocs[i]==null) {
                  nargs[pos++] = args[i];
                } else {
                  pos = allocs[i].collectArgs(nargs, pos);
                }
              }
              bc.withArgs(nargs);
        
        //!System.out.print("Rewrote knownCons call: ");
        //!this.display();
        //!System.out.print(" as: ");
        //!bc.display();
        //!System.out.println();
              return bc;
            }
          }
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
    
        int l = bc.args.length;
        Allocator[] allocs = null;
        for (int i=0; i<l; i++) {
          Tail t = bc.args[i].lookupFact(facts);
          if (t!=null) {
            Allocator alloc = t.isAllocator(); // we're only going to keep info about Allocators
            if (alloc!=null) {
              if (allocs==null) {
                allocs = new Allocator[l];
              }
              allocs[i] = alloc;
            }
          }
        }
        this.allox = allocs; // TODO: temporary, for inspection of results.
        if (allocs!=null) {
          BlockCall bc1 = bc.deriveWithKnownCons(allocs);
          if (bc1!=null) {
            bc = bc1;
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
    public void analyzeCalls() { b.called(); }

    /** Compute the set of live variables that appear in this Tail, adding
     *  each one to the list that is passed in as a parameter if it is not
     *  already included.
     */
    Vars liveVars(Vars vs) {
        return Vars.add(b.getLiveVars(), vs);
    }

    /** Rewrite trailing block calls (i.e., block calls at the end of code
     *  sequences) to use the lists of arguments that have previously been
     *  computed by the live variables analysis.
     */
    void fixTrailingBlockCalls() {
        if (args!=null) {
            debug.Internal.error("tail block call with non null arguments");
        }
        withArgs(b.formalsToArgs());
    }
    
    boolean callsBlock(String id) {
    	if (b == null) return false;
    	return b.getId().equalsIgnoreCase(id);
    }
}
