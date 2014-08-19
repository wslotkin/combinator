package combinator.datamodel;

import java.util.Map;

public class DataObject {

    private final String id;
    private final Map<String, ? extends Comparable> attributes;

    public DataObject(String id, Map<String, ? extends Comparable> attributes) {
        this.id = id;
        this.attributes = attributes;
    }

    public String getId() {
        return id;
    }

    public Comparable getAttribute(String attribute) {
        return attributes.get(attribute);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataObject that = (DataObject) o;

        if (!id.equals(that.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return id;
    }
}
