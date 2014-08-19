package combinator;

import combinator.datamodel.Combination;
import combinator.datamodel.DataObject;
import combinator.datamodel.Position;
import combinator.datamodel.Validator;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static combinator.datamodel.Position.distance;

public class DataObjectUtil {

    public static List<DataObject> filterObjectsByAttribute(List<DataObject> objects,
                                                            String attribute,
                                                            Validator attributeValidator) {
        List<DataObject> filteredObjects = newArrayList();

        for (DataObject object : objects) {
            if (attributeValidator.isValid(object.getAttribute(attribute))) filteredObjects.add(object);
        }

        return filteredObjects;
    }

    public static List<DataObject> filterObjectsById(List<DataObject> objects,
                                                     Validator<String> idValidator) {
        List<DataObject> filteredObjects = newArrayList();

        for (DataObject object : objects) {
            if (idValidator.isValid(object.getId())) filteredObjects.add(object);
        }

        return filteredObjects;
    }

    public static List<DataObject> getNeighbors(Position position,
                                                Combination combination,
                                                Validator<Double> distanceValidator) {
        List<DataObject> neighbors = newArrayList();

        for (Position otherPosition : combination.getPositions()) {
            double distance = distance(position, otherPosition);
            if (distance != 0.0 && distanceValidator.isValid(distance)) {
                neighbors.add(combination.getObjectForPosition(otherPosition));
            }
        }

        return neighbors;
    }
}
