package debug;


/** This class provides a mechanism for reporting internal errors.
 */
public class Internal {
    public static void error(String msg) {
        System.out.println("INTERNAL ERROR: " + msg);
        System.exit(1);
      }
}
