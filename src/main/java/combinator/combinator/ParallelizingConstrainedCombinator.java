package combinator.combinator;

import combinator.datamodel.Combination;
import combinator.datamodel.Constraint;

import java.util.List;
import java.util.concurrent.*;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.Long.MAX_VALUE;
import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static java.util.concurrent.Executors.newFixedThreadPool;

public class ParallelizingConstrainedCombinator extends ConstrainedCombinator {

    private final ParallelizingCombinator combinator;
    private final int numberOfConsumers;
    private final long loggingInterval;
    private final ExecutorCompletionService<Combination> consumerExecutor;
    private final List<Future<Combination>> consumerFutures;
    private final CyclicBarrier consumerCompletionBarrier;

    public ParallelizingConstrainedCombinator(ParallelizingCombinator combinator,
                                              int numberOfConsumers,
                                              long loggingInterval) {
        super(combinator);
        this.combinator = combinator;
        this.numberOfConsumers = numberOfConsumers;
        this.loggingInterval = loggingInterval != 0 ? loggingInterval : MAX_VALUE;
        consumerExecutor = new ExecutorCompletionService<>(newFixedThreadPool(numberOfConsumers));
        consumerFutures = newArrayList();
        consumerCompletionBarrier = new CyclicBarrier(numberOfConsumers);
    }

    public void finit() {
        cancelRunningTasks();
        combinator.finit();
    }

    @Override
    protected Combination searchForSolution(List<Constraint> constraints,
                                            final LinkedBlockingQueue<Combination> combinationsQueue) throws InterruptedException {
        consumerFutures.clear();
        List<CombinationConsumer> runningJobs = newArrayList();
        for (int i = 0; i < numberOfConsumers; i++) {
            final CombinationConsumer consumerJob = new CombinationConsumer(constraints, consumerCompletionBarrier);
            consumerFutures.add(consumerExecutor.submit(new Callable<Combination>() {
                @Override
                public Combination call() throws Exception {
                    return consumerJob.findFirstValidCombination(combinationsQueue);
                }
            }));
            runningJobs.add(consumerJob);
        }

        Combination solution = null;

        long startTime = currentTimeMillis();
        Future<Combination> completedFuture;
        while ((completedFuture = consumerExecutor.poll(loggingInterval, TimeUnit.SECONDS)) == null) {
            logStatus(combinationsQueue, runningJobs, startTime);
        }

        try {
            solution = completedFuture.get();
        } catch (ExecutionException e) {
            fail();
        }

        cancelRunningTasks();

        logStatus(combinationsQueue, runningJobs, startTime);

        return solution;
    }

    private void logStatus(LinkedBlockingQueue<Combination> workQueue, List<CombinationConsumer> runningJobs, long startTime) {
        long totalResultsChecked = 0;
        for (CombinationConsumer runningJob : runningJobs) {
            totalResultsChecked += runningJob.getResultCount();
        }

        double elapsedTime = (currentTimeMillis() - startTime) / ((double) TimeUnit.SECONDS.toMillis(1L));
        System.out.println(format("Checked %,d combinations. Current queue size is %,d. Elapsed time is %f seconds.",
                totalResultsChecked,
                workQueue.size(),
                elapsedTime));
    }

    private void cancelRunningTasks() {
        consumerCompletionBarrier.reset();
        combinator.cancel();
        for (Future<Combination> consumerFuture : consumerFutures) {
            consumerFuture.cancel(true);
        }
    }

    @Override
    protected Combination fail() {
        cancelRunningTasks();
        return super.fail();
    }

    private static class CombinationConsumer extends BaseCombinationChecker {
        private final CyclicBarrier consumerCompletionBarrier;

        private CombinationConsumer(List<Constraint> constraints,
                                    CyclicBarrier consumerCompletionBarrier) {
            super(constraints);
            this.consumerCompletionBarrier = consumerCompletionBarrier;
        }

        @Override
        protected boolean shouldContinue() {
            return !Thread.currentThread().isInterrupted();
        }

        @Override
        protected void endOfDataReached(BlockingQueue<Combination> workQueue) {
            try {
                workQueue.put(ParallelizingCombinator.END_OF_COMBINATIONS);
                consumerCompletionBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}
