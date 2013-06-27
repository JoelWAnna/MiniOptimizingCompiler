package mil;

public abstract class Allocator extends Call {

    /** Test to determine whether a given tail expression is pure, that is,
     *  if it might have an externally visible side effect.
     */
    public boolean isPure() { return true; }

    abstract boolean sameFormAlloc(Allocator a);

    boolean isDataAlloc(Cfun c) { return false; }

    boolean isClosAlloc(ClosureDefn k) { return false; }

    boolean isCompAlloc(Block m) { return false; }

    int collectArgs(Atom[] result, int pos) {
        for (int j=0; j<args.length; j++) {
          result[pos++] = args[j];
        }
        return pos;
      }

    public Allocator isAllocator() { return this; }

    public Tail lookupFact(Top top) { 
        //!System.out.print("REGISTER ");
        //!this.display();
        //!System.out.println(" for " + top);
                            this.top = top; return this; }

    protected Top top = null;

    public Top getTop() {
        return top;
    }

    public static void display(Allocator[] allocs) {
        System.out.print("{");
        for (int i=0; i<allocs.length; i++) {
          if (i>0) { System.out.print(", "); }
          if (allocs[i]==null) {
            System.out.print("-");
          } else {
            allocs[i].display();
          }
        }
        System.out.print("}");
      }
}
