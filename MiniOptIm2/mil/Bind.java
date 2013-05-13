package mil;

/** Represents a code sequence that binds the variable v to the result
 *  produced by running t and then continues by executing the code in c.
 */
public class Bind extends Code {

    /** The variable that will capture the result.
     */
    private Var v;

    /** The tail whose result will be stored in v.
     */
    private Tail t;

    /** The rest of the code sequence.
     */
    private Code c;

    /** Default constructor.
     */
    public Bind(Var v, Tail t, Code c) {
        this.v = v;
        this.t = t;
        this.c = c;
    }

    /** Test for a free occurrence of a particular variable.
     */
    public boolean contains(Var w) { return t.contains(w) || (v!=w && c.contains(w)); }

    /** Find the list of Defns that this Code sequence depends on.
     */
    public Defns dependencies(Defns ds) {  // v <- t; c
        return t.dependencies(c.dependencies(ds));
    }

    /** Display a printable representation of this MIL construct
     *  on the standard output.
     */
    public void display() {
        indent();
  // TODO: hiding the _ <- part of a bind seems mostly like a good thing to do,
  //       but it does look a bit confusing when the right hand side expression
  //       is a return ... revisit this, but for now I'm keeping the _ <- part.
  //    if (v!=Wildcard.obj) {
        System.out.print(v.toString());
        System.out.print(" <- ");
  //    }
        t.displayln();
        c.display();
    }

    /** Apply an AtomSubst to this Code.
     */
    public Code forceApply(AtomSubst s) {    // v <- t; c
        Temp w = new Temp();
        return new Bind(w, t.forceApply(s), c.forceApply(new AtomSubst(v, w, s)));
    }

    public Val eval(ValEnv env)
      throws Fail { return c.eval(new ValEnv(v, t.eval(env), env)); }

    /** Simplify uses of constructor functions in this code sequence.
     */
    Code cfunSimplify() {
        t = t.removeNewtypeCfun();
        c = c.cfunSimplify();
        return this;
    }

    /** Modify this code sequence to add a trailing invoke operation.
     */
    Code deriveWithInvoke() {
        return new Bind(v, t, c.deriveWithInvoke());
    }

    /** Given an expression of the form (w <- b(..); c), attempt to construct an
     *  equivalent code sequence that instead invokes a block whose code includes
     *  a trailing invoke.
     */
    public Code invokes(Var w, BlockCall bc) {
        return (t.invokes(w) && !c.contains(w))
                  ? new Bind(v, bc.deriveWithInvoke(), c) : null;
    }

    /** Modify this code sequence to add a trailing enter operation that
     *  applies the value that would have been returned by the code in
     *  the original block to the specified argument parameter.
     */
    Code deriveWithEnter(Atom arg) {
        return new Bind(v, t, c.deriveWithEnter(arg));
    }

    /** Given an expression of the form (w <- b(..); c), attempt to construct an
     *  equivalent code sequence that instead calls a block whose code includes a
     *  trailing enter.
     */
    public Code enters(Var w, BlockCall bc) {
        Atom arg = t.enters(w);
        return (arg!=null && !c.contains(w))
           ? new Bind(v, bc.deriveWithEnter(arg), c) : null;
    }

    /** Modify this code sequence to add a trailing enter operation that
     *  passes the value that would have been returned by the code in
     *  the original block to the specified continuation parameter.
     */
    Code deriveWithCont(Atom cont) {
        return new Bind(v, t, c.deriveWithCont(cont));
    }

    /** Given an expression of the form (w <- b(..); c), attempt to construct an
     *  equivalent code sequence that instead calls a block whose code will attempt
     *  to call a continuation on the result instead of returning it directly.
     */
    public Code appliesTo(Var w, BlockCall bc) {
        Atom cont = t.appliesTo(w);
        return (cont!=null && !c.contains(w))
            ? new Bind(v, bc.deriveWithCont(cont), c) : null;
    }

    Code copy() { return new Bind(v, t, c.copy()); }

