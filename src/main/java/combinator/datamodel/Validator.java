package combinator.datamodel;

public interface Validator<T extends Comparable<T>> {

    boolean isValid(T value);
}
