package mil;

public class BlockWithKnownCons extends Block {

    private Allocator[] allocs;

    /** Default constructor.
     */
    public BlockWithKnownCons(Code code, Allocator[] allocs) {
        super(code);
        this.allocs = allocs;
    }

    boolean hasKnownCons(Allocator[] allocs) {
        for (int i=0; i<allocs.length; i++) {
          // Compare allocs[i] and this.allocs[i].  Both must be null, or
          // they must have the same constructor to continue to the next i.
          if (allocs[i]==null) {
            if (this.allocs[i]!=null) {
              return false;
            }
          } else if (this.allocs[i]==null || !allocs[i].sameFormAlloc(this.allocs[i])) {
            return false;
          }
        }
        return true;
      }
}
