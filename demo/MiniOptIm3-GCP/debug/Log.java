package debug;
import java.io.PrintStream;

/** This class provides a simple debugging log.
 */
public class Log {

    private static PrintStream out = null;

    public static void on(PrintStream out) {
        Log.out = out;
      }

    public static void on() { on(System.out); }

    public static void off() { on(null); }

    public static void print(String msg) {
        if (out!=null) {
          out.print(msg);
        }
      }

    public static void println(String msg) {
        if (out!=null) {
          out.println(msg);
        }
      }

    public static void println() {
        if (out!=null) {
          out.println();
        }
      }
}
