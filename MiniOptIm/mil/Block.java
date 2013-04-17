package mil;

public class Block extends Defn {
    private Code code;

    /** Default constructor.
     */
    public Block(Code code) {
        this.code = code;
        specialized = "";
    }
    private Block parent = null;
    private Blocks children = null;
    private Atom replacedVar = null;
    private static int count = 0;
    private final String id = "b" + count++;
    private Var[] formals;
	private String specialized;
	private Lattice lattice;
    public void setFormals(Var[] formals) {
        this.formals = formals;
    }

    /** Return the identifier that is associated with this definition.
     */
    public String getId() { return id /*+ specialized*/; }

    /** Find the list of Defns that this Defn depends on.
     */
    public Defns dependencies() {  // b(args) = code
        return code.dependencies(null);
    }
    void displayDefn() {
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
    void cfunSimplify() { code = code.cfunSimplify(); }
    void liftAllocators() { code.liftAllocators(); }
    public void inlining() {
        //!System.out.println("==================================");
        //!System.out.println("Going to try inlining on:");
        //!displayDefn();
        //!System.out.println();
            this.safeToInline = true;   // Defaults to true
            code = code.cleanup(this);
            if (!(code instanceof Done)) {
              code = code.inlining(this);
              // TODO: failing to run inlining on a block will also fail to set the
              // safeToInline flag, which could be a problem until the whole goto block
              // story is cleared up ...
            }
        //!System.out.println("And the result is:");
        //!displayDefn();
        //!System.out.println();
          }
    boolean detectLoops(Blocks visited) {
        // Check to see if this block calls code for an already visited block:
        if (Blocks.isIn(this, visited) || code.detectLoops(this, visited)) {
          MILProgram.report("detected an infinite loop in block " + getId());
          code = new Done(PrimCall.loop);
          return true;
        }
        return false;
      }

    /** We mark a block as safe to inline if it does not end in a tail call
     *  to a block in the same SCC;  A block that fails to satisfy this
     *  property and is called from outside the SCC has the potential to
     *  cause infinite unrolling during suffix inlining.
     */
    private boolean safeToInline;
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

    /** We allow a block to be inlined if the original call is in a different
     *  block, the code for the block ends with a Done, and either there is
     *  only one reference to the block in the whole program, or else the
     *  length of the code sequence is at most INLINE_LINES_LIMIT lines long.
     */
    boolean canSuffixInline(Block src) {
        if (occurs==1 || code.isDone()!=null) {    // Inline single occurrences and trivial
    //!System.out.println("Single occurrence!");
          return true;                             // blocks (safe, as a result of removing loops)
        } else if (!safeToInline) {                // Don't inline previously marked blocks
    //!System.out.println("marked as unsafeToInline!");
          return false;
        } else if (this.getScc()==src.getScc()) {  // But otherwise don't inline
    //!System.out.println("Same SCC!");
          src.safeToInline = false;                // blocks in the same SCC and mark the
          return false;                            // source block as unsafe to inline.
        } else {
          int n = code.suffixInlineLength(0);      // Inline short code blocks
    //!System.out.println("inline length = " + n);
          return n>0 && n<=INLINE_LINES_LIMIT;
        }
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
    private Blocks derived = null;
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
    //!System.out.println("New block:");
    //!b.displayDefn();
        return b;
      }
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
    //!System.out.println("Original block:");
    //!this.displayDefn();
    //!System.out.println("New block:");
    //!b.displayDefn();
        return b;
      }
    boolean contCand() {
        boolean b = getScc().contCand();
    //!System.out.println("Block " + getId() + " is a cont candidate: " + b);
        return b;
      }
    public Block deriveWithKnownCons(Allocator[] allocs) {
        //!System.out.println("Looking for derive with Known Cons ");
        //!Allocator.display(allocs);
        //!System.out.println(" for the Block");
        //!this.display();
        //!System.out.println();
        
            // Do not create a specialized version of a simple block (i.e., a block
            // that contains only a single Done):
            if (code instanceof Done) {
        //!System.out.println("Will not specialize this block: code is a single tail");
              return null;
            }
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
    public void flow() {
        //!System.out.println("FLOW:"); displayDefn();
            code = code.flow(null, null);
            Vars vs = code.liveness();  // Run liveness analysis
            for (int i=0; i<formals.length; i++) {
              if (!Vars.isIn(formals[i], vs)) {
                System.out.println("Unused parameter " + formals[i] + " in block " + id);
                displayDefn();
              }
            }
          }
    public void buildLattice() {
    	//TODO
    	if (formals.length == 0) {
    		System.out.println("Block " + id + " has no vars");
        	
    		return;
    	}
    	System.out.println("reached Block buildLattice of block " + id);
   
    	//lattice = new Lattice(formals);
    	int MAX = 1;
    	boolean unrollLoop = false;
    	Atom lattice[][] = new Atom[formals.length][MAX];
    	for(Defns xs= this.getCallers(); xs != null; xs = xs.next)
    	{
    		Block x = (Block) xs.head;

    		BlockCalls x_calls = x.code.getBlockCall(id);
    		if (x_calls != null)
    		{
    			BlockCalls current_call = x_calls;
    			
    			while (current_call != null) {
    				
    				// Check for calls from the current block
    				if (x.getId().equalsIgnoreCase(id))
    				{
    					//x_calls.head.args;
        				Atom formals2[] = checkFormals();
        				for (int j = 0; j < formals.length; ++j )
        				{
        					if (lattice[j][0] != Var.toomany.TOPLATTICE)
        						
        					{
        						if (!unrollLoop && formals2[j] == Var.toomany.TOPLATTICE)
        							lattice[j][0] = Var.toomany.TOPLATTICE;
        						if ((formals2[j].isConst() != null))
        						{
        							int k;
        						
	        						for (k = 0; k < MAX; ++k)
	        						{
	        							if (lattice[j][k] == null) {
	        								lattice[j][k] = formals2[j];
	        								break;
	        							}
	        							if (formals2[j].sameAtom(lattice[j][k])) {
	        								break;
	        							}
	        						}
	        						if (k == MAX) {
	        							lattice[j][0] = Var.toomany.TOPLATTICE;
	        						}
        						}
        					}
        				}
        			
    					//x_calls.head.display();
        				
        				System.out.println("Block " + id + " called from itself");
        			
        			//Atom a[] modifies;
        			
        			}
    				else {
    					Atom formals2[] =  current_call.head.args;
    					for (int j = 0; j < formals.length; ++j )
        				{
        					if (lattice[j][0] != Var.toomany.TOPLATTICE && (formals2[j].isConst() != null))
        					{
        						int k;
        						for (k = 0; k < MAX; ++k)
        						{
        							if (lattice[j][k] == null) {
        								lattice[j][k] =  formals2[j];
        								break;
        							}
        							if (formals2[j].sameAtom(lattice[j][k])) {
        								break;
        							}
        						}
        						if (k == MAX) {
        							lattice[j][0] = Var.toomany.TOPLATTICE;
        						}
        					}
        				}
    				}
    				current_call = current_call.next;
        		}
    		}
    	}
    		
		System.out.println(id + "Found constants");
		for (int j = 0; j < formals.length; ++j )
		{
			for ( int k = 0; k < MAX; ++k) {
				if (lattice[j][k] != null) {
					Block b = null;
					Blocks currentChild = this.children;
					while (currentChild != null) {
						if (currentChild.head.replacedVar.sameAtom(lattice[j][k]))
						{
							b = currentChild.head;
							break;
						}
						currentChild = currentChild.next;
					}
					if (b == null) {
						b = new Block();
					    derived   = new Blocks(b, derived);
					    int l = formals.length -1;
					    Var[] nfs = new Var[l];
					    for (int i = 0; i < l; ++i) {
					    	if (i >= j)
					    	{
					    		nfs[i] = formals[i+1];
					    	}
					    	else
					    		nfs[i] = formals[i];
					    		
					    }
					    Code bind = new Bind(formals[j], new Return(lattice[j][k]), code);
					    b.code = bind;
					    b.formals = nfs;
					    b.parent = this;
					    b.replacedVar = lattice[j][k];				    
					    
					    children = new Blocks(b, children);
						//defns.
						System.out.println("Created Block " + b.id);
					}
				    b.display();
				    //new BlockCall(b);
				    //BlockCalls foo = 
	
		        	for(Defns xs1= this.getCallers(); xs1 != null; xs1 = xs1.next)
		        	{
		        		Block x1 = (Block) xs1.head;
		        		if (x1.code.replaceCalls(id, j, lattice[j][k], b))
		        		{
		        			//TODO is it necessary to update call(er/ee)s
		        			//b.
		        		}
		        		//BlockCalls x_calls = x.code.getBlockCall(id);
		        		//if (x_calls.)
		        	}
	
		    		
					//System.out.println(lattice[j][k].toString());
				}
				//else
			}
		}
		//x.displayDefn();
		//for (Vars v = x.getLiveVars(); v != null; v = v.next)
		//	System.out.println("Live + " + v.head.toString());
	

    }
    private  Var [] checkFormals() {
    	 Var [] formals2 = new Var[formals.length];
     	for (int i = 0; i < formals.length; ++i)
    			formals2[i] = formals[i];
     	formals2 = code.checkformals(formals2);
    	for (int i = 0; i < formals.length; ++i)
    	{
    		if (formals2[i] == null) break;
    		if (formals2[i] != Var.toomany.TOPLATTICE) {
    			if (formals2[i].isConst() != null &&  !formals2[i].sameAtom(formals[i])) {
    				formals2[i] = Var.toomany.TOPLATTICE;
    			}
    			//if (formals2[i].isVar) {
    			//	System.out.println(formals2[i].isConst());
    			//}
    			//else {
    				
    			//	formals2[i] = Var.empty.EMPTY;
    			//	System.out.println("Argument " + i + " is not a constant, ignoring");
    			//}
    		}
    		else {
    			// TODO
    			System.out.println("Argument " + i + " is modified");
    			
    		}
    	}
    	return formals2;

		
	}

	/** Test to determine whether there is a way to short out a Match
     *  from a call to this block with the specified arguments, and
     *  given the set of facts that have been computed.  We start by
     *  querying the code in the Block to determine if it starts with
     *  a Match; if not, then the optimization will not apply and a null
     *  result is returned.
     */
    BlockCall shortMatch(Atom[] args, Facts facts) { return code.shortMatch(formals, args, facts); }
    public void analyzeCalls() { code.analyzeCalls(); }

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
  //System.out.println("Rec block " + this.getId() + "(" + Vars.toString(liveVars) + ")" + changed);
            return changed;
        } else {
            liveVars = vs;
  //System.out.println("Non rec block " + this.getId() + "(" + Vars.toString(vs) + ")");
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
}
