package mini;

import compiler.Failure;
import mil.*;
import compiler.*;

/** A simple program for testing the Mini lexer, parser, and
 *  IndentOutput phases.
 */
public class IndentTest {
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
                if (defs!=null) {
                    Fundefs.indent(defs);
                }
            } catch (Exception e) {
                handler.report(new Failure("Exception: " + e));
            }
        }
    }
}
