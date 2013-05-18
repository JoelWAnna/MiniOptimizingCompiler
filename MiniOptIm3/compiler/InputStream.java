package compiler;

/** A base class for line-oriented source input.
 */
public abstract class InputStream extends java.io.InputStream {
    private Source source;
    private String line = "";
    private int    pos  = (-1);

    /** Construct a new source input phase with a specified handler.
     */
    public InputStream(Source source) {
      this.source = source;
    }

    /** Read the next character from the input stream.
     *
     *  @return The next character, or -1 at the end of the input stream.
     */
    public int read() {
      if (pos<0) {
        line = source.readLine();
        pos  = 0;
      }
      if (line==null) {
        return (-1);
      }
      if (pos > line.length()) {
        pos = (-1);
        return '\n';
      }
      return line.charAt(pos++);
    }
}
