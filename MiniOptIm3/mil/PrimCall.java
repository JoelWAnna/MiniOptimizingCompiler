package mil;

public class PrimCall extends Call {

    private Prim p;

    /** Default constructor.
     */
    public PrimCall(Prim p) {
        this.p = p;
    }

    /** Test to determine whether a given tail expression is pure, that is,
     *  if it might have an externally visible side effect.
     */
    public boolean isPure() { return p.isPure(); }

    /** Test to see if two Tail expressions are the same.
     */
    public boolean sameTail(Tail that) { return that.samePrimCall(this); }

    boolean samePrimCall(PrimCall that) { return this.p==that.p && this.sameArgs(that); }

    /** Represents a tail expression that halts/terminates the current program.
     */
    public static final Call halt = new PrimCall(Prim.halt).withArgs(new Atom[0]);

    /** Find the list of Defns that this Tail depends on.
     */
    public Defns dependencies(Defns ds) { // p(args)
        return Atom.dependencies(args, ds); // TODO: do we need to register prims?
    }

    /** Display a printable representation of this MIL construct
     *  on the standard output.
     */
    public void display() { display(p.getId(), "((", args, "))"); }

    /** Construct a new Call value that is based on the receiver,
     *  without copying the arguments.
     */
    Call callDup() { return new PrimCall(p);  }

    public Val eval(ValEnv env)
      throws Fail { return p.call(ValEnv.lookup(args, env)); }

    /** Test for code that is guaranteed not to return.
     */
    boolean doesntReturn() { return p.doesntReturn(); }

    public static final Call loop = new PrimCall(Prim.loop).withArgs(new Atom[0]);

    boolean loops() { return p==Prim.loop; }

    Atom isBnot() { return p==Prim.bnot ? args[0] : null; }

    public Code rewrite(Facts facts) {
        return this.rewritePrimCall(facts);
      }

