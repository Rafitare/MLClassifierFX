package evaluation;

import modelDomain.Instance;

import java.util.List;

@FunctionalInterface
public interface EvaluationMeasure<F, L> {
    double evaluate(List<Instance<F, L>> instances, List<L> predictions);
}
