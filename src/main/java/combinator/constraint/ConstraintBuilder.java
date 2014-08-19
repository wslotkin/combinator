package combinator.constraint;

import combinator.combinator.Combinator;
import combinator.datamodel.*;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import static com.google.common.collect.Lists.newArrayList;

public class ConstraintBuilder {

    private DataObject object;
    private DataObject[] objects;
    private Validator<Double> distanceValidator;
    private Constraint currentConstraint;
    private Constraint previousConstraint;
    private boolean all;
    private boolean and;

    public ConstraintBuilder restrictObject(DataObject object) {
        clearObjects();
        this.object = object;

        return this;
    }

    public ConstraintBuilder restrictAllObjects(DataObject... objects) {
        clearObjects();
        this.objects = objects;
        all = true;

        return this;
    }

    public ConstraintBuilder restrictAnyObject(DataObject... objects) {
        clearObjects();
        this.objects = objects;
        all = false;

        return this;
    }

    public ConstraintBuilder restrictAllNeighborsOfObject(Validator<Double> distanceValidator, DataObject object) {
        clearObjects();
        this.object = object;
        this.distanceValidator = distanceValidator;
        all = true;

        return this;
    }

    public ConstraintBuilder restrictAnyNeighborsOfObject(Validator<Double> distanceValidator, DataObject object) {
        clearObjects();
        this.object = object;
        this.distanceValidator = distanceValidator;
        all = false;

        return this;
    }

    public ConstraintBuilder restrictAllNeighborsOfObjects(Validator<Double> distanceValidator, DataObject... objects) {
        clearObjects();
        this.objects = objects;
        this.distanceValidator = distanceValidator;
        all = true;

        return this;
    }

    public ConstraintBuilder restrictAnyNeighborsOfObjects(Validator<Double> distanceValidator, DataObject... objects) {
        clearObjects();
        this.objects = objects;
        this.distanceValidator = distanceValidator;
        all = false;

        return this;
    }

    public ConstraintBuilder toPosition(Validator<Position> positionValidator) {
        Constraint newConstraint = null;

        if (object != null) {
            newConstraint = new SingleObjectPositionConstraint(object.getId(), positionValidator);
        } else if (objects != null) {
            List<Constraint> constraints = newArrayList();
            for (DataObject object : objects) {
                constraints.add(new SingleObjectPositionConstraint(object.getId(), positionValidator));
            }
            if (all) {
                newConstraint = new CompositeAllConstraint(constraints);
            } else {
                newConstraint = new CompositeAnyConstraint(constraints);
            }
        }

        buildCurrentConstraint(newConstraint);
        return this;
    }

    public ConstraintBuilder toAttribute(String attribute, Validator<?> attributeValidator) {
        Constraint newConstraint = null;

        if (object != null) {
            newConstraint = createNeighboringConstraint(object.getId(),
                    attribute,
                    attributeValidator,
                    distanceValidator);
        } else if (objects != null) {
            List<Constraint> newConstraints = newArrayList();
            for (DataObject dataObject : objects) {
                newConstraints.add(createNeighboringConstraint(dataObject.getId(),
                        attribute,
                        attributeValidator,
                        distanceValidator));
            }
            newConstraint = new CompositeAllConstraint(newConstraints);
        }

        buildCurrentConstraint(newConstraint);
        return this;
    }

    private Constraint createNeighboringConstraint(String objectId,
                                                   String attribute,
                                                   Validator<?> attributeValidator,
                                                   Validator<Double> distanceValidator) {
        if (all) {
            return new AllNeighboringObjectsConstraint(objectId,
                    attribute,
                    attributeValidator,
                    distanceValidator);
        } else {
            return new AnyNeighboringObjectsConstraint(objectId,
                    attribute,
                    attributeValidator,
                    this.distanceValidator);
        }
    }

    public ConstraintBuilder toDistance(Validator<Double> distanceValidator) {
        Constraint newConstraint = null;

        if (objects != null) {
            List<Constraint> constraints = newArrayList();
            for (Combination pair : getPairs(objects)) {
                List<DataObject> pairObjects = pair.getObjects();
                String firstObjectId = pairObjects.get(0).getId();
                String secondObjectId = pairObjects.get(1).getId();
                constraints.add(new ObjectPairDistanceConstraint(firstObjectId, secondObjectId, distanceValidator));
            }
            if (all) {
                newConstraint = new CompositeAllConstraint(constraints);
            } else {
                newConstraint = new CompositeAnyConstraint(constraints);
            }
        }

        buildCurrentConstraint(newConstraint);
        return this;
    }

    public ConstraintBuilder and() {
        previousConstraint = currentConstraint;
        currentConstraint = null;
        and = true;

        return this;
    }

    public ConstraintBuilder or() {
        previousConstraint = currentConstraint;
        currentConstraint = null;
        and = false;

        return this;
    }

    public Constraint build() {
        return currentConstraint;
    }

    private Collection<Combination> getPairs(DataObject[] objects) {
        List<Position> twoPositions = newArrayList(new Position(0), new Position(1));
        LinkedBlockingQueue<Combination> combinations = new LinkedBlockingQueue<>();
        try {
            new Combinator().generateCombinations(newArrayList(objects), twoPositions, combinations);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return combinations;
    }

    private void buildCurrentConstraint(Constraint newConstraint) {
        if (previousConstraint != null) {
            if (and) {
                currentConstraint = new CompositeAllConstraint(newArrayList(previousConstraint, newConstraint));
            } else {
                currentConstraint = new CompositeAnyConstraint(newArrayList(previousConstraint, newConstraint));
            }
        } else {
            currentConstraint = newConstraint;
        }
        previousConstraint = null;
        clearObjects();
    }

    private void clearObjects() {
        object = null;
        objects = null;
    }
}
