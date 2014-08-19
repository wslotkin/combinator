package combinator.position;

import combinator.datamodel.Position;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class NDimensionalPositionFactoryTest {

    private static final int WIDTH = 4;
    private static final int HEIGHT = 3;
    private static final int DEPTH = 2;

    private NDimensionalPositionFactory positionFactory;

    @Test
    public void whenNoDimensionsShouldReturnEmptyList() {
        positionFactory = new NDimensionalPositionFactory();
        List<Position> positions = positionFactory.createPositions();

        assertEquals(0, positions.size());
    }

    @Test
    public void whenOneDimensionShouldReturnCorrectList() {
        positionFactory = new NDimensionalPositionFactory(WIDTH);
        List<Position> positions = positionFactory.createPositions();

        assertEquals(WIDTH, positions.size());

        for (int i = 0; i < WIDTH; i++) {
            positions.contains(new Position(i));
        }
    }

    @Test
    public void whenTwoDimensionsShouldReturnCorrectList() {
        positionFactory = new NDimensionalPositionFactory(WIDTH, HEIGHT);
        List<Position> positions = positionFactory.createPositions();

        assertEquals(WIDTH * HEIGHT, positions.size());

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                positions.contains(new Position(i, j));
            }
        }
    }

    @Test
    public void whenThreeDimensionsShouldReturnCorrectList() {
        positionFactory = new NDimensionalPositionFactory(WIDTH, HEIGHT, DEPTH);
        List<Position> positions = positionFactory.createPositions();

        assertEquals(WIDTH * HEIGHT * DEPTH, positions.size());

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                for (int k = 0; k < DEPTH; k++) {
                    positions.contains(new Position(i, j, k));
                }
            }
        }
    }
}
