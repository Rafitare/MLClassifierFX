package models;

import domain.Instance;
import domain.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NaiveBayes implements Model<Double, Integer> {
    private Map<Integer, Double> classProbabilities;
    private Map<Integer, List<Map<Double, Double>>> featureProbabilities;

    public NaiveBayes() {
        this.classProbabilities = new HashMap<>();
        this.featureProbabilities = new HashMap<>();
    }

    @Override
    public void train(List<Instance<Double, Integer>> instances) {
        Map<Integer, Integer> classCounts = new HashMap<>();
        Map<Integer, List<Map<Double, Integer>>> featureCounts = new HashMap<>();
        int numFeatures = instances.get(0).getInput().size();

        // Initialize counts
        for (Instance<Double, Integer> instance : instances) {
            int label = instance.getOutput();
            classCounts.put(label, classCounts.getOrDefault(label, 0) + 1);
            if (!featureCounts.containsKey(label)) {
                featureCounts.put(label, new ArrayList<>());
                for (int i = 0; i < numFeatures; i++) {
                    featureCounts.get(label).add(new HashMap<>());
                }
            }

            List<Double> input = instance.getInput();
            for (int i = 0; i < numFeatures; i++) {
                double featureValue = input.get(i);
                Map<Double, Integer> featureCount = featureCounts.get(label).get(i);
                featureCount.put(featureValue, featureCount.getOrDefault(featureValue, 0) + 1);
            }
        }

        // Calculate class probabilities
        int totalInstances = instances.size();
        for (Map.Entry<Integer, Integer> entry : classCounts.entrySet()) {
            int label = entry.getKey();
            int count = entry.getValue();
            classProbabilities.put(label, (double) count / totalInstances);
        }

        // Calculate feature probabilities
        for (Map.Entry<Integer, List<Map<Double, Integer>>> entry : featureCounts.entrySet()) {
            int label = entry.getKey();
            List<Map<Double, Integer>> featureCountList = entry.getValue();

            if (!featureProbabilities.containsKey(label)) {
                featureProbabilities.put(label, new ArrayList<>());
            }

            for (Map<Double, Integer> featureCount : featureCountList) {
                Map<Double, Double> featureProbability = new HashMap<>();
                int totalFeatureCount = featureCount.values().stream().mapToInt(Integer::intValue).sum();

                for (Map.Entry<Double, Integer> featureEntry : featureCount.entrySet()) {
                    double featureValue = featureEntry.getKey();
                    int count = featureEntry.getValue();
                    featureProbability.put(featureValue, (double) count / totalFeatureCount);
                }

                featureProbabilities.get(label).add(featureProbability);
            }
        }

        System.out.println("Training with Naive Bayes complete.");
    }

    @Override
    public List<Integer> test(List<Instance<Double, Integer>> instances) {
        List<Integer> predictions = new ArrayList<>();

        for (Instance<Double, Integer> instance : instances) {
            predictions.add(predict(instance.getInput()));
        }

        return predictions;
    }

    public int predict(List<Double> input) {
        double maxProbability = Double.NEGATIVE_INFINITY;
        int bestLabel = -1;

        for (Map.Entry<Integer, Double> entry : classProbabilities.entrySet()) {
            int label = entry.getKey();
            double classProbability = entry.getValue();
            double probability = Math.log(classProbability);

            for (int i = 0; i < input.size(); i++) {
                double featureValue = input.get(i);
                Map<Double, Double> featureProbability = featureProbabilities.get(label).get(i);
                probability += Math.log(featureProbability.getOrDefault(featureValue, 1e-6)); // Smoothing
            }

            if (probability > maxProbability) {
                maxProbability = probability;
                bestLabel = label;
            }
        }

        return bestLabel;
    }
}