    Code rewritePrimCall(Facts facts) {
        
        if (p==Prim.bnot) {
          Atom  x = args[0];
          Const a = x.isConst();
          return (a==null) ? bnotVar(x, facts) : bnotConst(a.getVal());
        }
      
        if (p==Prim.not) {
          Atom  x = args[0];
          Const a = x.isConst();
          return (a==null) ? notVar(x, facts) : notConst(a.getVal());
        }
      
        if (p==Prim.neg) {
          Atom  x = args[0];
          Const a = x.isConst();
          return (a==null) ? negVar(x, facts) : negConst(a.getVal());
        }
      
        if (p==Prim.add) {
          Atom  x = args[0];
          Atom  y = args[1];
          Const a = x.isConst();
          Const b = y.isConst();
          if (a==null) {
            return (b==null) ? addVarVar(x, y, facts)
                             : addVarConst(x, b.getVal(), facts);
          } else if (b==null) {
            Code nc = addVarConst(y, a.getVal(), facts);
            return (nc!=null) ? nc : done(p, y, x);
          } else {
            return addConst(a.getVal(), b.getVal());
          }
        }
      
        if (p==Prim.mul) {
          Atom  x = args[0];
          Atom  y = args[1];
          Const a = x.isConst();
          Const b = y.isConst();
          if (a==null) {
            return (b==null) ? mulVarVar(x, y, facts)
                             : mulVarConst(x, b.getVal(), facts);
          } else if (b==null) {
            Code nc = mulVarConst(y, a.getVal(), facts);
            return (nc!=null) ? nc : done(p, y, x);
          } else {
            return mulConst(a.getVal(), b.getVal());
          }
        }
      
        if (p==Prim.or) {
          Atom  x = args[0];
          Atom  y = args[1];
          Const a = x.isConst();
          Const b = y.isConst();
          if (a==null) {
            return (b==null) ? orVarVar(x, y, facts)
                             : orVarConst(x, b.getVal(), facts);
          } else if (b==null) {
            Code nc = orVarConst(y, a.getVal(), facts);
            return (nc!=null) ? nc : done(p, y, x);
          } else {
            return orConst(a.getVal(), b.getVal());
          }
        }
      
        if (p==Prim.and) {
          Atom  x = args[0];
          Atom  y = args[1];
          Const a = x.isConst();
          Const b = y.isConst();
          if (a==null) {
            return (b==null) ? andVarVar(x, y, facts)
                             : andVarConst(x, b.getVal(), facts);
          } else if (b==null) {
            Code nc = andVarConst(y, a.getVal(), facts);
            return (nc!=null) ? nc : done(p, y, x);
          } else {
            return andConst(a.getVal(), b.getVal());
          }
        }
      
        if (p==Prim.xor) {
          Atom  x = args[0];
          Atom  y = args[1];
          Const a = x.isConst();
          Const b = y.isConst();
          if (a==null) {
            return (b==null) ? xorVarVar(x, y, facts)
                             : xorVarConst(x, b.getVal(), facts);
          } else if (b==null) {
            Code nc = xorVarConst(y, a.getVal(), facts);
            return (nc!=null) ? nc : done(p, y, x);
          } else {
            return xorConst(a.getVal(), b.getVal());
          }
        }
      
        if (p==Prim.sub) {
          Atom  x = args[0];
          Atom  y = args[1];
          Const a = x.isConst();
          Const b = y.isConst();
          return (a==null)
                  ? ((b==null) ? subVarVar(x, y, facts)
                               : subVarConst(x, b.getVal(), facts))
                  : ((b==null) ? subConstVar(a.getVal(), y, facts)
                               : subConst(a.getVal(), b.getVal()));
        }
      
        if (p==Prim.shl) {
          Atom  x = args[0];
          Atom  y = args[1];
          Const a = x.isConst();
          Const b = y.isConst();
          return (a==null)
                  ? ((b==null) ? shlVarVar(x, y, facts)
                               : shlVarConst(x, b.getVal(), facts))
                  : ((b==null) ? shlConstVar(a.getVal(), y, facts)
                               : shlConst(a.getVal(), b.getVal()));
        }
      
        if (p==Prim.shr) {
          Atom  x = args[0];
          Atom  y = args[1];
          Const a = x.isConst();
          Const b = y.isConst();
          return (a==null)
                  ? ((b==null) ? shrVarVar(x, y, facts)
                               : shrVarConst(x, b.getVal(), facts))
                  : ((b==null) ? shrConstVar(a.getVal(), y, facts)
                               : shrConst(a.getVal(), b.getVal()));
        }
      
        if (p==Prim.eq) {
          Atom  x = args[0];
          Atom  y = args[1];
          Const a = x.isConst();
          if (a!=null) {
              Const b = y.isConst();
              if (b!=null) {
                  return eqConst(a.getVal(), b.getVal());
              }
          }
          return null;
        }
      
        if (p==Prim.neq) {
          Atom  x = args[0];
          Atom  y = args[1];
          Const a = x.isConst();
          if (a!=null) {
              Const b = y.isConst();
              if (b!=null) {
                  return neqConst(a.getVal(), b.getVal());
              }
          }
          return null;
        }
      
        if (p==Prim.lt) {
          Atom  x = args[0];
          Atom  y = args[1];
          Const a = x.isConst();
          if (a!=null) {
              Const b = y.isConst();
              if (b!=null) {
                  return ltConst(a.getVal(), b.getVal());
              }
          }
          return null;
        }
      
        if (p==Prim.gt) {
          Atom  x = args[0];
          Atom  y = args[1];
          Const a = x.isConst();
          if (a!=null) {
              Const b = y.isConst();
              if (b!=null) {
                  return gtConst(a.getVal(), b.getVal());
              }
          }
          return null;
        }
      
        if (p==Prim.lte) {
          Atom  x = args[0];
          Atom  y = args[1];
          Const a = x.isConst();
          if (a!=null) {
              Const b = y.isConst();
              if (b!=null) {
                  return lteConst(a.getVal(), b.getVal());
              }
          }
          return null;
        }
      
        if (p==Prim.gte) {
          Atom  x = args[0];
          Atom  y = args[1];
          Const a = x.isConst();
          if (a!=null) {
              Const b = y.isConst();
              if (b!=null) {
                  return gteConst(a.getVal(), b.getVal());
              }
          }
          return null;
        }
      
        return null;
      }

