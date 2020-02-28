package helpers.throwables;

public class GeneralStepException extends RuntimeException {
    public GeneralStepException() { super();}

    public GeneralStepException(String message, Throwable cause) {super(message, cause);}

    public GeneralStepException(Throwable cause) {super(cause);}
}
