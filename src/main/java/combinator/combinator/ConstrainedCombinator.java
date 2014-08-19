package combinator.combinator;

import combinator.UnsatisfiedConstraintException;
import combinator.datamodel.Combination;
import combinator.datamodel.Constraint;
import combinator.datamodel.DataObject;
import combinator.datamodel.Position;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ConstrainedCombinator {

    private final Combinator combinator;

    public ConstrainedCombinator(Combinator combinator) {
        this.combinator = combinator;
    }

    public Combination findValidCombination(List<Constraint> constraints,
                                            List<DataObject> dataObjects,
                                            List<Position> positions,
                                            final LinkedBlockingQueue<Combination> combinationsQueue) throws InterruptedException {
        combinator.generateCombinations(dataObjects,
                positions,
                combinationsQueue);

        Combination solution = searchForSolution(constraints, combinationsQueue);

        if (solution == null) fail();

        return solution;
    }

    protected Combination searchForSolution(final List<Constraint> constraints, final LinkedBlockingQueue<Combination> combinationsQueue) throws InterruptedException {
        BaseCombinationChecker combinationChecker = new BaseCombinationChecker(constraints) {
            @Override
            protected boolean shouldContinue() {
                return !combinationsQueue.isEmpty();
            }

            @Override
            protected void endOfDataReached(BlockingQueue<Combination> workQueue) {

            }
        };

        return combinationChecker.findFirstValidCombination(combinationsQueue);
    }

    protected Combination fail() {
        String errorMessage = "Unable to find any valid combination";
        throw new UnsatisfiedConstraintException(errorMessage);
    }
}
