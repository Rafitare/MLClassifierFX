package utils;

import evaluation.Accuracy;
import domain.Instance;
import models.KNN;

import java.util.List;

public class KSelection {
    public static int findBestK(List<Instance<Double, Integer>> instances, List<Instance<Double, Integer>> validationInstances, int maxK) {
        int bestK = 1;
        double bestAccuracy = 0.0;
        Accuracy<Double, Integer> accuracyEvaluator = new Accuracy<>();

        for (int k = 1; k <= maxK; k++) {
            KNN knn = new KNN(k);
            knn.train(instances);
            List<Integer> predictions = knn.test(validationInstances);
            double accuracy = accuracyEvaluator.evaluate(validationInstances, predictions);

            if (accuracy > bestAccuracy) {
                bestAccuracy = accuracy;
                bestK = k;
            }
        }

        return bestK;
    }
}