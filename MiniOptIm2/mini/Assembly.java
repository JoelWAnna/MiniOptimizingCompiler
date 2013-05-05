package mini;
import compiler.Failure;
import mil.*;

/** Provides a simple mechanism for assembly language output.
 */
public class Assembly {

    /** In the current system, we assume that all values can be represented
     *  by a single word whose size in bytes is given by the WORDSIZE constant.
     */
    public static final int WORDSIZE = 4;

    /** Specifies the name of the entry point to the compiled code.
     */
    static final String entry = "mini_main";
}
