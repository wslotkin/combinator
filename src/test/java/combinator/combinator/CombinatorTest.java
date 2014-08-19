package combinator.combinator;

import combinator.datamodel.Combination;
import combinator.datamodel.DataObject;
import combinator.datamodel.Position;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static org.junit.Assert.assertEquals;

public class CombinatorTest {

    private List<DataObject> dataObjects;
    private List<Position> positions;
    private Combinator combinator;

    @Before
    public void before() {
        dataObjects = newArrayList(createDataObject("1"), createDataObject("2"), createDataObject("3"));
        positions = newArrayList(new Position(0), new Position(1), new Position(2));

        combinator = new Combinator();
    }

    @Test
    public void whenEitherObjectsOrPositionsIsEmptyShouldReturnEmptyList() throws InterruptedException {
        LinkedBlockingQueue<Combination> firstResult = new LinkedBlockingQueue<>();
        LinkedBlockingQueue<Combination> secondResult = new LinkedBlockingQueue<>();
        combinator.generateCombinations(dataObjects, new ArrayList<Position>(), firstResult);
        combinator.generateCombinations(new ArrayList<DataObject>(), positions, secondResult);

        assertEquals(0, firstResult.size());
        assertEquals(0, secondResult.size());
    }

    @Test
    public void whenSameNumberOfObjectsAndPositionsShouldReturnCorrectList() throws InterruptedException {
        LinkedBlockingQueue<Combination> combinations = new LinkedBlockingQueue<>();
        combinator.generateCombinations(dataObjects, positions, combinations);

        List<Combination> expectedCombinations = newArrayList();

        Combination expectedCombination = new Combination();
        expectedCombination.addMapping(positions.get(0), dataObjects.get(0));
        expectedCombination.addMapping(positions.get(1), dataObjects.get(1));
        expectedCombination.addMapping(positions.get(2), dataObjects.get(2));
        expectedCombinations.add(expectedCombination);

        expectedCombination = new Combination();
        expectedCombination.addMapping(positions.get(0), dataObjects.get(0));
        expectedCombination.addMapping(positions.get(1), dataObjects.get(2));
        expectedCombination.addMapping(positions.get(2), dataObjects.get(1));
        expectedCombinations.add(expectedCombination);

        expectedCombination = new Combination();
        expectedCombination.addMapping(positions.get(0), dataObjects.get(1));
        expectedCombination.addMapping(positions.get(1), dataObjects.get(0));
        expectedCombination.addMapping(positions.get(2), dataObjects.get(2));
        expectedCombinations.add(expectedCombination);

        expectedCombination = new Combination();
        expectedCombination.addMapping(positions.get(0), dataObjects.get(1));
        expectedCombination.addMapping(positions.get(1), dataObjects.get(2));
        expectedCombination.addMapping(positions.get(2), dataObjects.get(0));
        expectedCombinations.add(expectedCombination);

        expectedCombination = new Combination();
        expectedCombination.addMapping(positions.get(0), dataObjects.get(2));
        expectedCombination.addMapping(positions.get(1), dataObjects.get(0));
        expectedCombination.addMapping(positions.get(2), dataObjects.get(1));
        expectedCombinations.add(expectedCombination);

        expectedCombination = new Combination();
        expectedCombination.addMapping(positions.get(0), dataObjects.get(2));
        expectedCombination.addMapping(positions.get(1), dataObjects.get(1));
        expectedCombination.addMapping(positions.get(2), dataObjects.get(0));
        expectedCombinations.add(expectedCombination);

        assertEquals(expectedCombinations.size(), combinations.size());
        assertEquals(new HashSet<>(expectedCombinations), new HashSet<>(combinations));
    }