    boolean detectLoops(Block src, Blocks visited) {     // look for src(x) = b(x) or src(x) = (v <- b(x); ...)
        return t.detectLoops(src, visited);
      }

    /** Perform inlining on this Code value, decrementing the limit each time
     *  a successful inlining is performed, and declining to pursue further
     *  inlining at this node once the limit reachs zero.
     */
    Code inlining(Block src, int limit) {
        //*
            if (v==Wildcard.obj && t.isPure()) {
              // Rewrite an expression (_ <- p; c) ==> c, if p is pure
              MILProgram.report("inlining eliminated a wildcard binding in " + src.getId());
              return c.inlining(src, INLINE_ITER_LIMIT);
            } else if (c.isReturn(v)) {
              // Rewrite an expression (v <- t; return v) ==> t
              MILProgram.report("applied right monad law in " + src.getId());
              return new Done(t).inlining(src, INLINE_ITER_LIMIT);
            } else if (t.loops()) {
              // Rewrite an expression (v <- loop(); c) ==> loop()
              MILProgram.report("removed code after a call to loop in " + src.getId());
              return new Done(t).inlining(src, INLINE_ITER_LIMIT);
            }
        //*/
            if (limit>0) {  // Is this an opportunity for prefix inlining?
              Code ic = t.prefixInline(src, v, c);
              if (ic!=null) {
                return ic.inlining(src, limit-1);
              }
            }
            c = c.inlining(src, INLINE_ITER_LIMIT);
        
            // Rewrite an expression (v <- b(x,..); invoke v) ==> b'(x,..)
            //                    or (v <- b(x,..); v @ a)    ==> b'(x,..,a)
            BlockCall bc = t.isBlockCall();
            if (bc!=null) {
              Code nc;
              if ((nc = c.invokes(v, bc))!=null) {
                MILProgram.report("pushed invoke into call in " + src.getId());
                return nc;
              }
              if ((nc = c.enters(v, bc))!=null) {
                MILProgram.report("pushed enter into call in " + src.getId());
        //!System.out.println("Transformed code:");
        //!this.display();
        //!System.out.println();
        //!System.out.println("Transformed code:");
        //!nc.display();
        //!System.out.println();
                return nc;
              }
              if ((nc = c.casesOn(v, bc))!=null) {
        //!System.out.println("casesOn for:");
        //!this.display();
                MILProgram.report("pushed case into call in " + src.getId());
        //!System.out.println("New code is:");
        //!nc.display();
        //!System.out.println();
                return nc;
              }
              if ((nc = c.appliesTo(v, bc))!=null) {
        //! IN PROGRESS:
        //!System.out.println("We could have used deriveWithCont here!");
        //!display();
              }
        
            }
            return this;
          }

    Code prefixInline(AtomSubst s, Var u, Code d) {
        Var w = new Temp();
        return new Bind(w, t.apply(s), c.prefixInline(new AtomSubst(v, w, s), u, d));
      }

    int prefixInlineLength(int len) { return c.prefixInlineLength(len+1); }

    /** Compute the length of this Code sequence for the purposes of prefix inlining.
     *  The returned value is either the length of the code sequence (counting
     *  one for each Bind and Done node) or 0 if the code sequence ends with
     *  something other than Done.  Argument should be initialized to 0 for
     *  first call.
     */
    int suffixInlineLength(int len) { return c.suffixInlineLength(len+1); }

    void liftAllocators() {
        if (t.isAllocator()!=null) {
          // This bind uses an allocator, so we can only look for lifting opportunities in
          // the rest of the code.
          c.liftAllocators();
        } else {
          // This bind does not have an allocator, but it could be used as a non-allocator
          // parent for the following code ... which might turn this node into an allocator,
          // prompting the need to repeat the call to this.liftAllocators().
          // 
          if (c.liftAllocators(this)) {
            this.liftAllocators();
          }
        }
      }

