package combinator.combinator;

import combinator.UnsatisfiedConstraintException;
import combinator.datamodel.Combination;
import combinator.datamodel.Constraint;
import combinator.datamodel.DataObject;
import combinator.datamodel.Position;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public class ConstrainedCombinatorTest {

    private static final List<DataObject> DATA_OBJECTS = newArrayList();
    private static final List<Position> POSITIONS = newArrayList();

    private Combinator combinator;
    private Combination invalidCombination;
    private Combination validCombination;
    private List<Constraint> constraints;
    private Constraint constraint;
    private ConstrainedCombinator constrainedCombinator;
    private LinkedBlockingQueue<Combination> combinationQueue;

    @Before
    public void before() throws InterruptedException {
        combinationQueue = new LinkedBlockingQueue<>();
        invalidCombination = mock(Combination.class);
        validCombination = mock(Combination.class);

        constraint = mock(Constraint.class);
        when(constraint.isSatisfied(invalidCombination)).thenReturn(false);
        when(constraint.isSatisfied(validCombination)).thenReturn(true);
        constraints = newArrayList(constraint);

        combinator = mock(Combinator.class);
        when(combinator.generateCombinations(DATA_OBJECTS, POSITIONS, combinationQueue)).thenAnswer(new Answer<LinkedBlockingQueue<Combination>>() {
            @Override
            public LinkedBlockingQueue<Combination> answer(InvocationOnMock invocationOnMock) throws Throwable {
                combinationQueue.put(invalidCombination);
                combinationQueue.put(validCombination);
                return combinationQueue;
            }
        });

        constrainedCombinator = new ConstrainedCombinator(combinator);
    }

    @Test
    public void shouldFindOnlyValidCombination() throws InterruptedException {
        Combination result = constrainedCombinator.findValidCombination(constraints, DATA_OBJECTS, POSITIONS, combinationQueue);

        assertSame(validCombination, result);

        verify(combinator).generateCombinations(DATA_OBJECTS, POSITIONS, combinationQueue);
        verify(constraint).isSatisfied(invalidCombination);
        verify(constraint).isSatisfied(validCombination);
    }

    @Test
    public void shouldFindFirstValidCombination() throws InterruptedException {
        List<Constraint> noConstraints = newArrayList();
        Combination result = constrainedCombinator.findValidCombination(noConstraints, DATA_OBJECTS, POSITIONS, combinationQueue);

        assertSame(invalidCombination, result);

        verify(combinator).generateCombinations(DATA_OBJECTS, POSITIONS, combinationQueue);
    }

    @Test(expected = UnsatisfiedConstraintException.class)
    public void shouldThrowExceptionWhenThereAreNoValidCombinations() throws InterruptedException {
        Constraint failAllConstraint = new Constraint() {
            @Override
            public boolean isSatisfied(Combination dataSet) {
                return false;
            }
        };
        constraints.add(failAllConstraint);
        constrainedCombinator.findValidCombination(constraints, DATA_OBJECTS, POSITIONS, combinationQueue);
    }
}
