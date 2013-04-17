package mil;


/** Represents a code sequence that just executes a single Tail.
 */
public class Done extends Code {

    /** The tail to be executed.
     */
    private Tail t;

    /** Default constructor.
     */
    public Done(Tail t) {
        this.t = t;
    }

    /** Test for a free occurrence of a particular variable.
     */
    public boolean contains(Var w) { return t.contains(w); }

    /** Find the list of Defns that this Code sequence depends on.
     */
    public Defns dependencies(Defns ds) {  // t
        return t.dependencies(ds);
    }

    /** Display a printable representation of this MIL construct
     *  on the standard output.
     */
    public void display() {
        indent();
        t.displayln();
    }

    /** Apply an AtomSubst to this Code.
     */
    public Code forceApply(AtomSubst s) {    // t
        return new Done(t.forceApply(s));
    }
    public Val eval(ValEnv env)
      throws Fail { return t.eval(env); }
    Code cfunSimplify() {
        t = t.removeNewtypeCfun();
        return this;
      }
    boolean detectLoops(Block src, Blocks visited) {     // look for src(x) = b(x) or src(x) = (v <- b(x); ...)
        return t.detectLoops(src, visited);
      }

    /** Test whether a given Code/Tail value is an expression of the form return v,
     *  with the specified variable v as its parameter.  We also return a true result
     *  for a Tail of the form return _, where the wildcard indicates that any
     *  return value is acceptable because the result will be ignored by the caller.
     *  This allows us to turn more calls in to tail calls when they occur at the end
     *  of "void functions" that do not return a useful result.
     */
    boolean isReturn(Var v) { return t.isReturn(v); }

    /** Perform inlining on this Code sequence, looking for opportunities
     *  to: inline BlockCalls in both Bind and Done nodes; to skip goto
     *  blocks referenced from Match nodes; and to apply the right monad
     *  law by rewriting Code of the form (v <- t; return v) as t.
     *  As a special case, we do not apply inlining to the code of a block
     *  that contains a single Tail; any calls to that block should be
     *  inlined anyway, at which point the block will become deadcode.
     *  In addition, expanding a single tail block like this could lead
     *  to significant code duplication, not least because we also have
     *  another transformation (toBlockCall) that turns an expanded code
     *  sequence back in to a single block call, and we want to avoid
     *  oscillation back and forth between these two forms.
     *  
     *  TODO: Alas, the special treatment of TopLevel values doesn't work.
     *  A code sequence of the form (_ <- return 0; b()) for a top-level
     *  value can trick the inliner into firing (because it's not a single
     *  Done value), expanding the code for b() in place.  Forcing every
     *  TopLevel to be a BlockCall doesn't work either because then the
     *  code that inlines enters to top-level closures won't notice that
     *  the top-level value is indeed a closure (because it goes through
     *  an unnecessary block).  This could probably be handled by a special
     *  case that routes through the block to see if a simple closure can be
     *  found ...
     */
    Code inlining(Block src) { return this; }

    /** Perform inlining on this Code value, decrementing the limit each time
     *  a successful inlining is performed, and declining to pursue further
     *  inlining at this node once the limit reachs zero.
     */
    Code inlining(Block src, int limit) {
        //!System.out.print("Got down to Done (limit "+limit+"): ");
        //!t.display();
        //!System.out.println();
            if (limit>0) {  // Is this an opportunity for suffix inlining?
              Code ic = t.suffixInline(src);
              if (ic!=null) {
        //!System.out.println("Immediate result of suffix inlining was:");
        //!ic.display();
                return ic.inlining(src, limit-1);
              }
        //!System.out.println("could not inline here");
            }
            return this;
          }
    Code prefixInline(AtomSubst s, Var u, Code d) {
        return new Bind(u, t.apply(s), d);
      }
    int prefixInlineLength(int len) { return len+1; }

    /** Compute the length of this Code sequence for the purposes of prefix inlining.
     *  The returned value is either the length of the code sequence (counting
     *  one for each Bind and Done node) or 0 if the code sequence ends with
     *  something other than Done.  Argument should be initialized to 0 for
     *  first call.
     */
    int suffixInlineLength(int len) { return len+1; }

    /** Test to determine whether this Code is a Done.
     */
    public Tail isDone() { return t; }

    /** Test to determine whether this Code/Tail in a specified src Block
     *  constitutes a goto block.  For this to be valid, the code must be
     *  an immediate BlockCall to a different block.
     */
    public BlockCall isGotoBlockCode(Block src) { return t.isGotoBlockCode(src); }

    /** Modify this code sequence to add a trailing invoke operation.
     */
    Code deriveWithInvoke() {
        Var v = new Temp();
        return new Bind(v, t, new Done(new Invoke(v)));
      }

    /** Given an expression of the form (w <- b(..); c), attempt to construct an
     *  equivalent code sequence that instead invokes a block whose code includes
     *  a trailing invoke.
     */
    public Code invokes(Var w, BlockCall bc) { return t.invokes(w)
        ? new Done(bc.deriveWithInvoke()) : null; }

