package combinator.constraint;

import combinator.datamodel.*;

import java.util.List;

import static combinator.DataObjectUtil.getNeighbors;

class AnyNeighboringObjectsConstraint implements Constraint {

    private final String objectId;
    private final String attribute;
    private final Validator attributeValidator;
    private final Validator<Double> distanceValidator;

    public AnyNeighboringObjectsConstraint(String objectId,
                                           String attribute,
                                           Validator attributeValidator,
                                           Validator<Double> distanceValidator) {
        this.objectId = objectId;
        this.attribute = attribute;
        this.attributeValidator = attributeValidator;
        this.distanceValidator = distanceValidator;
    }

    @Override
    public boolean isSatisfied(Combination combination) {
        Position position = combination.getPositionForId(objectId);

        List<DataObject> neighbors = getNeighbors(position, combination, distanceValidator);
        for (DataObject neighbor : neighbors) {
            Comparable neighborValue = neighbor.getAttribute(attribute);
            if (attributeValidator.isValid(neighborValue)) {
                return true;
            }
        }

        return false;
    }
}
