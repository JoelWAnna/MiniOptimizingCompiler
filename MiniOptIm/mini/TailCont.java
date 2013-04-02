package mini;

import compiler.Failure;
import mil.*;

/** Represents a continuation that takes a Tail that will produce the
 *  final result of a previous calculation and returns a code sequence
 *  that uses the Tail to complete a computation.
 */
abstract class TailCont {
    abstract Code with(final Tail t);
}
