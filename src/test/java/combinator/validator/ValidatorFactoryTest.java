package combinator.validator;

import combinator.datamodel.Validator;
import org.junit.Test;

import static combinator.validator.ValidatorFactory.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ValidatorFactoryTest {

    private static final Double referenceValue = 1.234567891;
    private static final Double increment = 0.5;

    @Test
    public void verifyLessThanValidator() {
        Validator<Double> validator = newLessThanValidator(referenceValue);

        assertTrue(validator.isValid(referenceValue - increment));
        assertFalse(validator.isValid(referenceValue + increment));
        assertFalse(validator.isValid(referenceValue));
    }

    @Test
    public void verifyGreaterThanValidator() {
        Validator<Double> validator = newGreaterThanValidator(referenceValue);

        assertFalse(validator.isValid(referenceValue - increment));
        assertTrue(validator.isValid(referenceValue + increment));
        assertFalse(validator.isValid(referenceValue));
    }

    @Test
    public void verifyLessThanOrEqualToValidator() {
        Validator<Double> validator = newLessThanOrEqualToValidator(referenceValue);

        assertTrue(validator.isValid(referenceValue - increment));
        assertFalse(validator.isValid(referenceValue + increment));
        assertTrue(validator.isValid(referenceValue));
    }

    @Test
    public void verifyGreaterThanOrEqualToValidator() {
        Validator<Double> validator = newGreaterThanOrEqualToValidator(referenceValue);

        assertFalse(validator.isValid(referenceValue - increment));
        assertTrue(validator.isValid(referenceValue + increment));
        assertTrue(validator.isValid(referenceValue));
    }

    @Test
    public void verifyEqualToValidator() {
        Validator<Double> validator = newEqualToValidator(referenceValue);

        assertFalse(validator.isValid(referenceValue - increment));
        assertFalse(validator.isValid(referenceValue + increment));
        assertTrue(validator.isValid(referenceValue));
    }

    @Test
    public void verifyNotEqualToValidator() {
        Validator<Double> validator = newNotEqualToValidator(referenceValue);

        assertTrue(validator.isValid(referenceValue - increment));
        assertTrue(validator.isValid(referenceValue + increment));
        assertFalse(validator.isValid(referenceValue));
    }

    @Test
    public void verifyAllValidators() {
        Validator<Double> firstValidator = newGreaterThanValidator(referenceValue - increment);
        Validator<Double> secondValidator = newLessThanValidator(referenceValue + increment);
        Validator<Double> validator = allOfValidators(firstValidator, secondValidator);

        assertFalse(validator.isValid(referenceValue - increment));
        assertFalse(validator.isValid(referenceValue + increment));
        assertTrue(validator.isValid(referenceValue));
    }

    @Test
    public void verifyAnyValidators() {
        Validator<Double> firstValidator = newGreaterThanValidator(referenceValue - increment);
        Validator<Double> secondValidator = newLessThanValidator(referenceValue + increment);
        Validator<Double> validator = anyOfValidators(firstValidator, secondValidator);

        assertTrue(validator.isValid(referenceValue - increment));
        assertTrue(validator.isValid(referenceValue + increment));
        assertTrue(validator.isValid(referenceValue));
    }
}