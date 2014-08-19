package combinator.datamodel;

import java.util.*;

import static com.google.common.collect.Lists.newArrayList;

public class Combination {

    private final Map<String, Position> idToPositionMappings;
    private final Map<Position, DataObject> positionToObjectMappings;

    public Combination() {
        this(new HashMap<String, Position>(), new HashMap<Position, DataObject>());
    }

    public Combination(Combination combination) {
        this(new HashMap<>(combination.idToPositionMappings), new HashMap<>(combination.positionToObjectMappings));
    }

    public Combination(Map<String, Position> idToPositionMappings,
                       Map<Position, DataObject> positionToObjectMappings) {
        this.idToPositionMappings = idToPositionMappings;
        this.positionToObjectMappings = positionToObjectMappings;
    }

    public void addMapping(Position position, DataObject dataObject) {
        idToPositionMappings.put(dataObject.getId(), position);
        positionToObjectMappings.put(position, dataObject);
    }

    public DataObject getObjectForPosition(Position position) {
        return positionToObjectMappings.get(position);
    }

    public Position getPositionForId(String id) {
        return idToPositionMappings.get(id);
    }

    public List<Position> getPositions() {
        return newArrayList(positionToObjectMappings.keySet());
    }

    public List<String> getIds() {
        return newArrayList(idToPositionMappings.keySet());
    }

    public List<DataObject> getObjects() {
        return newArrayList(positionToObjectMappings.values());
    }

    public boolean isEmpty() {
        return positionToObjectMappings.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Combination combination = (Combination) o;

        if (!idToPositionMappings.equals(combination.idToPositionMappings)) return false;
        if (!positionToObjectMappings.equals(combination.positionToObjectMappings)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = idToPositionMappings.hashCode();
        result = 31 * result + positionToObjectMappings.hashCode();
        return result;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        List<Position> list = new ArrayList<>(positionToObjectMappings.keySet());
        Collections.sort(list);

        for (Position position : list) {
            if (stringBuilder.length() == 0) {
                stringBuilder.append("Combination: ");
            } else {
                stringBuilder.append(", ");
            }

            stringBuilder.append("(")
                    .append(position.toString())
                    .append(", ")
                    .append(positionToObjectMappings.get(position).toString())
                    .append(")");
        }

        return stringBuilder.toString();
    }
}
