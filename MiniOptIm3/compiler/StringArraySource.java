// This is supporting software for the CS321 Compilers and Language Design I
// Copyright (c) Mark P Jones, Portland State University

package compiler;

/** An implementation of a Source that uses an array of Strings to
 *  specify the input lines.
 */
public class StringArraySource extends Source {
    private String   text;
    private String[] lines;
    private int      count = 0;

    public StringArraySource(Handler handler, String text, String[] lines) {
        super(handler);
        this.text  = text;
        this.lines = lines;
        this.count = 0;
    }

    /** Return a description of this source.
     */
    public String describe() {
        return text;
    }

    /** Read the next line from the input array.
     *
     *  @return The next line, or null at the end of the input stream.
     */
    public String readLine() {
        if (lines==null || count>=lines.length) {
            return null;
        } else {
            return lines[count++];
        }
    }

    /** Return the current line number.
     */
    public int getLineNo() {
        return count;
    }

    /** Return the text of a specific line, given its line number.
     */
    public String getLine(int lineNo) {
        if (lines==null || lineNo<1 || lineNo>lines.length) {
            return null;
        } else {
            return lines[lineNo-1];
        }
    }

    /** Close the input stream so that no further lines are returned.
     */
    public void close() {
        lines = null;
    }
}
