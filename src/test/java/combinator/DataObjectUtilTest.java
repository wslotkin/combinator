package combinator;

import combinator.datamodel.Combination;
import combinator.datamodel.DataObject;
import combinator.datamodel.Position;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static combinator.DataObjectUtil.*;
import static combinator.validator.ValidatorFactory.newEqualToValidator;
import static combinator.validator.ValidatorFactory.newGreaterThanOrEqualToValidator;
import static combinator.validator.ValidatorFactory.newLessThanOrEqualToValidator;
import static org.junit.Assert.*;

public class DataObjectUtilTest {

    private static final String SIZE = "size";
    private static final DataObject FIRST_OBJECT = createDataObjectAttribute("FIRST_OBJECT", SIZE, 1.0);
    private static final DataObject SECOND_OBJECT = createDataObjectAttribute("SECOND_OBJECT", SIZE, 2.0);
    private static final DataObject THIRD_OBJECT = createDataObjectAttribute("THIRD_OBJECT", SIZE, 3.0);
    private static final List<DataObject> ALL_OBJECTS = newArrayList(FIRST_OBJECT, SECOND_OBJECT, THIRD_OBJECT);
    private static final Position FIRST_POSITION = new Position(1);
    private static final Position SECOND_POSITION = new Position(2);
    private static final Position THIRD_POSITION = new Position(3);

    @Test
    public void verifyFilterByAttribute() {
        List<DataObject> filtered = filterObjectsByAttribute(ALL_OBJECTS, SIZE, newGreaterThanOrEqualToValidator(1.5));

        assertEquals(2, filtered.size());
        assertFalse(filtered.contains(FIRST_OBJECT));
        assertTrue(filtered.contains(SECOND_OBJECT));
        assertTrue(filtered.contains(THIRD_OBJECT));
    }

    @Test
    public void verifyFilterById() {
        List<DataObject> filtered = filterObjectsById(ALL_OBJECTS, newEqualToValidator("FIRST_OBJECT"));

        assertEquals(1, filtered.size());
        assertTrue(filtered.contains(FIRST_OBJECT));
        assertFalse(filtered.contains(SECOND_OBJECT));
        assertFalse(filtered.contains(THIRD_OBJECT));
    }

    @Test
    public void verifyGetNeighbors() {

        Combination combination = new Combination();
        combination.addMapping(FIRST_POSITION, FIRST_OBJECT);
        combination.addMapping(SECOND_POSITION, SECOND_OBJECT);
        combination.addMapping(THIRD_POSITION, THIRD_OBJECT);

        List<DataObject> filtered = getNeighbors(THIRD_POSITION, combination, newLessThanOrEqualToValidator(1.5));

        assertEquals(1, filtered.size());
        assertFalse(filtered.contains(FIRST_OBJECT));
        assertTrue(filtered.contains(SECOND_OBJECT));
        assertFalse(filtered.contains(THIRD_OBJECT));
    }


    private static DataObject createDataObjectAttribute(String id, String attribute, Double value) {
        Map<String, Double> attributeMap = newHashMap();
        attributeMap.put(attribute, value);

        return new DataObject(id, attributeMap);
    }
}