    @Test
    public void whenMoreObjectsThanPositionsShouldReturnCorrectList() throws InterruptedException {
        positions.remove(0);
        LinkedBlockingQueue<Combination> combinations = new LinkedBlockingQueue<>();
        combinator.generateCombinations(dataObjects, positions, combinations);

        List<Combination> expectedCombinations = newArrayList();

        Combination expectedCombination = new Combination();
        expectedCombination.addMapping(positions.get(0), dataObjects.get(0));
        expectedCombination.addMapping(positions.get(1), dataObjects.get(1));
        expectedCombinations.add(expectedCombination);

        expectedCombination = new Combination();
        expectedCombination.addMapping(positions.get(0), dataObjects.get(0));
        expectedCombination.addMapping(positions.get(1), dataObjects.get(2));
        expectedCombinations.add(expectedCombination);

        expectedCombination = new Combination();
        expectedCombination.addMapping(positions.get(0), dataObjects.get(1));
        expectedCombination.addMapping(positions.get(1), dataObjects.get(0));
        expectedCombinations.add(expectedCombination);

        expectedCombination = new Combination();
        expectedCombination.addMapping(positions.get(0), dataObjects.get(1));
        expectedCombination.addMapping(positions.get(1), dataObjects.get(2));
        expectedCombinations.add(expectedCombination);

        expectedCombination = new Combination();
        expectedCombination.addMapping(positions.get(0), dataObjects.get(2));
        expectedCombination.addMapping(positions.get(1), dataObjects.get(0));
        expectedCombinations.add(expectedCombination);

        expectedCombination = new Combination();
        expectedCombination.addMapping(positions.get(0), dataObjects.get(2));
        expectedCombination.addMapping(positions.get(1), dataObjects.get(1));
        expectedCombinations.add(expectedCombination);

        assertEquals(expectedCombinations.size(), combinations.size());
        assertEquals(new HashSet<>(expectedCombinations), new HashSet<>(combinations));
    }

    @Test
    public void whenFewerObjectsThanPositionsShouldReturnCorrectList() throws InterruptedException {
        dataObjects.remove(0);
        LinkedBlockingQueue<Combination> combinations = new LinkedBlockingQueue<>();
        combinator.generateCombinations(dataObjects, positions, combinations);

        List<Combination> expectedCombinations = newArrayList();

        Combination expectedCombination = new Combination();
        expectedCombination.addMapping(positions.get(0), dataObjects.get(0));
        expectedCombination.addMapping(positions.get(1), dataObjects.get(1));
        expectedCombinations.add(expectedCombination);

        expectedCombination = new Combination();
        expectedCombination.addMapping(positions.get(0), dataObjects.get(0));
        expectedCombination.addMapping(positions.get(2), dataObjects.get(1));
        expectedCombinations.add(expectedCombination);

        expectedCombination = new Combination();
        expectedCombination.addMapping(positions.get(0), dataObjects.get(1));
        expectedCombination.addMapping(positions.get(1), dataObjects.get(0));
        expectedCombinations.add(expectedCombination);

        expectedCombination = new Combination();
        expectedCombination.addMapping(positions.get(0), dataObjects.get(1));
        expectedCombination.addMapping(positions.get(2), dataObjects.get(0));
        expectedCombinations.add(expectedCombination);

        expectedCombination = new Combination();
        expectedCombination.addMapping(positions.get(1), dataObjects.get(0));
        expectedCombination.addMapping(positions.get(2), dataObjects.get(1));
        expectedCombinations.add(expectedCombination);

        expectedCombination = new Combination();
        expectedCombination.addMapping(positions.get(1), dataObjects.get(1));
        expectedCombination.addMapping(positions.get(2), dataObjects.get(0));
        expectedCombinations.add(expectedCombination);

        assertEquals(expectedCombinations.size(), combinations.size());
        assertEquals(new HashSet<>(expectedCombinations), new HashSet<>(combinations));
    }

    private static DataObject createDataObject(String id) {
        Map<String, Comparable> emptyMap = newHashMap();
        return new DataObject(id, emptyMap);
    }
}
