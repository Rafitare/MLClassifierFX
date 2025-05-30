package evaluation;

import domain.Instance;

import java.util.List;

public class Recall<F, L> implements EvaluationMeasure<F, L> {

    @Override
    public double evaluate(List<Instance<F, L>> instances, List<L> predictions) {
        int truePositives = 0;
        int falseNegatives = 0;

        for (int i = 0; i < instances.size(); i++) {
            if (instances.get(i).getOutput().equals(predictions.get(i))) {
                if (predictions.get(i).equals(1)) {
                    truePositives++;
                }
            } else {
                if (instances.get(i).getOutput().equals(1)) {
                    falseNegatives++;
                }
            }
        }

        // Recall is the ratio of true positives to the sum of true positives and false negatives
        if (truePositives + falseNegatives == 0) {
            return 0.0; // Avoid division by zero
        }
        return (double) truePositives / (truePositives + falseNegatives);
    }
}