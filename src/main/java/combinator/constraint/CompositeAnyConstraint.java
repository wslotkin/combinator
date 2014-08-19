package combinator.constraint;

import combinator.datamodel.Combination;
import combinator.datamodel.Constraint;

import java.util.List;

class CompositeAnyConstraint implements Constraint {

    private final List<Constraint> constraints;

    public CompositeAnyConstraint(List<Constraint> constraints) {
        this.constraints = constraints;
    }

    @Override
    public boolean isSatisfied(Combination combination) {
        for (Constraint constraint : constraints) {
            if (constraint.isSatisfied(combination)) return true;
        }

        return false;
    }

}
