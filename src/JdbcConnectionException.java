public class JdbcConnectionException extends Exception {
    public JdbcConnectionException () {

    }

    public JdbcConnectionException (String message) {
        super (message);
    }

    public JdbcConnectionException (Throwable cause) {
        super (cause);
    }

    public JdbcConnectionException (String message, Throwable cause) {
        super (message, cause);
    }
}
