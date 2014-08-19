package combinator.constraint;

import combinator.datamodel.Combination;
import combinator.datamodel.Constraint;
import combinator.datamodel.Position;
import combinator.datamodel.Validator;

import static combinator.datamodel.Position.distance;

class ObjectPairDistanceConstraint implements Constraint {

    private final String firstObjectId;
    private final String secondObjectId;
    private final Validator<Double> distanceValidator;

    public ObjectPairDistanceConstraint(String firstObjectId,
                                        String secondObjectId,
                                        Validator<Double> distanceValidator) {
        this.firstObjectId = firstObjectId;
        this.secondObjectId = secondObjectId;
        this.distanceValidator = distanceValidator;
    }

    @Override
    public boolean isSatisfied(Combination combination) {
        Position firstPosition = combination.getPositionForId(firstObjectId);
        Position secondPosition = combination.getPositionForId(secondObjectId);

        double distance = distance(firstPosition, secondPosition);

        return distanceValidator.isValid(distance);
    }
}
