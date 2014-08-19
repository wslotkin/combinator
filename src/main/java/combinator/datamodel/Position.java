package combinator.datamodel;

import java.util.Arrays;

import static java.lang.Integer.compare;
import static java.lang.Math.sqrt;

public class Position implements Comparable<Position> {

    private final int[] coordinates;

    public Position(int... coordinates) {
        this.coordinates = coordinates;
    }

    public int[] getCoordinates() {
        return coordinates;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Position position = (Position) object;

        if (!Arrays.equals(coordinates, position.coordinates)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return coordinates != null ? Arrays.hashCode(coordinates) : 0;
    }

    @Override
    public int compareTo(Position otherPosition) {
        int[] otherCoordinates = otherPosition.getCoordinates();

        assert coordinates.length == otherCoordinates.length;

        for (int i = 0; i < coordinates.length; i++) {
            int coordinate = coordinates[i];
            int otherCoordinate = otherCoordinates[i];
            int compare = compare(coordinate, otherCoordinate);
            if (compare != 0) return compare;
        }

        return 0;
    }

    @Override
    public String toString() {
        return Arrays.toString(coordinates);
    }

    public static double distance(Position left, Position right) {
        double distance = 0.0;

        for (int i = 0; i < left.coordinates.length; i++) {
            int leftCoordinate = left.coordinates[i];
            int rightCoordinate = right.coordinates[i];
            double difference = leftCoordinate - rightCoordinate;
            distance += (difference * difference);
        }

        return sqrt(distance);
    }
}
