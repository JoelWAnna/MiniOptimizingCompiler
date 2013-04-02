package mini;

import compiler.Failure;
import mil.*;

/** Represents a continuation that takes a variable containing the
 *  result of a previous calculation and returns a code sequence that
 *  uses that variable to complete a computation.
 */
abstract class VarCont {
    abstract Code with(final Var v);
}
