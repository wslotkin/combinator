package combinator.constraint;

import combinator.datamodel.Combination;
import combinator.datamodel.Constraint;
import combinator.datamodel.Position;
import combinator.datamodel.Validator;

class SingleObjectPositionConstraint implements Constraint {

    private final String objectId;
    private final Validator<Position> positionValidator;

    public SingleObjectPositionConstraint(String objectId, Validator<Position> positionValidator) {
        this.objectId = objectId;
        this.positionValidator = positionValidator;
    }

    @Override
    public boolean isSatisfied(Combination combination) {
        Position positionForObject = combination.getPositionForId(objectId);

        return positionValidator.isValid(positionForObject);
    }
}
