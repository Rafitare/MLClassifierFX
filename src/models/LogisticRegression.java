package models;

import modelDomain.Instance;
import modelDomain.Model;

import java.util.ArrayList;
import java.util.List;

public class LogisticRegression implements Model<Double, Integer> {
    private List<Double> weights;
    private double bias;
    private final int maxIterations;
    private final double learningRate;

    public LogisticRegression(int maxIterations, double learningRate) {
        this.weights = new ArrayList<>();
        this.bias = 0.0;
        this.maxIterations = maxIterations;
        this.learningRate = learningRate;
    }

    @Override
    public void train(List<Instance<Double, Integer>> instances) {
        // initialize weights
        int numFeatures = instances.getFirst().getInput().size();
        weights = new ArrayList<>(numFeatures);

        for (int i = 0; i < numFeatures; i++) {
            weights.add(0.0);
        }

        for (int iterNr = 0; iterNr < maxIterations; iterNr++) {
            for (Instance<Double, Integer> instance : instances) {
                List<Double> input = instance.getInput();
                double prediction = predictProbability(input);
                int label = instance.getOutput();
                double error = label - prediction;

                // Update weights
                for (int i = 0; i < numFeatures; i++) {
                    double newWeight = weights.get(i) + learningRate * error * input.get(i);
                    weights.set(i, newWeight);
                }
                bias += learningRate * error; // Update bias term
            }
        }
        System.out.println("Training with Logistic Regression complete.");
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
        if (predictProbability(input) >= 0.5) {
            return 1;
        } else {
            return 0;
        }
    }

    private double predictProbability(List<Double> input) {
        double linearSum = bias; // Start with the bias term
        for (int i = 0; i < input.size(); i++) {
            linearSum += weights.get(i) * input.get(i);
        }
        // map the linear sum to [0, 1] using the sigmoid function
        return sigmoid(linearSum);
    }

    // Sigmoid function for logistic regression
    private double sigmoid(double z) {
        return 1.0 / (1.0 + Math.exp(-z));
    }
}