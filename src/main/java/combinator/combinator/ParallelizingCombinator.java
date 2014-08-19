package combinator.combinator;

import combinator.datamodel.Combination;
import combinator.datamodel.DataObject;
import combinator.datamodel.Position;

import java.util.List;
import java.util.concurrent.*;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.Math.min;
import static java.util.concurrent.Executors.newFixedThreadPool;

public class ParallelizingCombinator extends Combinator {

    public static final Combination END_OF_COMBINATIONS = new Combination();

    private final ExecutorService executorService;
    private final List<Future<BlockingQueue<Combination>>> futures;
    private CountDownLatch countDownLatch;
    private Future<BlockingQueue<Combination>> terminationTask;

    public ParallelizingCombinator(int numberOfCombinatorThreads) {
        executorService = newFixedThreadPool(numberOfCombinatorThreads + 1);
        futures = newArrayList();
    }

    public void cancel() {
        for (Future<BlockingQueue<Combination>> future : futures) {
            future.cancel(true);
        }
        while (!terminationTask.isDone()) {
            countDownLatch.countDown();
        }
    }

    public void finit() {
        executorService.shutdown();
    }

    @Override
    public BlockingQueue<Combination> generateCombinations(List<DataObject> dataObjects, List<Position> positions, BlockingQueue<Combination> resultsQueue) throws InterruptedException {
        countDownLatch = new CountDownLatch(min(dataObjects.size(), positions.size()));
        final BlockingQueue<Combination> combinations = super.generateCombinations(dataObjects, positions, resultsQueue);
        startTerminationTask(combinations);

        return combinations;
    }

    private void startTerminationTask(final BlockingQueue<Combination> combinations) {
        terminationTask = executorService.submit(new Callable<BlockingQueue<Combination>>() {
            @Override
            public BlockingQueue<Combination> call() throws Exception {
                countDownLatch.await();
                combinations.put(END_OF_COMBINATIONS);
                return null;
            }
        });
    }

    @Override
    protected BlockingQueue<Combination> permuteWithEqualOrMorePositionsThanObjects(final List<DataObject> remainingObjects,
                                                                                    final List<Position> remainingPositions,
                                                                                    final Combination currentCombination,
                                                                                    final BlockingQueue<Combination> runningList) throws InterruptedException {
        if (Thread.currentThread().isInterrupted()) return null;

        if (currentCombination.getPositions().size() == 1) {
            Callable<BlockingQueue<Combination>> combinatorJob = new Callable<BlockingQueue<Combination>>() {
                @Override
                public BlockingQueue<Combination> call() throws Exception {
                    BlockingQueue<Combination> combinations = ParallelizingCombinator.super.permuteWithEqualOrMorePositionsThanObjects(remainingObjects,
                            remainingPositions,
                            currentCombination,
                            runningList);
                    countDownLatch.countDown();
                    return combinations;
                }
            };
            futures.add(executorService.submit(combinatorJob));
            return null;
        } else {
            return super.permuteWithEqualOrMorePositionsThanObjects(remainingObjects,
                    remainingPositions,
                    currentCombination,
                    runningList);
        }
    }

    @Override
    protected BlockingQueue<Combination> permuteWithFewerPositionsThanObjects(final List<DataObject> remainingObjects,
                                                                              final List<Position> remainingPositions,
                                                                              final Combination currentCombination,
                                                                              final BlockingQueue<Combination> runningList) throws InterruptedException {
        if (Thread.currentThread().isInterrupted()) return null;
        if (currentCombination.getPositions().size() == 1) {
            Callable<BlockingQueue<Combination>> combinatorJob = new Callable<BlockingQueue<Combination>>() {
                @Override
                public BlockingQueue<Combination> call() throws Exception {
                    BlockingQueue<Combination> combinations = ParallelizingCombinator.super.permuteWithFewerPositionsThanObjects(remainingObjects,
                            remainingPositions,
                            currentCombination,
                            runningList);
                    countDownLatch.countDown();
                    return combinations;
                }
            };
            futures.add(executorService.submit(combinatorJob));
            return null;
        } else {
            return super.permuteWithFewerPositionsThanObjects(remainingObjects,
                    remainingPositions,
                    currentCombination,
                    runningList);
        }
    }
}
