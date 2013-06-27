package mil;

/** Represents a code sequence that implements a conditional jump, using the value
 *  in a to determine which of the various alternatives in alts should be used, or
 *  taking the default branch, def, is there is no matching alternative.
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

    /** Simplify uses of constructor functions in this code sequence.
     */
    Code cfunSimplify() {
        // If there are no alternatives, replace this Match with a Done:
        if (alts.length==0) { // no alternatives; use default
            return (def==null) ? Code.halt : new Done(def);
        }
   
        // Determine which constructor numbers are covered by alts:
        boolean[] used = null;
        for (int i=0; i<alts.length; i++) {
            used = alts[i].cfunsUsed(used);
        }
  
        // Count to see if all constructor numbers are used:
        int count   = 0;
        int notused = 0;
        for (int i=0; i<used.length; i++) {
            if (used[i])
                count++;      // count this constructor as being used
            else
                notused = i;  // record index of an unused constructor
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
        } else if (count==used.length-1 && def!=null) {
            // Promote a default to a regular alternative for better flow results:
            alts = TAlt.extendAlts(alts, notused, def); // Add new alternative
            def  = null;                                // Eliminate default
        }
        return this;
    }

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
     *  passes the value that would have been returned by the code in
     *  the original block to the specified continuation parameter.
     */
    Code deriveWithCont(Atom cont) {
        TAlt[] nalts = new TAlt[alts.length];
        for (int i=0; i<alts.length; i++) {
            nalts[i] = alts[i].deriveWithCont(cont);
        }
        BlockCall d = (def==null) ? null : def.deriveWithCont(cont);
        return new Match(a, nalts, d);
    }

    /** Test to determine if this code is an expression of the form case v of alts where v
     *  is the result of a preceding block call.  If so, return a transformed version of
     *  the code that makes use of a newly defined, known continuation that can subsequently
     *  be pushed in to the individual branches of the case for further optimization.
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
            Tail t        = new BlockCall(b).withArgs(formals);
            ClosureDefn k = new ClosureDefn(v, t); // define a new closure
            Var[] stored  = Vars.toArray(Vars.remove(v, vs));
            k.setStored(stored);                   // do not store v in the closure
  //!k.displayDefn();
  
            // Construct a continuation for the derived block:
            Tail cont = new ClosAlloc(k).withArgs(stored);
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

    /** Test for code that is guaranteed not to return.
     */
    boolean doesntReturn() {
        // If the default or any of the alternatives can return,
        // then the Match might also be able to return.
        if (def!=null && !def.doesntReturn()) {
            return false;
        }
        for (int i=0; i<alts.length; i++) {
            if (!alts[i].doesntReturn()) {
                return false;
            }
        }
        return true;
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

    /** Find the list of variables that are used in this code sequence.  Variables
     *  that are mentioned in BlockCalls, ClosAllocs, or CompAllocs are only
     *  included if the corresponding flag in usedArgs is set.
     */
    Vars usedVars() {
        Vars vs = (def==null) ? null : def.usedVars(null);
        for (int i=0; i<alts.length; i++) {
          vs = Vars.add(alts[i].usedVars(), vs);
        }
        return a.add(vs);
      }

    /** Rewrite this program to remove unused arguments in block calls.
     */
    void removeUnusedArgs() {
        if (def!=null) {
            def.removeUnusedArgs();
        }
        for (int i=0; i<alts.length; i++) {
            alts[i].removeUnusedArgs();
        }
    }

    /** Optimize a Code block using a simple flow analysis.
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
              // TODO: is this comment valid?  It might have been written at a time
              // when flow was returning a set of variables, instead of a rewritten
              // Code object, because it is now possible to do a rewrite ...
    /*
              return new Done(bc.rewriteBlockCall(facts));
    */
              alts = new TAlt[0];
              def  = bc.rewriteBlockCall(facts);
              return this;
          } else {
              // If a = bnot((n)), then a match of the form case a of alts can be
              // rewritten as case n of alts', where alts' flips the cases for true
              // and false in the original alternatives, alts.
              Atom n = t.isBnot();
              if (n!=null) {
                a    = n;           // match on the parameter n
                alts = new TAlt[] { // swap alternatives for True and False
                         new TAlt(Cfun.True, Var.noVars, DataAlloc.False.shortMatch(s, alts, def)),
                         new TAlt(Cfun.False, Var.noVars, DataAlloc.True.shortMatch(s, alts, def))
                       };
                def  = null;       // Remove default branch
              }
          }
          // TODO: when we continue on to the following code, possibly after applying the substitution
          // s in the cases above, might we then apply it again in what follows?  (is that a problem?)
        }
    
        // Match will be preserved, but we still need to update using substitution s
        // and to compute the appropriate set of live variables.
        if (def!=null) {                    // update the default branch
          def = def.applyBlockCall(s).rewriteBlockCall(facts);
        }
        Var v = (Var)a;                     // TODO: eliminate this ugly cast!
        // We do not need to kill facts about v here because we are not changing its value
        // facts = Facts.kills(v, facts);
        for (int i=0; i<alts.length; i++) { // update regular branches
          alts[i].flow(v, facts, s);
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

    /** Compute an integer summary for a fragment of MIL code with the key property
     *  that alpha equivalent program fragments have the same summary value.
     */
    int summary() {
        int sum = (def==null) ? 19 : def.summary();
        if (alts!=null) {
            for (int i=0; i<alts.length; i++) {
                sum = sum*13 + alts[i].summary();
            }
        }
        return sum;
     }

    /** Test to see if two Code sequences are alpha equivalent.
     */
    boolean alphaCode(Vars thisvars, Code that, Vars thatvars) { return that.alphaMatch(thatvars, this, thisvars); }

    /** Test two items for alpha equivalence.
     */
    boolean alphaMatch(Vars thisvars, Match that, Vars thatvars) {
        if (!this.a.alphaAtom(thisvars, that.a, thatvars)) { // Compare discriminants:
            return false;
        }
      
        if (this.def==null) {                                // Compare default branches:
            if (that.def!=null) {
                return false;
            }
        } else if (that.def==null || !this.def.alphaTail(thisvars, that.def, thatvars)) {
            return false;
        }
      
        if (this.alts==null) {                               // Compare alternatives:
            return that.alts==null;
        } else if (that.alts==null || this.alts.length!=that.alts.length) {
            return false;
        }
        for (int i=0; i<alts.length; i++) {
            if (!this.alts[i].alphaTAlt(thisvars, that.alts[i], thatvars)) {
                return false;
            }
        }
        return true;
      }

    void eliminateDuplicates() {
        if (alts!=null) {
            for (int i=0; i<alts.length; i++) {
                alts[i].eliminateDuplicates();
            }
        }
        if (def!=null) {
            def.eliminateDuplicates();
        }
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
        BlockCalls block_calls = null;
        if (def!=null) {
            if (def.callsBlock(id)) {
                block_calls = new BlockCalls(def, block_calls);
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

    /** replaceCalls
     * @param id: the id of the block call which has been specialized
     * @param j:  the argument number which has been removed from block id
     *@param replaced: either the Const object which was removed, or for the case of a recursive call
     *the Var object which was removed
     *@param b: the new Block object which was specialized from id
     */
    boolean replaceCalls(String id, int j, Atom replaced, Block b) {
        boolean success = false;
        if (def!=null) {
        // Determine if the default case is a call to block id
            if (def.callsBlock(id)) {

                // Compare to either see if the call to block id can be replaced with a call to block b
                if (def.args[j].sameAtom(replaced)) {
                    
                    BlockCall temp = new BlockCall(b);
                    int l = def.args.length-1;
                    temp.args = new Atom[l];
                    for (int i = 0; i < l; ++i) {
                        if (i >= j) {
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
                if (alt_blockCall.args[j].sameAtom(replaced)) {
                    
                    BlockCall temp = new BlockCall(b);
                    int l = alt_blockCall.args.length-1;
                    temp.args = new Atom[l];
                    for (int i1 = 0; i1 < l; ++i1) {
                        if (i1 >= j) {
                            temp.args[i1] = alt_blockCall.args[i1+1];
                        }
                        else
                            temp.args[i1] = alt_blockCall.args[i1];
                    }
                    alts[i].replaceBc(temp);
                    success = true;
                }
            }
          }
        return success;
    }
}
