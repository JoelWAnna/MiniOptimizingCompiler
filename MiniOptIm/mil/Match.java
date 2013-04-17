package mil;


/** Represents a code sequence that implements a conditional jump,
 *  using the value in a to determine which of the various alternatives
 *  in alts should be used, or taking the default branch, def, is there
 *  is no matching alternative.
 */
public class Match extends Code {

    /** The discriminant for this Match.
     */
    private Atom a;

    /** A list of alternatives for this Match.
     */
    private TAlt[] alts;

    /** A default branch, to be used if none of the
     *  alternatives apply.
     */
    private BlockCall def;

    /** Default constructor.
     */
    public Match(Atom a, TAlt[] alts, BlockCall def) {
        this.a = a;
        this.alts = alts;
        this.def = def;
    }

    /** Test for a free occurrence of a particular variable.
     */
    public boolean contains(Var w) {
        if (a==w || (def!=null && def.contains(w))) {
            return true;
        }
        for (int i=0; i<alts.length; i++) {
            if (alts[i].contains(w)) {
                return true;
            }
        }
        return false;
    }

    /** Find the list of Defns that this Code sequence depends on.
     */
    public Defns dependencies(Defns ds) { // case a of alts; d
        if (def!=null) {
            ds = def.dependencies(ds);
        }
        for (int i=0; i<alts.length; i++) {
            ds = alts[i].dependencies(ds);
        }
        return a.dependencies(ds);
    }

    /** Display a printable representation of this MIL construct
     *  on the standard output.
     */
    public void display() {
        indentln("case " + a + " of");
        for (int i=0; i<alts.length; i++) {
            indent();  // double indent
            indent();
            alts[i].display();
        }
        if (def!=null) {
            indent();  // double indent
            indent();
            System.out.print("_ -> ");
            def.displayln();
        }
    }

    /** Apply an AtomSubst to this Code.
     */
    public Code forceApply(AtomSubst s) {   // case a of alts; d
        TAlt[] talts = new TAlt[alts.length];
        for (int i=0; i<alts.length; i++) {
            talts[i] = alts[i].forceApply(s);
        }
        BlockCall d = (def==null) ? null : def.forceApplyBlockCall(s);
        return new Match(a.apply(s), talts, d);
    }
    public Val eval(ValEnv env)
      throws Fail { return a.lookup(env).match(alts, def, env); }
    Code cfunSimplify() {
        // If there are no alternatives, replace this Match with a Done:
        if (alts.length==0) { // no alternatives; use default
          return (def==null) ? Halt.obj : new Done(def);
        }
     
        // Determine which constructor numbers are covered by alts:
        boolean[] used = null;
        for (int i=0; i<alts.length; i++) {
          used = alts[i].cfunsUsed(used);
        }
    
        // Count to see if all constructor numbers are used:
        int count = 0;
        for (int i=0; i<used.length; i++) {
          if (used[i]) count++;
        }
    
        // If all constructor numbers have been listed, then we can eliminate
        // the default case and look for a possible newtype match:
        if (count==used.length) {
          if (count==1) {  // Look for a newtype alternative:
            Tail t = alts[0].isNewtypeAlt(a);
            if (t!=null) {
              return new Done(t);
            }
          }
          def = null;     // Eliminate the default case
        }
        return this;
      }

    /** Perform inlining on this Code value, decrementing the limit each time
     *  a successful inlining is performed, and declining to pursue further
     *  inlining at this node once the limit reachs zero.
     */
    Code inlining(Block src, int limit) {
        for (int i=0; i<alts.length; i++) {
          alts[i].inlineAlt();
        }
        if (def!=null) {
          def = def.inlineBlockCall();
        }
        // If the Match has no alternatives, then we can use the default directly.
        return (alts.length==0) ? new Done(def).inlining(src, INLINE_ITER_LIMIT) : this;
        // TODO: is it appropriate to apply inlining to the def above?  If there is useful
        // work to be done, we'll catch it next time anyway ... won't we ... ?
      }

    /** Compute the length of this Code sequence for the purposes of prefix inlining.
     *  The returned value is either the length of the code sequence (counting
     *  one for each Bind and Done node) or 0 if the code sequence ends with
     *  something other than Done.  Argument should be initialized to 0 for
     *  first call.
     */
    int suffixInlineLength(int len) { return len+1; }

