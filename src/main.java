import evaluation.Accuracy;
import evaluation.Precision;
import modelDomain.Instance;
import models.LogisticRegression;
import utils.CSVReader;
import utils.FeatureScaler;

import java.util.List;

public class main {
    public static void main(String[] args) {
        CSVReader reader = new CSVReader();
        List<Instance<Double, Integer>> instances = reader.load("data/diabetes.csv");

//        Perceptron perceptron = new Perceptron(500, 0.03);
//
//        perceptron.train(instances.subList(0, 500));
//        List<Instance<Double, Integer>> testInstances = instances.subList(500, 768);
//        List<Integer> predictions = perceptron.test(testInstances);
//
//        Accuracy<Double, Integer> accuracy = new Accuracy<>();
//        Precision<Double, Integer> precision = new Precision<>();
//
//        System.out.println("Accuracy: " + accuracy.evaluate(testInstances, predictions));
//        System.out.println("Precision: " + precision.evaluate(testInstances, predictions));
//        int maxK = 100;
//        int bestK = utils.KSelection.findBestK(instances.subList(0, 500), instances.subList(500, 768), maxK);
//        System.out.println("Best K: " + bestK);
//        KNN knn = new KNN(bestK);
//        knn.train(instances.subList(0, 500));
//        List<Instance<Double, Integer>> testInstances = instances.subList(500, 768);
//        List<Integer> predictions = knn.test(testInstances);
//        Accuracy<Double, Integer> accuracy = new Accuracy<>();
//        Precision<Double, Integer> precision = new Precision<>();
//        System.out.println("Accuracy: " + accuracy.evaluate(testInstances, predictions));
//        System.out.println("Precision: " + precision.evaluate(testInstances, predictions));
//
        FeatureScaler.minMaxScale(instances);

//        // Print scaled data
//        for (Instance<Double, Integer> instance : instances) {
//            System.out.println(instance.getInput());
//        }


        LogisticRegression logisticRegression = new LogisticRegression( 500, 0.01);
        logisticRegression.train(instances.subList(0, 500));
        List<Instance<Double, Integer>> testInstances = instances.subList(500, 768);
        List<Integer> predictions = logisticRegression.test(testInstances);
        Accuracy<Double, Integer> accuracy = new Accuracy<>();
        Precision<Double, Integer> precision = new Precision<>();
        System.out.println("Accuracy: " + accuracy.evaluate(testInstances, predictions));
        System.out.println("Precision: " + precision.evaluate(testInstances, predictions));


    }
}
