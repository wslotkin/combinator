package combinator.constraint;

import combinator.datamodel.Combination;
import combinator.datamodel.Constraint;
import combinator.datamodel.DataObject;
import combinator.datamodel.Position;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static combinator.validator.ValidatorFactory.*;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class ConstraintBuilderTest {

    private static final DataObject object1 = createDataObjectWithAttribute("object1", "gender", "male");
    private static final DataObject object2 = createDataObjectWithAttribute("object2", "gender", "male");
    private static final DataObject object3 = createDataObjectWithAttribute("object3", "gender", "female");

    @Test
    public void verifySingleObjectPositionConstraint() {
        Constraint constraint = new ConstraintBuilder()
                .restrictObject(object1)
                .toPosition(newLessThanOrEqualToValidator(new Position(1)))
                .build();

        Combination validCombination1 = createCombinationForObjects(newArrayList(object1, object2, object3));
        Combination validCombination2 = createCombinationForObjects(newArrayList(object2, object1, object3));
        Combination invalidCombination = createCombinationForObjects(newArrayList(object3, object2, object1));

        assertTrue(constraint.isSatisfied(validCombination1));
        assertTrue(constraint.isSatisfied(validCombination2));
        assertFalse(constraint.isSatisfied(invalidCombination));
    }

    @Test
    public void verifyAllMultipleObjectPositionConstraint() {
        Constraint constraint = new ConstraintBuilder()
                .restrictAllObjects(object1, object2)
                .toPosition(newLessThanOrEqualToValidator(new Position(1)))
                .build();

        Combination validCombination1 = createCombinationForObjects(newArrayList(object1, object2, object3));
        Combination validCombination2 = createCombinationForObjects(newArrayList(object2, object1, object3));
        Combination invalidCombination = createCombinationForObjects(newArrayList(object3, object2, object1));

        assertTrue(constraint.isSatisfied(validCombination1));
        assertTrue(constraint.isSatisfied(validCombination2));
        assertFalse(constraint.isSatisfied(invalidCombination));
    }

    @Test
    public void verifyAnyMultipleObjectPositionConstraint() {
        Constraint constraint = new ConstraintBuilder()
                .restrictAnyObject(object1, object2)
                .toPosition(newLessThanOrEqualToValidator(new Position(0)))
                .build();

        Combination validCombination1 = createCombinationForObjects(newArrayList(object1, object2, object3));
        Combination validCombination2 = createCombinationForObjects(newArrayList(object2, object1, object3));
        Combination invalidCombination = createCombinationForObjects(newArrayList(object3, object2, object1));

        assertTrue(constraint.isSatisfied(validCombination1));
        assertTrue(constraint.isSatisfied(validCombination2));
        assertFalse(constraint.isSatisfied(invalidCombination));
    }

    @Test
    public void verifyObjectsDistanceConstraint() {
        Constraint constraint = new ConstraintBuilder()
                .restrictAllObjects(object1, object2)
                .toDistance(newLessThanOrEqualToValidator(1.0))
                .build();

        Combination validCombination1 = createCombinationForObjects(newArrayList(object1, object2, object3));
        Combination validCombination2 = createCombinationForObjects(newArrayList(object3, object2, object1));
        Combination invalidCombination = createCombinationForObjects(newArrayList(object2, object3, object1));

        assertTrue(constraint.isSatisfied(validCombination1));
        assertTrue(constraint.isSatisfied(validCombination2));
        assertFalse(constraint.isSatisfied(invalidCombination));
    }

    @Test
    public void verifyAllNeighborAttributeConstraint() {
        Constraint constraint = new ConstraintBuilder()
                .restrictAllNeighborsOfObject(newLessThanOrEqualToValidator(1.0), object1)
                .toAttribute("gender", newNotEqualToValidator("male"))
                .build();

        Combination invalidCombination1 = createCombinationForObjects(newArrayList(object1, object2, object3));
        Combination invalidCombination2 = createCombinationForObjects(newArrayList(object3, object2, object1));
        Combination validCombination = createCombinationForObjects(newArrayList(object2, object3, object1));

        assertFalse(constraint.isSatisfied(invalidCombination1));
        assertFalse(constraint.isSatisfied(invalidCombination2));
        assertTrue(constraint.isSatisfied(validCombination));
    }

    @Test
    public void verifyAnyObjectsNeighborAttributeConstraint() {
        Constraint constraint = new ConstraintBuilder()
                .restrictAnyNeighborsOfObjects(newLessThanOrEqualToValidator(1.0), object1)
                .toAttribute("gender", newNotEqualToValidator("male"))
                .build();

        Combination invalidCombination = createCombinationForObjects(newArrayList(object1, object2, object3));
        Combination validCombination1 = createCombinationForObjects(newArrayList(object3, object1, object2));
        Combination validCombination2 = createCombinationForObjects(newArrayList(object2, object3, object1));

        assertFalse(constraint.isSatisfied(invalidCombination));
        assertTrue(constraint.isSatisfied(validCombination1));
        assertTrue(constraint.isSatisfied(validCombination2));
    }

    @Test
     public void verifyAllObjectsNeighborAttributeConstraint() {
        Constraint constraint = new ConstraintBuilder()
                .restrictAllNeighborsOfObjects(newLessThanOrEqualToValidator(1.0), object1)
                .toAttribute("gender", newNotEqualToValidator("male"))
                .build();

        Combination invalidCombination1 = createCombinationForObjects(newArrayList(object1, object2, object3));
        Combination invalidCombination2 = createCombinationForObjects(newArrayList(object3, object2, object1));
        Combination validCombination = createCombinationForObjects(newArrayList(object2, object3, object1));

        assertFalse(constraint.isSatisfied(invalidCombination1));
        assertFalse(constraint.isSatisfied(invalidCombination2));
        assertTrue(constraint.isSatisfied(validCombination));
    }

    @Test
    public void verifyAnyNeighborAttributeConstraint() {
        Constraint constraint = new ConstraintBuilder()
                .restrictAnyNeighborsOfObject(newLessThanOrEqualToValidator(1.0), object1)
                .toAttribute("gender", newNotEqualToValidator("male"))
                .build();

        Combination invalidCombination = createCombinationForObjects(newArrayList(object1, object2, object3));
        Combination validCombination1 = createCombinationForObjects(newArrayList(object3, object1, object2));
        Combination validCombination2 = createCombinationForObjects(newArrayList(object2, object3, object1));

        assertFalse(constraint.isSatisfied(invalidCombination));
        assertTrue(constraint.isSatisfied(validCombination1));
        assertTrue(constraint.isSatisfied(validCombination2));
    }

    @Test
    public void verifyMultipleAndConstraints() {
        Constraint constraint = new ConstraintBuilder()
                .restrictObject(object1)
                .toPosition(newLessThanOrEqualToValidator(new Position(1)))
                .and()
                .restrictObject(object2)
                .toPosition(newEqualToValidator(new Position(1)))
                .build();

        Combination validCombination = createCombinationForObjects(newArrayList(object1, object2, object3));
        Combination invalidCombination1 = createCombinationForObjects(newArrayList(object3, object2, object1));
        Combination invalidCombination2 = createCombinationForObjects(newArrayList(object2, object3, object1));

        assertTrue(constraint.isSatisfied(validCombination));
        assertFalse(constraint.isSatisfied(invalidCombination1));
        assertFalse(constraint.isSatisfied(invalidCombination2));
    }

    @Test
    public void verifyMultipleOrConstraints() {
        Constraint constraint = new ConstraintBuilder()
                .restrictObject(object1)
                .toPosition(newLessThanOrEqualToValidator(new Position(1)))
                .or()
                .restrictObject(object2)
                .toPosition(newEqualToValidator(new Position(1)))
                .build();

        Combination validCombination1 = createCombinationForObjects(newArrayList(object1, object2, object3));
        Combination validCombination2 = createCombinationForObjects(newArrayList(object3, object2, object1));
        Combination invalidCombination = createCombinationForObjects(newArrayList(object2, object3, object1));

        assertTrue(constraint.isSatisfied(validCombination1));
        assertTrue(constraint.isSatisfied(validCombination2));
        assertFalse(constraint.isSatisfied(invalidCombination));
    }

    private static DataObject createDataObjectWithAttribute(String id, String attribute, Comparable value) {
        Map<String, Comparable> attributeMap = newHashMap();
        attributeMap.put(attribute, value);
        return new DataObject(id, attributeMap);
    }

    private static Combination createCombinationForObjects(List<DataObject> objectList) {
        Map<Position, DataObject> positionToObjectMap = newHashMap();
        Map<String, Position> idToPositionMap = newHashMap();

        int coordinate = 0;
        for (DataObject dataObject : objectList) {
            Position position = new Position(coordinate++);
            positionToObjectMap.put(position, dataObject);
            idToPositionMap.put(dataObject.getId(), position);
        }

        return new Combination(idToPositionMap, positionToObjectMap);
    }
}