    static Code done(Tail t) { return new Done(t); }

    static Code done(Atom a) { return done(new Return(a)); }

    static Code done(int n) { return done(new Const(n)); }

    static Code done(Prim p, Atom[] args) { return done(new PrimCall(p, args)); }

    static Code done(Prim p, Atom a) { return done(new PrimCall(p, a)); }

    static Code done(Prim p, Atom a, Atom b) { return done(new PrimCall(p, a, b)); }

    static Code done(Prim p, Atom a, int n) { return done(new PrimCall(p, a, n)); }

    /** Convenience constructor for PrimCalls.
     */
    public PrimCall(Prim p, Atom[] args) {
        this(p);
        withArgs(args);
      }

    /** Convenience constructor for unary PrimCalls.
     */
    public PrimCall(Prim p, Atom a) {
        this(p, new Atom[] {a});
      }

    /** Convenience constructor for binary PrimCalls.
     */
    public PrimCall(Prim p, Atom a, Atom b) {
        this(p, new Atom[] {a, b});
      }

    public PrimCall(Prim p, Atom a, int n) {
        this(p, a, new Const(n));
      }

    /** Test to see if this tail expression is a call to a specific primitive,
     *  returning null in the (most likely) case that it is not.
     */
    Atom[] isPrim(Prim p) { return (p==this.p) ? args : null; }

    Code bnotVar(Atom x, Facts facts) {
        Tail a = x.lookupFact(facts);
        if (a!=null) {
    
          // Eliminate double negation:
          Atom[] ap = a.isPrim(Prim.bnot);
          if (ap!=null) {
            MILProgram.report("eliminated double bnot");
            return done(ap[0]); // bnot(bnot(u)) == u
          }
    
          // Handle negations of relational operators:
          if ((ap = a.isPrim(Prim.eq))!=null) {  //  eq --> neq
            MILProgram.report("replaced bnot(eq(x,y)) with neq(x,y)");
            return done(Prim.neq, ap);
          }
          if ((ap = a.isPrim(Prim.neq))!=null) {  //  neq --> eq
            MILProgram.report("replaced bnot(neq(x,y)) with eq(x,y)");
            return done(Prim.eq, ap);
          }
          if ((ap = a.isPrim(Prim.lt))!=null) {   //  lt --> gte
            MILProgram.report("replaced bnot(lt(x,y)) with gte(x,y)");
            return done(Prim.gte, ap);
          }
          if ((ap = a.isPrim(Prim.lte))!=null) {  //  lte --> gt
            MILProgram.report("replaced bnot(lte(x,y)) with gt(x,y)");
            return done(Prim.gt, ap);
          }
          if ((ap = a.isPrim(Prim.gt))!=null) {   //  gt --> lte
            MILProgram.report("replaced bnot(gt(x,y)) with lte(x,y)");
            return done(Prim.lte, ap);
          }
          if ((ap = a.isPrim(Prim.gte))!=null) {  //  gte --> lt
            MILProgram.report("replaced bnot(gte(x,y)) with lt(x,y)");
            return done(Prim.lt, ap);
          }
        }
        return null;
      }

    static Code bnotConst(int n) {
        // No opportunity for constant folding here because bnot
        // takes a boolean (True() or False()) and not an int arg.
        return null;
      }

    Code notVar(Atom x, Facts facts) {
        Tail a = x.lookupFact(facts);
        if (a!=null) {
          // Eliminate double negation:
          Atom[] ap = a.isPrim(Prim.not);
          if (ap!=null) {
            MILProgram.report("eliminated double not");
            return done(ap[0]); // not(not(u)) == u
          }
        }
        return null;
      }

    static Code notConst(int n) {
        MILProgram.report("constant folding for not");
        return done(~n);
      }

    Code negVar(Atom x, Facts facts) {
        Tail a = x.lookupFact(facts);
        if (a!=null) {
          Atom[] ap = a.isPrim(Prim.neg);
          if (ap!=null) {
            MILProgram.report("rewrite: -(-x) ==> x");
            return done(ap[0]); // neg(neg(u)) == u
          }
          if ((ap = a.isPrim(Prim.sub))!=null) {
            MILProgram.report("rewrite: -(x - y) ==> y - x");
            return done(Prim.sub, ap[1], ap[0]);
          }
        }
        return null;
      }

