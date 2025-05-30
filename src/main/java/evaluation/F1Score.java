package evaluation;

import domain.Instance;

import java.util.List;

public class F1Score<F, L> implements EvaluationMeasure<F, L> {

    @Override
    public double evaluate(List<Instance<F, L>> instances, List<L> predictions) {
        int truePositives = 0;
        int falsePositives = 0;
        int falseNegatives = 0;

        for (int i = 0; i < instances.size(); i++) {
            if (predictions.get(i).equals(instances.get(i).getOutput())) {
                if (predictions.get(i).equals(1)) {
                    truePositives++;
                }
            } else {
                if (predictions.get(i).equals(1)) {
                    falsePositives++;
                } else if (instances.get(i).getOutput().equals(1)) {
                    falseNegatives++;
                }
            }
        }

        // Calculate precision and recall
        double precision = truePositives + falsePositives == 0 ? 0.0 : (double) truePositives / (truePositives + falsePositives);
        double recall = truePositives + falseNegatives == 0 ? 0.0 : (double) truePositives / (truePositives + falseNegatives);

        // Calculate F1 Score. F1 Score is the harmonic mean of precision and recall
        return 2 * (precision * recall) / (precision + recall);
    }
}