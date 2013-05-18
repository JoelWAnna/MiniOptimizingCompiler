package mini;
import compiler.Failure;
import mil.*;

/** Represents an output phase for producing textual output of
 *  abstract syntax trees in dot format, suitable for the AT&T
 *  Graphviz tools.
 */
public class DotOutput {

    private java.io.PrintStream out;

    /** Default constructor.
     */
    public DotOutput(java.io.PrintStream out) {
        this.out = out;
    }

    /** Create a dot graph on the specified output stream to
     *  describe the program for a specific source program.
     */
    public void toDot(Fundefs defs) {
        out.println("digraph AST {");
        out.println("node [shape=box];");
        Fundefs.toDot(defs, this, 0);
        out.println("}");
    }

    /** Output a description of a particular node in the
     *  dot description of an AST.  The label specifies the
     *  "friendly" description of the node that will be used
     *  in the output, while the nodeNo is an integer value
     *  that uniquely identifies this particular node.  We
     *  return nodeNo+1 at the end of this function, indicating
     *  the next available node number.
     */
    public int node(String label, int nodeNo) {
        out.println(nodeNo + "[label=\"" + label + "\"];");
        return nodeNo+1;
    }

    /** A variant of node() that uses a different background
     *  color to signal a root node.
     */
    public int root(String label, int nodeNo) {
        out.println(nodeNo + "[style=\"filled\", fillcolor=\"lightblue\", label=\"" + label + "\"];");
        return nodeNo+1;
    }

    /** Output an edge between the specified pair of AST nodes
     *  in the appropriate format for the dot tools.
     */
    public void join(int from, int to) {
        out.println(from + " -> " + to + ";");
    }
}