    static Code negConst(int n) {
        MILProgram.report("constant folding for neg");
        return done(-n);
      }

    /** Look for opportunities to simplify an expression using idempotence.
     */
    private static Code idempotent(Atom x, Atom y) {
        if (x==y) { // simple idempotence
          // TODO: in an expression of the form (x & y), we could further
          // exploit idempotence if x includes an or with y (or vice versa);
          // handling this would require the addition of Prim and a Facts
          // arguments.
          MILProgram.report("rewrite: x ! x ==> x");
          return done(x);
        }
        return null;
      }

    /** Look for opportunities to rearrange an expression written in terms of a
     *  commutative and associative primitive p, written with an infix ! in the
     *  following code.  The assumptions are that we're trying to optimize an
     *  expression of the form (x ! y) where a and b are facts that have already
     *  been looked up for each of x and y, respectively.
     */
    private static Code commuteRearrange(Prim p, Atom x, Tail a, Atom y, Tail b) {
        Atom[] ap = (a!=null) ? a.isPrim(p)      : null;
        Const  c  = (ap!=null) ? ap[1].isConst() : null;
    
        Atom[] bp = (b!=null) ? b.isPrim(p)      : null;
        Const  d  = (bp!=null) ? bp[1].isConst() : null;
    
        if (c!=null) {
          if (d!=null) {      // (u ! c) ! (w ! d)
            MILProgram.report("rewrite: (u ! c) ! (w ! d) ==> (u ! w) ! (c ! d)");
            return varVarConst(p, ap[0], bp[0], c.getVal() | d.getVal());
          } else {            // (u ! c) ! y
            MILProgram.report("rewrite: (u ! c) ! y==> (u ! y) ! c");
            return varVarConst(p, ap[0], y, c.getVal());
          }
        } else if (d!=null) { // x ! (w ! c)
          MILProgram.report("rewrite: x ! (w ! d) ==> (x ! w) ! d");
          return varVarConst(p, x, bp[0], d.getVal());
        }
        return null;
      }

    /** Create code for (a ! b) ! n where ! is a primitive p; a and b are
     *  variables; and n is a known constant.
     */
    private static Code varVarConst(Prim p, Atom a, Atom b, int n) {
        Var v = new Temp();
        return new Bind(v, new PrimCall(p, a, b), done(p, v, n));
      }

    /** Generate code for a deMorgan's law rewrite.  We are trying to rewrite an expression
     *  of the form p(x, y) with an associated (possibly null) fact a for x and b for y.  If
     *  both a and b are of the form inv(_) for some specific "inverting" primitive, inv,
     *  then we can rewrite the whole formula, p(inv(u), inv(v)) as inv(q(u,v)) where q is a
     *  "dual" for p.  There are (at least) three special cases for this rule:
     *    if p=and, then q=or,  inv=not:    ~p | ~q = ~(p & q)
     *    if p=or,  then q=and, inv=not:    ~p & ~q = ~(p | q)
     *    if p=add, then q=add, inv=neg:    -p + -q = -(p + q)  (add is self-dual)
     */
    private static Code deMorgan(Prim q, Prim inv, Tail a, Tail b) {
        Atom[] ap;
        Atom[] bp;
        if (a!=null && (ap = a.isPrim(inv))!=null &&
            b!=null && (bp = b.isPrim(inv))!=null) {
          MILProgram.report("applied a version of deMorgan's law");
          Var v = new Temp();
          return new Bind(v, new PrimCall(q, ap[0], bp[0]), done(inv, v));
        }
        return null;
      }

    Code addVarVar(Atom x, Atom y, Facts facts) {
        Tail a = x.lookupFact(facts);
        Tail b = y.lookupFact(facts);
        if (a!=null || b!=null) {   // Only look for a rewrite if there are some facts
          Code nc = commuteRearrange(Prim.add, x, a, y, b);
          return ((nc!=null || (nc = deMorgan(Prim.add, Prim.neg, a, b))!=null)) ? nc : null;
        }
        return null;
      }

