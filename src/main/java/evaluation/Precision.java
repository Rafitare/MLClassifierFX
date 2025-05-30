package evaluation;

import domain.Instance;

import java.util.List;

public class Precision<F, L> implements EvaluationMeasure<F, L> {

    @Override
    public double evaluate(List<Instance<F, L>> instances, List<L> predictions) {
        int truePositives = 0;
        int falsePositives = 0;

        for (int i = 0; i < instances.size(); i++) {
            if (predictions.get(i).equals(instances.get(i).getOutput())) {
                if (predictions.get(i).equals(1)) {
                    truePositives++;
                }
            } else {
                if (predictions.get(i).equals(1)) {
                    falsePositives++;
                }
            }
        }

        int denominator = truePositives + falsePositives;

        // avoid division by zero
        // the denominator is zero if there are no true positives and no false positives
        if (denominator == 0) {
            return 0.0;
        }

        return (double) truePositives / (truePositives + falsePositives);
    }
}