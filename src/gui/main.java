package gui;

import evaluation.Accuracy;
import evaluation.Precision;
import modelDomain.Instance;
import modelDomain.Model;
import utils.CSVReader;
import utils.ModelFactory;
import utils.TrainTestDataSplitter;

import java.util.ArrayList;
import java.util.List;

public class main {
    public static void main(String[] args) {
        CSVReader reader = new CSVReader();
        List<Instance<Double, Integer>> instances = reader.load("data/diabetes.csv");

        Model<Double, Integer> model = ModelFactory.getModel("KNN", "3");
        ArrayList<Instance<Double, Integer>> trainSet = new ArrayList<>();
        ArrayList<Instance<Double, Integer>> testSet = new ArrayList<>();
        TrainTestDataSplitter.splitData(instances, 0.8, trainSet, testSet);

        model.train(trainSet);
        List<Integer> predictions = model.test(testSet);

        Accuracy<Double, Integer> accuracyEvaluator = new Accuracy<>();
        double accuracy = accuracyEvaluator.evaluate(testSet, predictions);
        System.out.println("Accuracy: " + accuracy);

        Precision<Double, Integer> precisionEvaluator = new Precision<>();
        double precision = precisionEvaluator.evaluate(testSet, predictions);
        System.out.println("Precision: " + precision);


    }
}