    boolean liftAllocators(Bind parent) {
        if (t.isAllocator()!=null) {
          // This bind uses an allocator, so it can be swapped with the parent Bind,
          // if that is safe.
          if (this.v!=parent.v
              && !this.t.contains(parent.v)
              && !parent.t.contains(this.v)) {
            Var  tempv = parent.v; parent.v = this.v; this.v = tempv;  // swap vars
            Tail tempt = parent.t; parent.t = this.t; this.t = tempt;  // swap tails
            MILProgram.report("lifted allocator for " + parent.v);
            c.liftAllocators(this); // Now this node is a non-allocator parent of c.
            return true;
          }
    
          // We can't change this Bind, but can still scan the rest of the code:
          c.liftAllocators();
          return false;
        } else {
          // This bind does not have an allocator, but it could be used as a non-allocator
          // parent for the following code ... if that changes this node, then we might
          // have a second opportunity to rewrite this node, hence the tail call:
          return c.liftAllocators(this) && this.liftAllocators(parent);
        }
      }

    /** Find the list of variables that are used in this code sequence.  Variables
     *  that are mentioned in BlockCalls, ClosAllocs, or CompAllocs are only
     *  included if the corresponding flag in usedArgs is set.
     */
    Vars usedVars() { return t.usedVars(Vars.remove(v, c.usedVars())); }

    /** Rewrite this program to remove unused arguments in block calls.
     */
    void removeUnusedArgs() {
        t.removeUnusedArgs();
        c.removeUnusedArgs();
    }

    /** Optimize a Code block using a simple flow analysis.
     */
    public Code flow(Facts facts, AtomSubst s) {
        t = t.apply(s);                 // Update tail to reflect substitution
        s = AtomSubst.remove(v, s);     // Update substitution
    
        // Common subexpression elimination:
        // TODO: do we need to limit the places where this is used?
        Var p = Facts.find(t, facts);   // Look for previously computed value
        if (p!=null) {
          MILProgram.report("cse: using previously computed value "+p+" for "+v);
          return c.flow(facts, new AtomSubst(v, p, s));
        }
    
        // Apply left monad law: (v <- return a; c) == [a/v]c
        Atom a  = t.returnsAtom();      // Check for v <- return a; c
        if (a!=null) {
          MILProgram.report("applied left monad law for "+v+" <- return "+a);
    //!System.out.println("Skipping a return");
          return c.flow(facts, new AtomSubst(v, a, s));
        }
    
        // Look for opportunities to rewrite this tail, perhaps using previous results
    //!System.out.print("Looking for ways to rewrite tail "); t.display(); System.out.println();
        Code nc = t.rewrite(facts); // Look for ways to rewrite the tail
    //!System.out.println("In Bind, result was null: " + (nc==null));
        if (nc!=null) {
    //!System.out.println("Rewriting a tail");
    //!nc.display();
    //!System.out.println("Expanded");
    //!Code nc1 = nc.andThen(v, c);
    //!nc1.display();
    //!return nc1.flow(facts, s);
          return nc.andThen(v, c).flow(facts, s);
        }
    
        // Propagate analysis to the following code, updating facts as necessary.
    //!System.out.println("Propagating analysis");
        c = c.flow(t.addFact(v, Facts.kills(v, facts)), s);
        return this;
      }

    public Code andThen(Var v, Code rest) { c = c.andThen(v, rest); return this; }

    /** Live variable analysis on a section of code; rewrites bindings v <- t using
     *  a wildcard, _ <- t, if the variable v is not used in the following code.
     */
    Vars liveness() {
        Vars vs = c.liveness();
        // Stub out this variable with a wildcard 
        if (v!=Wildcard.obj && !Vars.isIn(v,vs)) {
          MILProgram.report("liveness replaced " + v + " with a wildcard");
          v = Wildcard.obj;
        }
        // Return the set of variables that are used in (v <-t; c)
        Vars ws = Vars.remove(v,vs);  // find variables in rest of code
        vs      = t.liveness(ws);     // add variables mentioned only in t
        return vs;
      }

    public void analyzeCalls() { t.analyzeCalls(); c.analyzeCalls(); }