    /** Modify this code sequence to add a trailing invoke operation.
     */
    Code deriveWithInvoke() {
        TAlt[] nalts = new TAlt[alts.length];
        for (int i=0; i<alts.length; i++) {
          nalts[i] = alts[i].deriveWithInvoke();
        }
        BlockCall d = (def==null) ? null : def.deriveWithInvoke();
        return new Match(a, nalts, d);
      }

    /** Modify this code sequence to add a trailing enter operation that
     *  applies the value that would have been returned by the code in
     *  the original block to the specified argument parameter.
     */
    Code deriveWithEnter(Atom arg) {
        TAlt[] nalts = new TAlt[alts.length];
        for (int i=0; i<alts.length; i++) {
          nalts[i] = alts[i].deriveWithEnter(arg);
        }
        BlockCall d = (def==null) ? null : def.deriveWithEnter(arg);
        return new Match(a, nalts, d);
      }

    /** Modify this code sequence to add a trailing enter operation that
     *  applies the value that would have been returned by the code in
     *  the original block to the specified argument parameter.
     */
    Code deriveWithCont(Atom cont) {
        //!System.out.println("deriveWithCont: Match");
            TAlt[] nalts = new TAlt[alts.length];
            for (int i=0; i<alts.length; i++) {
              nalts[i] = alts[i].deriveWithCont(cont);
            }
            BlockCall d = (def==null) ? null : def.deriveWithCont(cont);
            return new Match(a, nalts, d);
          }

    /** Test to determine if this code is an expression of the form @case v of ...@,
     *  where v is the result of a preceding block call @b(...)@.  In this case, we will
     *  replace the original code sequence with @(f <- k{...}; b'(..., f))@, where
     *  where @k{...} v = case v of ...@ is a known continuation and @b'(..., f)@ is
     *  derived from @b(...)@ with the addition of a continuation parameter.  In making
     *  this transformation and introducing (rather than eliminating) a ClosAlloc,
     *  we are relying on the assumption that a subsequent deriveWithKnownCons will
     *  derive a modified version of @b'@ that is specialized to the continuation
     *  @k{...}@, ultimately allowing us to eliminate the closure construction.  For
     *  this to work well, we limit the use of this transformation to cases where the
     *  definition of block @b(...)@ satisfies a syntactic check described by the
     *  contCand() function described below.
     */
    public Code casesOn(Var v, BlockCall bc) {
        if (a==v && bc.contCand()) {
    //!System.out.println("We could have used a cases optimization here ... !");
    //!display();
          // Build a block:  b(...) = case v of ...
          Code  match   = copy();                // make a copy of this Match
          Vars  vs      = match.liveness();      // find the free variables
          Block b       = new Block(match);
          Var[] formals = Vars.toArray(vs);      // set the formal parameters
          b.setFormals(formals);
    //!b.displayDefn();
    
          // Build a closure definition: k{...} v = b(...)
          Tail t        = new BlockCall(b).withArgs(Atom.fromVars(formals));
          ClosureDefn k = new ClosureDefn(v, t); // define a new closure
          Var[] stored  = Vars.toArray(Vars.remove(v, vs));
          k.setStored(stored);                   // do not store v in the closure
    //!k.displayDefn();
    
          // Construct a continuation for the derived block:
          Tail cont = new ClosAlloc(k).withArgs(Atom.fromVars(stored));
          Var  w    = new Temp();
    //!System.out.print("The continuation is ");
    //!cont.display();
    //!System.out.println();
    
          // Replace original code with call to a new derived block:
          return new Bind(w, cont, new Done(bc.deriveWithCont(w)));
        }
        return null;
      }
    Code copy() { return new Match(a, TAlt.copy(alts), def); }

    /** Optimize a Code block using a simple dataflow analysis to track which variables
     *  have been defined using a binding (v <- return a) and which have been defined
     *  usi
     */
    public Code flow(Facts facts, AtomSubst s) { // case a of alts; d
        // Look for an opportunity to short this Match
        a = a.apply(s);
        Tail t = a.lookupFact(facts);
        if (t!=null) {
          BlockCall bc = t.shortMatch(s, alts, def);
          if (bc!=null) {
            // We can't change the overall structure of the Code object that we're
            // traversing inside flow(), but we can modify this Match to eliminate
            // the alternatives and replace the default with the direct BlockCall.
            alts = new TAlt[0];
            def  = bc;
          }
        }
    
        // Match will be preserved, but we still need to update using substitution s
        // and to compute the appropriate set of live variables.
        if (def!=null) {                    // update the default branch
          def = def.applyBlockCall(s).rewriteBlockCall(facts);
        }
        for (int i=0; i<alts.length; i++) { // update regular branches
          alts[i].flow(facts, s);
        }
        return this;
      }
    public Code andThen(Var v, Code rest) {
        debug.Internal.error("append requires straightline code this code object");
        return this;
      }