    Code addVarConst(Atom x, int m, Facts facts) {
        if (m==0) {                         // x + 0 == x
          MILProgram.report("rewrite: x + 0 ==> x");
          return done(x);
        }
        Tail a = x.lookupFact(facts);
        if (a!=null) {
          Atom[] ap;
          if ((ap = a.isPrim(Prim.add))!=null) {
            Const b = ap[1].isConst();
            if (b!=null) {                  // (u + n) + m == u + (m + n)
              return done(p, ap[0], b.getVal() + m);
            }
          }
        }
        return null;
      }

    static Code addConst(int n, int m) {
        MILProgram.report("constant folding for add");
        return done(n + m);
      }

    Code mulVarVar(Atom x, Atom y, Facts facts) {
        return commuteRearrange(Prim.mul, x, x.lookupFact(facts), y, y.lookupFact(facts));
      }

    Code mulVarConst(Atom x, int m, Facts facts) {
        if (m==0) {                         // x * 0 == 0
          MILProgram.report("rewrite: x * 0 ==> 0");
          return done(0);
        }
        if (m==1) {                         // x * 1 == x
          MILProgram.report("rewrite: x * 1 ==> x");
          return done(x);
        }
        if (m==(-1)) {                      // x * -1 == neg(x)
          MILProgram.report("rewrite: x * (-1) ==> -x");
          return done(Prim.neg, x);
        }
        Tail a = x.lookupFact(facts);
        if (a!=null) {
          Atom[] ap;
          if ((ap = a.isPrim(Prim.mul))!=null) {
            Const b = ap[1].isConst();
            if (b!=null) {                  // (u * c) * m == u * (c * m)
              return done(Prim.mul, ap[0], b.getVal() * m);
            }
          } else if ((ap = a.isPrim(Prim.add))!=null) {
            Const b = ap[1].isConst();
            if (b!=null) {                  // (u + n) * m == (u * m) + (n * m)
              Var v = new Temp();
              return new Bind(v,
                              new PrimCall(Prim.mul, ap[0], new Const(m)),
                              done(Prim.add, v, b.getVal()*m));
            }
          }
        }
        return null;
      }

    static Code mulConst(int n, int m) {
        MILProgram.report("constant folding for mul");
        return done(n * m);
      }

    Code orVarVar(Atom x, Atom y, Facts facts) {
        Code nc = idempotent(x, y);
        if (nc==null) {
          Tail a = x.lookupFact(facts);
          Tail b = y.lookupFact(facts);
          if ((a!=null || b!=null) && 
              (nc = commuteRearrange(Prim.or, x, a, y, b))==null) {
            nc = deMorgan(Prim.and, Prim.not, a, b);
          }
        }
        return nc;
      }

    Code orVarConst(Atom x, int m, Facts facts) {
        if (m==0) {
          MILProgram.report("rewrite: x | 0 ==> x");
          return done(x);
        }
        if (m==(~0)) {
          MILProgram.report("rewrite: x | (~0) ==> (~0)");
          return done(~0);
        }
        Tail a = x.lookupFact(facts);
        if (a!=null) {
          Atom[] ap;
          if ((ap = a.isPrim(Prim.or))!=null) {
            Const c = ap[1].isConst();
            if (c!=null) {
              MILProgram.report("rewrite: (u | c) | m ==> u | (c | n)");
              return done(new PrimCall(Prim.or, ap[0], c.getVal() | m));
            }
          } else if ((ap = a.isPrim(Prim.not))!=null) {
            MILProgram.report("rewrite: (~u) | m ==> ~(u & ~m)");
            Var v = new Temp();
            return new Bind(v, new PrimCall(Prim.and, ap[0], ~m),
                            done(new PrimCall(Prim.not, v)));
          } else if ((ap = a.isPrim(Prim.and))!=null) {
            Const c = ap[1].isConst();          // (_ & c) | m
            if (c!=null) {
              Tail b = ap[0].lookupFact(facts); // ((b) & c) | m
              if (b!=null) {
                Atom[] bp = b.isPrim(Prim.or);  // ((_ | _) & c) | m
                if (bp!=null) {
                  Const d = bp[1].isConst();    // ((_ | d) & c) | m
                  if (d!=null) {
                    MILProgram.report("rewrite: ((u | d) & c) | m ==> (u & c) | ((d & c) | m)");
                    Var v = new Temp();
                    int n = (d.getVal() & c.getVal()) | m;
                    return new Bind(v, new PrimCall(Prim.and, bp[0], c),
                                    done(new PrimCall(Prim.or, v, n)));
                  }
                }
              }
            }
          }
        }
        return null;
      }