    /** Compute an integer summary for a fragment of MIL code with the key property
     *  that alpha equivalent program fragments have the same summary value.
     */
    int summary() { return t.summary() * 17 + c.summary() * 11 + 511; }

    /** Test to see if two Code sequences are alpha equivalent.
     */
    boolean alphaCode(Vars thisvars, Code that, Vars thatvars) { return that.alphaBind(thatvars, this, thisvars); }

    /** Test two items for alpha equivalence.
     */
    boolean alphaBind(Vars thisvars, Bind that, Vars thatvars) {
        return this.t.alphaTail(thisvars, that.t, thatvars)
               && this.c.alphaCode(new Vars(this.v, thisvars), that.c, new Vars(that.v, thatvars));
      }

    void eliminateDuplicates() { t.eliminateDuplicates(); c.eliminateDuplicates(); }

    /** Compute the set of live variables in this code sequence.
     */
    Vars liveVars() {
        return t.liveVars(Vars.remove(v, c.liveVars()));
    }

    /** Rewrite trailing block calls (i.e., block calls at the end of code
     *  sequences) to use the lists of arguments that have previously been
     *  computed by the live variables analysis.
     */
    void fixTrailingBlockCalls() {
        c.fixTrailingBlockCalls();
    }

    /** getBlockCall 
     *  @param id The id of the Block which you want block calls to.
     *    @return a list of BlockCall objects which call id
     *
     *  If the tail of this object is a Block call, compare if it calls the passed in id
     *  calls getBlockCall on the following code c.
     *if the tail calls block id, cons the tail to the list returned from c.getBlockCall
     *
     */
    public BlockCalls getBlockCall(String id) {
        BlockCall thisCall = null;
        BlockCall bc = t.isBlockCall();
        if (bc instanceof BlockCall)
        {
            if (bc.callsBlock(id)) {
                thisCall = bc;
            }
        }
        BlockCalls calls = c.getBlockCall(id);
        if (thisCall != null)
            calls = new BlockCalls(thisCall, calls);
        
        return calls;
    }

    /** checkformals
     * @param atoms A list of formals to compare to the variable "assigned" to with a Bind
     * If incoming argument is given a new value with a call to Bind.
     *Currently, if Bind is called on an incoming parameter the argument is set to NAC
     *
     */
    public Atom[] checkformals(Atom[] atoms) { for(int i = 0; i < atoms.length; ++i) {
        if (v.sameAtom(atoms[i])) {
            atoms[i] = NAC.obj;
        }
    }
    return c.checkformals(atoms);    
}

    /** replaceCalls
     * @param id: the id of the block call which has been specialized
     * @param j:  the argument number which has been removed from block id
     *@param replaced: either the Const object which was removed, or for the case of a recursive call
     *the Var object which was removed
     *@param b: the new Block object which was specialized from id
     */
    boolean replaceCalls(String id, int j, Atom replaced, Block b) {
        Boolean success = false;
        BlockCall thisCall = null;
        BlockCall bc = t.isBlockCall();
        if (bc != null) {
            if (bc.callsBlock(id)) {
                thisCall = bc;
                if (thisCall.args[j].sameAtom(replaced)) {
                    
                    BlockCall temp = new BlockCall(b);
                    int l = bc.args.length-1;
                    temp.args = new Atom[l];
                    for (int i = 0; i < l; ++i) {
                        if (i >= j) {
                            temp.args[i] = bc.args[i+1];
                        }
                        else
                            temp.args[i] = bc.args[i];
                    }
                    t = temp;
                    success = true;
                }
            }
        }
        
        return c.replaceCalls(id, j, replaced, b) || success;
    }

    public Pairs outset(Pairs ins) {
        Pairs outs = null;
        Pair d = new Pair(t, new Atoms(v, null));
        if (ins == null) {
                outs = new Pairs(d, null);
        }
        else {
                outs = t.addIns(ins);
                outs = Pairs.meets(outs, ins, true);
                outs.kill(v);
                outs.gen(d);
        }
if (c == null) {
        System.out.println("unlinked bind call found!?!");
        return outs;
}
        return c.outset(outs);
        }
}
