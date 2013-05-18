package mini;
import compiler.Failure;
import mil.*;
import compiler.*;

/** A simple program for testing the Mini lexer, parser, and
 *  static analysis phases.
 */
public class Check {

    public static void main(String[] args) {
        if (args.length!=1) {
            System.out.println("Program requires exactly one argument");
        } else {
            Handler handler = new SimpleHandler();
            try {
                MiniLexer lexer
                  = new MiniLexer(new java.io.FileReader(args[0]), handler);
                MiniParser parser
                  = new MiniParser(handler, lexer);
                Fundefs defs = parser.getProgram();
  
                if (defs!=null) {                       // Did parser succeed?
                    Fundef mainFun = Fundefs.check(new Context(handler, defs), defs);
                    if (handler.getNumFailures()==0) {  // Static analysis ok?
                        System.out.println("Program appears to be valid!");
                    }
                }
            } catch (Failure f) {
                handler.report(f);
            } catch (Exception e) {
                handler.report(new Failure("Exception: " + e));
            }
            System.out.println("Total failures found: "
                                 + handler.getNumFailures());
        }
    }
}