    static Code orConst(int n, int m) {
        MILProgram.report("constant folding for or");
        return done(n | m);
      }

    Code andVarVar(Atom x, Atom y, Facts facts) {
        Code nc = idempotent(x, y);
        if (nc==null) {
          Tail a = x.lookupFact(facts);
          Tail b = y.lookupFact(facts);
          if ((a!=null || b!=null) && 
              (nc = commuteRearrange(Prim.and, x, a, y, b))==null) {
            nc = deMorgan(Prim.or, Prim.not, a, b);
          }
        }
        return nc;
      }

    Code andVarConst(Atom x, int m, Facts facts) {
        if (m==0) {
          MILProgram.report("rewrite: x & 0 ==> 0");
          return done(0);
        }
        if (m==(~0)) {
          MILProgram.report("rewrite: x & (~0) ==> x");
          return done(x);
        }
        Tail a = x.lookupFact(facts);           // (a) & m
        if (a!=null) {
          Atom[] ap;
          if ((ap = a.isPrim(Prim.and))!=null) {
            Const c = ap[1].isConst();
            if (c!=null) {
              MILProgram.report("rewrite: (u & c) & m ==> u & (c & n)");
              return done(new PrimCall(Prim.and, ap[0], c.getVal() & m));
            }
          } else if ((ap = a.isPrim(Prim.not))!=null) {
            MILProgram.report("rewrite: (~u) & m ==> ~(u | ~m)");
            Var v = new Temp();
            return new Bind(v, new PrimCall(Prim.or, ap[0], ~m),
                            done(new PrimCall(Prim.not, v)));
          } else if ((ap = a.isPrim(Prim.or))!=null) {
            Const c = ap[1].isConst();          // (_ | c) & m
            if (c!=null) {
              MILProgram.report("rewrite: (a | c) & m ==> (a & m) | (c & m)");
              Var v = new Temp();
              return new Bind(v, new PrimCall(Prim.and, ap[0], m),
                              done(new PrimCall(Prim.or, v, c.getVal() & m)));
            }
          }
        }
        return null;
      }

    static Code andConst(int n, int m) {
        MILProgram.report("constant folding for and");
        return done(n & m);
      }

    Code xorVarVar(Atom x, Atom y, Facts facts) {
        if (x==y) { // simple anihilator
          // TODO: in an expression of the form (x ^ y), we could further
          // exploit anihilation if x includes an or with y (or vice versa).
          MILProgram.report("rewrite: x ^ x ==> 0");
          return done(0);
        }
        return commuteRearrange(Prim.mul, x, x.lookupFact(facts), y, y.lookupFact(facts));
      }

    Code xorVarConst(Atom x, int m, Facts facts) {
        if (m==0) {                         // x ^ 0 == x
          MILProgram.report("rewrite: x ^ 0 ==> x");
          return done(x);
        }
        if (m==(~0)) {                      // x ^ (~0) == not(x)
          MILProgram.report("rewrite: x ^ (~0) ==> not(x)");
          return done(new PrimCall(Prim.not, x));
        }
        return null;
      }

    static Code xorConst(int n, int m) {
        MILProgram.report("constant folding for xor");
        return done(n ^ m);
      }

    Code subVarVar(Atom x, Atom y, Facts facts) {
        if (x==y) {                        // x - x == 0
          MILProgram.report("rewrite: x - x ==> 0");
          return done(0);
        }
        return null;
      }

