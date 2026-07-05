package exceptions;

public class ConstructorException extends IllegalArgumentException {
    public ConstructorException(String message) {
        super(message);
    }
}
