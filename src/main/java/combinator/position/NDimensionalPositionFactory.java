package combinator.position;

import combinator.datamodel.Position;
import combinator.datamodel.PositionFactory;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.copyOf;
import static java.util.Arrays.copyOfRange;

public class NDimensionalPositionFactory implements PositionFactory {

    private final int[] dimensions;

    public NDimensionalPositionFactory(int... dimensions) {
        this.dimensions = dimensions;
    }

    @Override
    public List<Position> createPositions() {
        List<Position> positionList = new ArrayList<>();
        return positions(positionList, dimensions, new int[0]);
    }

    private List<Position> positions(List<Position> runningPositionList, int[] dimensions, int[] runningPosition) {
        if (dimensions.length == 0) {
            if (runningPosition.length != 0) runningPositionList.add(new Position(runningPosition));
        } else {
            int dimension = dimensions[0];
            for (int i = 0; i < dimension; i++) {
                int[] updatedPosition = copyOf(runningPosition, runningPosition.length + 1);
                updatedPosition[runningPosition.length] = i;

                int[] updatedDimensions = copyOfRange(dimensions, 1, dimensions.length);

                positions(runningPositionList, updatedDimensions, updatedPosition);
            }
        }

        return runningPositionList;
    }
}
