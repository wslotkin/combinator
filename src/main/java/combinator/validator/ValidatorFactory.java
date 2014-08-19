package combinator.validator;

import combinator.datamodel.Validator;

public class ValidatorFactory {

    public static <T extends Comparable<T>> Validator<T> newLessThanValidator(final T referenceValue) {
        return new Validator<T>() {
            @Override
            public boolean isValid(T value) {
                return value.compareTo(referenceValue) < 0;
            }
        };
    }

    public static <T extends Comparable<T>> Validator<T> newGreaterThanValidator(final T referenceValue) {
        return new Validator<T>() {
            @Override
            public boolean isValid(T value) {
                return value.compareTo(referenceValue) > 0;
            }
        };
    }

    public static <T extends Comparable<T>> Validator<T> newLessThanOrEqualToValidator(final T referenceValue) {
        return new Validator<T>() {
            @Override
            public boolean isValid(T value) {
                return value.compareTo(referenceValue) <= 0;
            }
        };
    }

    public static <T extends Comparable<T>> Validator<T> newGreaterThanOrEqualToValidator(final T referenceValue) {
        return new Validator<T>() {
            @Override
            public boolean isValid(T value) {
                return value.compareTo(referenceValue) >= 0;
            }
        };
    }

    public static <T extends Comparable<T>> Validator<T> newEqualToValidator(final T referenceValue) {
        return new Validator<T>() {
            @Override
            public boolean isValid(T value) {
                return value.equals(referenceValue);
            }
        };
    }

    public static <T extends Comparable<T>> Validator<T> newNotEqualToValidator(final T referenceValue) {
        return new Validator<T>() {
            @Override
            public boolean isValid(T value) {
                return !value.equals(referenceValue);
            }
        };
    }

    public static <T extends Comparable<T>> Validator<T> newNegatingValidator(final Validator<T> inputValidator) {
        return new Validator<T>() {
            @Override
            public boolean isValid(T value) {
                return !inputValidator.isValid(value);
            }
        };
    }

    public static <T extends Comparable<T>> Validator<T> anyOfValidators(final Validator<T>... inputValidators) {
        return new Validator<T>() {
            @Override
            public boolean isValid(T value) {
                for (Validator<T> validator : inputValidators) {
                    if (validator.isValid(value)) return true;
                }

                return false;
            }
        };
    }

    public static <T extends Comparable<T>> Validator<T> allOfValidators(final Validator<T>... inputValidators) {
        return new Validator<T>() {
            @Override
            public boolean isValid(T value) {
                for (Validator<T> validator : inputValidators) {
                    if (!validator.isValid(value)) return false;
                }

                return true;
            }
        };
    }
}