    Code subVarConst(Atom x, int m, Facts facts) {
        if (m==0) {                         // x - 0 == x
          MILProgram.report("rewrite: x - 0 ==> x");
          return done(x);
        }
        // TODO: not sure about this one; it turns simple decrements like sub(x,1) into
        // adds like add(x, -1); I guess this could be addressed by a code generator that
        // doesn't just naively turn every add(x,n) into an add instruction ...
        //
        // If n==0,  then add(x,n) shouldn't occur ...
        //    n==1,  then add(x,n) becomes an increment instruction
        //    n>1,   then add(x,n) becomes an add with immediate argument
        //    n==-1, then add(x,n) becomes a decrement instruction
        //    n< -1, then add(x,n) becomes a subtract with immediate argument
        //
        return done(Prim.add, x, (-m));     // x - n == x + (-n)
      }

    Code subConstVar(int n, Atom y, Facts facts) {
        if (n==0) {                         // 0 - y == -y
          MILProgram.report("rewrite: 0 - y ==> -y");
          return done(Prim.neg, y);
        }
        return null;
      }

    static Code subConst(int n, int m) {
        MILProgram.report("constant folding for sub");
        return done(n - m);
      }

    Code shlVarVar(Atom x, Atom y, Facts facts) {
        return null;
      }

    Code shlVarConst(Atom x, int m, Facts facts) {
        if (m==0) {                         // x << 0 == x
          MILProgram.report("rewrite: x << 0 ==> x");
          return done(x);
        }
        return null;
      }

    Code shlConstVar(int n, Atom y, Facts facts) {
        if (n==0) {                         // 0 << y == 0
          MILProgram.report("rewrite: 0 << y ==> 0");
          return done(0);
        }
        return null;
      }

    static Code shlConst(int n, int m) {
        MILProgram.report("constant folding for shl");
        return done(n << m);
      }

    Code shrVarVar(Atom x, Atom y, Facts facts) {
        return null;
      }

    Code shrVarConst(Atom x, int m, Facts facts) {
        if (m==0) {                         // x >> 0 == x
          MILProgram.report("rewrite: x >> 0 ==> x");
          return done(x);
        }
        return null;
      }

    Code shrConstVar(int n, Atom y, Facts facts) {
        if (n==0) {                         // 0 >> y == 0
          MILProgram.report("rewrite: 0 >> y ==> 0");
          return done(0);
        }
        return null;
      }

    static Code shrConst(int n, int m) {
        MILProgram.report("constant folding for shr");
        return done(n >> m);
      }

    private static Code toBool(boolean b) {
        return new Done(b ? DataAlloc.True : DataAlloc.False);
//        DataAlloc d = new DataAlloc(b ? Cfun.True : Cfun.False);
//        return new Done(d.withArgs(new Atom[0]));
    }

    static Code eqConst(int n, int m) {
        MILProgram.report("constant folding for eq");
        return toBool(n == m);
      }

    static Code neqConst(int n, int m) {
        MILProgram.report("constant folding for neq");
        return toBool(n != m);
      }

    static Code ltConst(int n, int m) {
        MILProgram.report("constant folding for lt");
        return toBool(n < m);
      }

    static Code gtConst(int n, int m) {
        MILProgram.report("constant folding for gt");
        return toBool(n > m);
      }

    static Code lteConst(int n, int m) {
        MILProgram.report("constant folding for lte");
        return toBool(n <= m);
      }

    static Code gteConst(int n, int m) {
        MILProgram.report("constant folding for gte");
        return toBool(n >= m);
      }

    /** Compute an integer summary for a fragment of MIL code with the key property
     *  that alpha equivalent program fragments have the same summary value.
     */
    int summary() { return p.summary(args)*33 + 1; }

    /** Test to see if two Tail expressions are the alpha equivalent.
     */
    boolean alphaTail(Vars thisvars, Tail that, Vars thatvars) { return that.alphaPrimCall(thatvars, this, thisvars); }

    /** Test two items for alpha equivalence.
     */
    boolean alphaPrimCall(Vars thisvars, PrimCall that, Vars thatvars) { return this.p==that.p && this.alphaArgs(thisvars, that, thatvars); }

    public Pairs gen(Pairs ins, Atom a) {
        if (isPure()){
                Pair generated = new Pair(this, new Atoms(a, null));
                if (ins != null) {
                        return ins.gen(generated);
                }
                return new Pairs (generated, null);
        }
        return ins;
}
}
