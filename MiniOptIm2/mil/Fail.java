package mil;

public class Fail extends Exception {

    private String msg;

    /** Default constructor.
     */
    public Fail(String msg) {
        this.msg = msg;
    }

    public String toString() {
        return "Interpreter error: " + msg;
    }
}
