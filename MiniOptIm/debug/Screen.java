package debug;


/** This class provides a simple mechanism for formatted output.
 */
public class Screen {
    private int col = 0;
    public int getIndent() {
        return col;
      }
    public void indent(int n) {
        if (n<col) {
          System.out.println();
          col = 0;
        }
        for (; col<n; col++) {
          System.out.print(" ");
        }
      }
    public void println() {
        System.out.println();
        col = 0;
      }
    public void print(String s) {
        System.out.print(s);
        col += s.length();
      }
    public void print(int i) {
        print(Integer.toString(i));
      }
    public void print(String[] ids) {
        if (ids.length>0) {
          print(ids[0]);
          for (int i=1; i<ids.length; i++) {
            print(" ");
            print(ids[i]);
          }
        }
      }
    public void space() {
        print(" ");
      }
}
