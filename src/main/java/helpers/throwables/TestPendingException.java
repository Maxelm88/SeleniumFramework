package helpers.throwables;

public class TestPendingException extends RuntimeException {
    public TestPendingException() {}

    public TestPendingException(String message) { super(message);}

    public TestPendingException(String message, Throwable cause) {super(message, cause);}

    public TestPendingException(Throwable cause) {super(cause);}
}
