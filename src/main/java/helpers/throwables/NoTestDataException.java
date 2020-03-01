package helpers.throwables;

import java.util.Arrays;

public class NoTestDataException extends RuntimeException {
    public NoTestDataException() {}

    public NoTestDataException(String message) {super(message);}

    public NoTestDataException(String message, Throwable cause) {super(message,cause);}

    public NoTestDataException(Throwable cause) {super(cause);}

    public NoTestDataException(Class clazz, String endpoint, Object ...params) {
        this("Empty " + clazz.getSimpleName() + " for " + endpoint + ", params: " + Arrays.toString(params));
    }
}