    /** Modify this code sequence to add a trailing enter operation that
     *  applies the value that would have been returned by the code in
     *  the original block to the specified argument parameter.
     */
    Code deriveWithEnter(Atom arg) {
        Var f = new Temp();
        return new Bind(f, t, new Done(new Enter(f,arg)));
      }

    /** Given an expression of the form (w <- b(..); c), attempt to construct an
     *  equivalent code sequence that instead calls a block whose code includes a
     *  trailing enter.
     */
    public Code enters(Var w, BlockCall bc) {
        Atom arg = t.enters(w);
    //!if (arg!=null) {
    //!System.out.println("Found an argument " + arg);
    //!System.out.print("Block call: ");
    //!bc.display();
    //!System.out.println();
    //!}
        return (arg!=null) ? new Done(bc.deriveWithEnter(arg)) : null;
      }

    /** Modify this code sequence to add a trailing enter operation that
     *  applies the value that would have been returned by the code in
     *  the original block to the specified argument parameter.
     */
    Code deriveWithCont(Atom cont) {
        //!System.out.println("deriveWithCont: Done");
            Var v = new Temp();
            return new Bind(v, t, new Done(new Enter(cont,v)));
          }

    /** Given an expression of the form (w <- b(..); c), attempt to construct an
     *  equivalent code sequence that instead calls a block whose code will attempt
     *  to call a continuation on the result instead of returning it directly.
     */
    public Code appliesTo(Var w, BlockCall bc) {
        Atom cont = t.appliesTo(w);
    //!if (arg!=null) {
    //!System.out.println("Found a continuation " + cont);
    //!System.out.print("Block call: ");
    //!bc.display();
    //!System.out.println();
    //!}
    
        // TODO: replace this with the commented section below to make this live ...
        return (cont!=null) ? this /* new Done(bc.deriveWithCont(cont)) */ : null;
      }
    Code copy() { return new Done(t); }

    /** Optimize a Code block using a simple dataflow analysis to track which variables
     *  have been defined using a binding (v <- return a) and which have been defined
     *  usi
     */
    public Code flow(Facts facts, AtomSubst s) {
        t = t.apply(s);
   //!System.out.println("At Done, before rewrite");
        Code nc = t.rewrite(facts);
   //!System.out.println("At Done, with code");
   //!if (nc==null) {
   //!  System.out.print("  null, so keeping "); t.display(); System.out.println();
   //!} else {
   //!  nc.display();
   //!}
        return (nc==null) ? this : nc.flow(facts, s);
     }
    public Code andThen(Var v, Code rest) { return new Bind(v, t, rest); }

    /** Live variable analysis on a section of code; rewrites bindings v <- t using
     *  a wildcard, _ <- t, if the variable v is not used in the following code.
     */
    Vars liveness() { return t.liveness(null); }

    /** Test to determine whether this Code/Tail value corresponds to a
     *  closure allocator, returning either a ClosAlloc value, or else
     *  a null result.
     */
    ClosAlloc lookForClosAlloc() { return t.lookForClosAlloc(); }

    /** Test to see if this Code/Tail is a CompAlloc (that is, if it has
     *  the form m[x..] for some m and x..).
     */
    CompAlloc lookForCompAlloc() { return t.lookForCompAlloc(); }
    public void analyzeCalls() { t.analyzeTailCalls(); }

    /** Compute the set of live variables in this code sequence.
     */
    Vars liveVars() {
        return t.liveVars(null);
    }

    /** Rewrite trailing block calls (i.e., block calls at the end of code
     *  sequences) to use the lists of arguments that have previously been
     *  computed by the live variables analysis.
     */
    void fixTrailingBlockCalls() {
        t.fixTrailingBlockCalls();
    }

	@Override
	public BlockCalls getBlockCall(String id) {
		BlockCall thisCall = null;
		BlockCall bc = t.isBlockCall();
		if (bc != null)
		{
			// TODO is this ever needed
			if (bc.callsBlock(id)) {
				thisCall = bc;
			}
		}
		BlockCalls calls = null;
		if (thisCall != null)
			calls = new BlockCalls(thisCall, calls);
		
		return calls;
	}



	@Override
	void replaceCalls(String id, int j, Atom replaced, Block b) {
		BlockCall thisCall = null;
		BlockCall bc = t.isBlockCall();
		if (bc != null)
		{
			if (bc.callsBlock(id)) {
				thisCall = bc;
				if (thisCall.args[j].sameAtom(replaced)) {
					
					BlockCall temp = new BlockCall(b);
					int l = bc.args.length-1;
					temp.args = new Atom[l];
				    for (int i = 0; i < l; ++i) {
				    	if (i >= j)
				    	{
				    		temp.args[i] = bc.args[i+1];
				    	}
				    	else
				    		temp.args[i] = bc.args[i];
				    }
				    t = temp;
				}
			}
			
		}
	}
}
