package models;

import domain.Instance;
import domain.Model;

import java.util.*;

public class DecisionTree implements Model<Double, Integer> {
    private Node root;

    private static class Node {
        int featureIndex;
        double threshold;
        Node left;
        Node right;
        Integer label;

        Node(int featureIndex, double threshold) {
            this.featureIndex = featureIndex;
            this.threshold = threshold;
        }

        Node(Integer label) {
            this.label = label;
        }
    }

    @Override
    public void train(List<Instance<Double, Integer>> instances) {
        root = buildTree(instances, 0);
        System.out.println("Training with Decision Tree complete.");
    }

    private Node buildTree(List<Instance<Double, Integer>> instances, int depth) {
        if (instances.isEmpty()) {
            return new Node(0); // Default label
        }

        if (instances.stream().allMatch(i -> i.getOutput().equals(instances.get(0).getOutput()))) {
            return new Node(instances.get(0).getOutput());
        }

        int numFeatures = instances.get(0).getInput().size();
        int bestFeature = -1;
        double bestThreshold = 0;
        double bestGain = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < numFeatures; i++) {
            Set<Double> values = new TreeSet<>();
            for (Instance<Double, Integer> instance : instances) {
                values.add(instance.getInput().get(i));
            }
            List<Double> sortedValues = new ArrayList<>(values);
            for (int j = 1; j < sortedValues.size(); j++) {
                double threshold = (sortedValues.get(j - 1) + sortedValues.get(j)) / 2.0;
                double gain = informationGain(instances, i, threshold);
                if (gain > bestGain) {
                    bestGain = gain;
                    bestFeature = i;
                    bestThreshold = threshold;
                }
            }
        }

        if (bestFeature == -1) {
            return new Node(majorityLabel(instances));
        }

        Node node = new Node(bestFeature, bestThreshold);
        List<Instance<Double, Integer>> left = new ArrayList<>();
        List<Instance<Double, Integer>> right = new ArrayList<>();

        for (Instance<Double, Integer> instance : instances) {
            if (instance.getInput().get(bestFeature) <= bestThreshold) {
                left.add(instance);
            } else {
                right.add(instance);
            }
        }

        node.left = buildTree(left, depth + 1);
        node.right = buildTree(right, depth + 1);
        return node;
    }

    private double informationGain(List<Instance<Double, Integer>> instances, int featureIndex, double threshold) {
        double entropyBefore = entropy(instances);
        List<Instance<Double, Integer>> left = new ArrayList<>();
        List<Instance<Double, Integer>> right = new ArrayList<>();

        for (Instance<Double, Integer> instance : instances) {
            if (instance.getInput().get(featureIndex) <= threshold) {
                left.add(instance);
            } else {
                right.add(instance);
            }
        }

        double weightedEntropy = 0.0;
        if (!left.isEmpty()) {
            weightedEntropy += ((double) left.size() / instances.size()) * entropy(left);
        }
        if (!right.isEmpty()) {
            weightedEntropy += ((double) right.size() / instances.size()) * entropy(right);
        }

        return entropyBefore - weightedEntropy;
    }

    private double entropy(List<Instance<Double, Integer>> instances) {
        Map<Integer, Integer> labelCounts = new HashMap<>();
        for (Instance<Double, Integer> instance : instances) {
            labelCounts.put(instance.getOutput(), labelCounts.getOrDefault(instance.getOutput(), 0) + 1);
        }

        double entropy = 0.0;
        for (int count : labelCounts.values()) {
            double probability = (double) count / instances.size();
            entropy -= probability * Math.log(probability) / Math.log(2);
        }

        return entropy;
    }

    private int majorityLabel(List<Instance<Double, Integer>> instances) {
        if (instances.isEmpty()) {
            return 0; // default fallback
        }

        Map<Integer, Integer> labelCounts = new HashMap<>();
        for (Instance<Double, Integer> instance : instances) {
            labelCounts.put(instance.getOutput(), labelCounts.getOrDefault(instance.getOutput(), 0) + 1);
        }

        return labelCounts.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
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
        Node current = root;
        while (current.label == null) {
            if (current.featureIndex >= input.size()) {
                return 0; // fallback
            }
            double featureValue = input.get(current.featureIndex);
            if (featureValue <= current.threshold) {
                current = current.left;
            } else {
                current = current.right;
            }
            if (current == null) {
                return 0; // fallback
            }
        }
        return current.label;
    }
}