    /** Live variable analysis on a section of code; rewrites bindings v <- t using
     *  a wildcard, _ <- t, if the variable v is not used in the following code.
     */
    Vars liveness() {
        Vars vs = (def==null) ? null : def.liveness(null);
        for (int i=0; i<alts.length; i++) {
          vs = Vars.add(alts[i].liveness(), vs);
        }
        a = a.shortTopLevel();
        return a.add(vs);
      }

    /** Test to see if this code is Match that can be shorted out. Even
     *  If we find a Match, we still need to check for a relevant item
     *  in the set of Facts (after applying a subsitution that
     *  captures the result of entering the block that starts with the
     *  Match).  Again, if it turns out that the optimization cannot be
     *  used, then we return null.
     */
    BlockCall shortMatch(Var[] formals, Atom[] args, Facts facts) {
        AtomSubst s = AtomSubst.extend(formals, args, null);
        Tail      t = a.apply(s).lookupFact(facts);
        return (t==null) ? null : t.shortMatch(s, alts, def);
      }
    public void analyzeCalls() { /* a Match can only contain tail calls */ }

    /** Compute the set of live variables in this code sequence.
     */
    Vars liveVars() {
        Vars vs = (def==null) ? null : def.liveVars(null);
        for (int i=0; i<alts.length; i++) {
            vs = Vars.add(alts[i].liveVars(), vs);
        }
        return a.add(vs);
    }

    /** Rewrite trailing block calls (i.e., block calls at the end of code
     *  sequences) to use the lists of arguments that have previously been
     *  computed by the live variables analysis.
     */
    void fixTrailingBlockCalls() {
        if (def!=null) {
            def.fixTrailingBlockCalls();
        }
        for (int i=0; i<alts.length; i++) {
          alts[i].fixTrailingBlockCalls();
        }
    }

	@Override
	public BlockCalls getBlockCall(String id) {
		BlockCalls block_calls = null;
		if (def!=null) {
			if (def.callsBlock(id)) {
				block_calls = new BlockCalls(def, block_calls);
            //	Atom args[] = def.args;
            //	for(Atom a : args)
    		//		System.out.println(a.toString());
			}
		}

        for (int i=0; i<alts.length; i++) {
            BlockCall alt_blockCall = alts[i].getBlockCall(id);
            if (alt_blockCall != null && alt_blockCall.callsBlock(id))
            {
				block_calls = new BlockCalls(alt_blockCall, block_calls);
            }
          }
		return block_calls;
	}
	@Override
	public boolean replaceCalls(String id, int j, Atom replaced, Block b) {
		boolean success = false;
		BlockCalls block_calls = null;
		if (def!=null) {
			if (def.callsBlock(id)) {
				//block_calls = new BlockCalls(def, block_calls);
	            //	Atom args[] = def.args;
	            //	for(Atom a : args)
	    		//		System.out.println(a.toString());
			//	}
				//thisCall = bc;
				if (def.args[j].sameAtom(replaced)) {
					
					BlockCall temp = new BlockCall(b);
					int l = def.args.length-1;
					temp.args = new Atom[l];
				    for (int i = 0; i < l; ++i) {
				    	if (i >= j)
				    	{
				    		temp.args[i] = def.args[i+1];
				    	}
				    	else
				    		temp.args[i] = def.args[i];
				    }
				    def = temp;
				    success = true;
				}
			}

		}

        for (int i=0; i<alts.length; i++) {
            BlockCall alt_blockCall = alts[i].getBlockCall(id);
            if (alt_blockCall != null && alt_blockCall.callsBlock(id)){
				//block_calls = new BlockCalls(def, block_calls);
	            //	Atom args[] = def.args;
	            //	for(Atom a : args)
	    		//		System.out.println(a.toString());
			//	}
				//thisCall = bc;
				if (alt_blockCall.args[j].sameAtom(replaced)) {
					
					BlockCall temp = new BlockCall(b);
					int l = alt_blockCall.args.length-1;
					temp.args = new Atom[l];
				    for (int i1 = 0; i1 < l; ++i1) {
				    	if (i1 >= j)
				    	{
				    		temp.args[i1] = alt_blockCall.args[i1+1];
				    	}
				    	else
				    		temp.args[i1] = alt_blockCall.args[i1];
				    }
				    def = temp;
				    success = true;
				}
			}
          }
        return success;
	}
}
