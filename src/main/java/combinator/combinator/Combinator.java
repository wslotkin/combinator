package combinator.combinator;

import combinator.datamodel.Combination;
import combinator.datamodel.DataObject;
import combinator.datamodel.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Combinator {

    public BlockingQueue<Combination> generateCombinations(List<DataObject> dataObjects,
                                                           List<Position> positions,
                                                           BlockingQueue<Combination> resultsQueue) throws InterruptedException {
        if (positions.size() < dataObjects.size()) {
            return permuteWithFewerPositionsThanObjects(dataObjects,
                    positions,
                    new Combination(),
                    resultsQueue);
        } else {
            return permuteWithEqualOrMorePositionsThanObjects(dataObjects,
                    positions,
                    new Combination(),
                    resultsQueue);
        }
    }

    protected BlockingQueue<Combination> permuteWithEqualOrMorePositionsThanObjects(List<DataObject> remainingObjects,
                                                                                  List<Position> remainingPositions,
                                                                                  Combination currentCombination,
                                                                                  BlockingQueue<Combination> runningList) throws InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            return null;
        } else if (remainingObjects.isEmpty()) {
            if (!currentCombination.isEmpty()) runningList.put(currentCombination);
        } else {
            List<DataObject> updatedRemainingObjects = new ArrayList<>(remainingObjects);
            DataObject remainingObject = remainingObjects.get(0);
            updatedRemainingObjects.remove(remainingObject);
            for (Position remainingPosition : remainingPositions) {
                List<Position> updatedRemainingPositions = new ArrayList<>(remainingPositions);
                updatedRemainingPositions.remove(remainingPosition);

                Combination updatedCombination = new Combination(currentCombination);
                updatedCombination.addMapping(remainingPosition, remainingObject);

                permuteWithEqualOrMorePositionsThanObjects(updatedRemainingObjects,
                        updatedRemainingPositions,
                        updatedCombination,
                        runningList);
            }
        }

        return runningList;
    }

    protected BlockingQueue<Combination> permuteWithFewerPositionsThanObjects(List<DataObject> remainingObjects,
                                                                            List<Position> remainingPositions,
                                                                            Combination currentCombination,
                                                                            BlockingQueue<Combination> runningList) throws InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            return null;
        } else if (remainingPositions.isEmpty()) {
            if (!currentCombination.isEmpty()) runningList.put(currentCombination);
        } else {
            List<Position> updatedRemainingPositions = new ArrayList<>(remainingPositions);
            Position remainingPosition = remainingPositions.get(0);
            updatedRemainingPositions.remove(remainingPosition);
            for (DataObject remainingObject : remainingObjects) {

                List<DataObject> updatedRemainingObjects = new ArrayList<>(remainingObjects);
                updatedRemainingObjects.remove(remainingObject);

                Combination updatedCombination = new Combination(currentCombination);
                updatedCombination.addMapping(remainingPosition, remainingObject);

                permuteWithFewerPositionsThanObjects(updatedRemainingObjects,
                        updatedRemainingPositions,
                        updatedCombination,
                        runningList);
            }
        }

        return runningList;
    }
}
