package combinator.combinator;

import combinator.datamodel.Combination;
import combinator.datamodel.Constraint;

import java.util.List;
import java.util.concurrent.BlockingQueue;

abstract class BaseCombinationChecker {

    private final List<Constraint> constraints;
    private volatile long resultCount;

    public BaseCombinationChecker(List<Constraint> constraints) {
        this.constraints = constraints;
    }

    public Combination findFirstValidCombination(BlockingQueue<Combination> workQueue) throws InterruptedException {
        while (shouldContinue()) {
            Combination nextCombination = workQueue.take();
            resultCount++;

            if (nextCombination == ParallelizingCombinator.END_OF_COMBINATIONS) {
                endOfDataReached(workQueue);
                break;
            } else if (satisfiesAllConstraints(nextCombination, constraints)) {
                return nextCombination;
            }
        }
        return null;
    }

    protected abstract boolean shouldContinue();

    protected abstract void endOfDataReached(BlockingQueue<Combination> workQueue);

    private boolean satisfiesAllConstraints(Combination combination, List<Constraint> constraints) {
        for (Constraint constraint : constraints) {
            if (!constraint.isSatisfied(combination)) return false;
        }
        return true;
    }

    public long getResultCount() {
        return resultCount;
    }
}
