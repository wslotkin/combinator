package combinator;

public class UnsatisfiedConstraintException extends RuntimeException {
    public UnsatisfiedConstraintException(String message) {
        super(message);
    }
}
