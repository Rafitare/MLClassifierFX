package models;

import domain.Instance;
import domain.Model;

import java.util.ArrayList;
import java.util.List;

public class Perceptron implements Model<Double, Integer> {

    private List<Double> weights;
    private double bias;
    private final int maxGenerations;
    private final double learningRate;


    public Perceptron(int maxGenerations, double learningRate) {
        this.weights = new ArrayList<>();
        this.bias = 0.0;
        this.maxGenerations = maxGenerations;
        this.learningRate = learningRate;
    }

    @Override
    public void train(List<Instance<Double, Integer>> instances) {
        // initialize weights
        int nrOfFeatures = instances.get(0).getInput().size();
        weights = new ArrayList<>(nrOfFeatures);

        for (int i = 0; i < nrOfFeatures; i++) {
            weights.add(0.0);
        }

        for (int genNr = 0; genNr < maxGenerations; genNr++) {
            for (Instance<Double, Integer> instance : instances) {
                int prediction = predict(instance.getInput());
                int label = instance.getOutput();
                int error = label - prediction;

                for (int i = 0; i < weights.size(); i++) {
                    double newWeight = weights.get(i) + learningRate * error * instance.getInput().get(i);
                    weights.set(i, newWeight);
                }
                bias += learningRate * error; // Update bias term
            }
        }
        System.out.println("Training with Perceptron complete.");
    }

    @Override
    public List<Integer> test(List<Instance<Double, Integer>> instances) {
        List<Integer> predictions = new ArrayList<>();

        for (Instance<Double, Integer> instance : instances) {
            predictions.add(predict(instance.getInput()));
        }
        return predictions;
    }

    // predict the label of an example based on bias and weights
    public int predict(List<Double> input) {
        double sum = bias;

        for (int i = 0; i < weights.size(); i++) {
            sum += weights.get(i) * input.get(i);
        }

        if (sum > 0) {
            return 1;
        } else {
            return 0;
        }
    }
}
