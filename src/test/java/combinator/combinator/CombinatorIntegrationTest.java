package combinator.combinator;

import combinator.constraint.ConstraintBuilder;
import combinator.datamodel.*;
import combinator.position.NDimensionalPositionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static combinator.DataObjectUtil.filterObjectsByAttribute;
import static combinator.DataObjectUtil.getNeighbors;
import static combinator.validator.ValidatorFactory.*;
import static junit.framework.Assert.assertTrue;

public class CombinatorIntegrationTest {

    private static final int HEIGHT = 5;
    private static final int WIDTH = 5;
    private static final String GENDER = "gender";
    private static final String VISION = "vision";
    private List<DataObject> dataObjects;
    private NDimensionalPositionFactory positionFactory;
    private Validator<Double> poorVisionValidator;
    private Validator<Boolean> isMaleValidator;
    private Validator<Double> neighboringDistanceValidator;
    private Validator<Position> firstTwoRowsValidator;
    private ParallelizingConstrainedCombinator constrainedCombinator;

    @Before
    public void before() {
        positionFactory = new NDimensionalPositionFactory(HEIGHT, WIDTH);
        List<DataObject> fullObjectsList = newArrayList(createDataObjectWithIdGenderAndVision("a", true, 1.0),
                createDataObjectWithIdGenderAndVision("b", true, 2.0),
                createDataObjectWithIdGenderAndVision("c", false, 3.0),
                createDataObjectWithIdGenderAndVision("d", false, 4.0),
                createDataObjectWithIdGenderAndVision("e", true, 5.0),
                createDataObjectWithIdGenderAndVision("f", true, 6.0),
                createDataObjectWithIdGenderAndVision("g", false, 7.0),
                createDataObjectWithIdGenderAndVision("h", false, 8.0),
                createDataObjectWithIdGenderAndVision("i", false, 9.0),
                createDataObjectWithIdGenderAndVision("j", true, 10.0),
                createDataObjectWithIdGenderAndVision("k", true, 11.0),
                createDataObjectWithIdGenderAndVision("l", false, 12.0),
                createDataObjectWithIdGenderAndVision("m", true, 13.0),
                createDataObjectWithIdGenderAndVision("n", false, 14.0),
                createDataObjectWithIdGenderAndVision("o", false, 15.0),
                createDataObjectWithIdGenderAndVision("p", true, 16.0),
                createDataObjectWithIdGenderAndVision("q", true, 17.0),
                createDataObjectWithIdGenderAndVision("r", false, 18.0),
                createDataObjectWithIdGenderAndVision("s", true, 19.0),
                createDataObjectWithIdGenderAndVision("t", false, 20.0),
                createDataObjectWithIdGenderAndVision("u", false, 21.0),
                createDataObjectWithIdGenderAndVision("v", false, 22.0),
                createDataObjectWithIdGenderAndVision("w", true, 23.0),
                createDataObjectWithIdGenderAndVision("x", true, 24.0),
                createDataObjectWithIdGenderAndVision("y", true, 25.0),
                createDataObjectWithIdGenderAndVision("z", false, 26.0));
        dataObjects = fullObjectsList.subList(0, HEIGHT * WIDTH);

        poorVisionValidator = newLessThanOrEqualToValidator(5.0);
        isMaleValidator = newEqualToValidator(true);
        neighboringDistanceValidator = newEqualToValidator(1.0);
        firstTwoRowsValidator = newLessThanValidator(new Position(3, 0));

        constrainedCombinator = new ParallelizingConstrainedCombinator(new ParallelizingCombinator(6), 3, 30L);
    }

    @After
    public void after() {
        constrainedCombinator.finit();
    }

    @Test
    public void testEndToEndWithGenderAndVisionConstraints() throws InterruptedException {
        List<DataObject> males = filterObjectsByAttribute(dataObjects, GENDER, isMaleValidator);
        List<DataObject> poorVisions = filterObjectsByAttribute(dataObjects, VISION, poorVisionValidator);

        Constraint constraints = new ConstraintBuilder()
                .restrictAllObjects(poorVisions.toArray(new DataObject[poorVisions.size()]))
                .toPosition(firstTwoRowsValidator)
                .and()
                .restrictAllNeighborsOfObjects(neighboringDistanceValidator, males.toArray(new DataObject[males.size()]))
                .toAttribute(GENDER, newNegatingValidator(isMaleValidator))
                .build();

        Combination validCombination = constrainedCombinator.findValidCombination(newArrayList(constraints),
                dataObjects,
                positionFactory.createPositions(), new LinkedBlockingQueue<Combination>());

        for (DataObject poorVision : poorVisions) {
            Position position = validCombination.getPositionForId(poorVision.getId());
            assertTrue(firstTwoRowsValidator.isValid(position));
        }

        for (DataObject male : males) {
            Position position = validCombination.getPositionForId(male.getId());
            List<DataObject> neighbors = getNeighbors(position, validCombination, neighboringDistanceValidator);
            for (DataObject neighbor : neighbors) {
                Boolean genderAttribute = (Boolean) neighbor.getAttribute(GENDER);
                assertTrue(!isMaleValidator.isValid(genderAttribute));
            }
        }
    }

    private static DataObject createDataObjectWithIdGenderAndVision(String id, boolean isMale, double vision) {
        Map<String, Comparable> attributeMap = newHashMap();
        attributeMap.put(GENDER, isMale);
        attributeMap.put(VISION, vision);

        return new DataObject(id, attributeMap);
    }
}