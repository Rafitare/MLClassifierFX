package models;

import modelDomain.Instance;
import modelDomain.Model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class KNN implements Model <Double, Integer> {
    // k is the number of neighbors to consider
    private final int k;
    private List<Instance<Double, Integer>> trainingInstances;

    public KNN(int k) {
        this.k = k;
    }


    @Override
    public void train(List<Instance<Double, Integer>> instances) {
        this.trainingInstances = instances;
        System.out.println("Training with KNN complete.");
    }


    @Override
    public List<Integer> test(List<Instance<Double, Integer>> instances) {
        List<Integer> predictions = new ArrayList<>();

        for (Instance<Double, Integer> instance : instances) {
            predictions.add(predict(instance.getInput()));
        }

        return predictions;
    }

    public double distance(List<Double> a, List<Double> b){
        double sum = 0;
        for (int i = 0; i < a.size(); i++) {
            sum += (a.get(i) - b.get(i)) * (a.get(i) - b.get(i));
        }
        return Math.sqrt(sum);
    }

    public int predict(List<Double> input) {
        // priority queue to store training instances ordered by their distance to the test instance
        PriorityQueue<Instance<Double, Integer>> pq = new PriorityQueue<>(
                Comparator.comparingDouble(x -> distance(x.getInput(), input)));

        // add all training instances to the priority queue
        pq.addAll(trainingInstances);

        int[] labelsCount = {0, 0};
        // iterate over the k nearest neighbors
        // this is the 'voting' part in my implementation of KNN
        for (int i = 0; i < k; i++) {
            Instance<Double, Integer> instance = pq.poll();
            assert instance != null;
            labelsCount[instance.getOutput()]++;
        }
        if (labelsCount[0] > labelsCount[1]) {
            return 0;
        } else {
            return 1;
        }
    }
}
