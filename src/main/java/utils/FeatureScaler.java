package utils;

import domain.Instance;

import java.util.List;

public class FeatureScaler {
    public static void minMaxScale(List<Instance<Double, Integer>> instances) {
        int numFeatures = instances.get(0).getInput().size();
        double[] minValues = new double[numFeatures];
        double[] maxValues = new double[numFeatures];

        // Initialize min and max values
        for (int i = 0; i < numFeatures; i++) {
            minValues[i] = Double.MAX_VALUE;
            maxValues[i] = Double.MIN_VALUE;
        }

        // Find min and max values for each feature
        for (Instance<Double, Integer> instance : instances) {
            List<Double> input = instance.getInput();
            for (int i = 0; i < numFeatures; i++) {
                if (input.get(i) < minValues[i]) {
                    minValues[i] = input.get(i);
                }
                if (input.get(i) > maxValues[i]) {
                    maxValues[i] = input.get(i);
                }
            }
        }

        // Scale features to [0, 1] range
        for (Instance<Double, Integer> instance : instances) {
            List<Double> input = instance.getInput();
            for (int i = 0; i < numFeatures; i++) {
                double scaledValue = (input.get(i) - minValues[i]) / (maxValues[i] - minValues[i]);
                input.set(i, scaledValue);
            }
        }
    }
}