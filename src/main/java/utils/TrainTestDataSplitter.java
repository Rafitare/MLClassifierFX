package utils;

import domain.Instance;

import java.util.List;

// Split data into training and testing sets
public class TrainTestDataSplitter {
    public static void splitData(List<Instance<Double, Integer>> instances, double trainPercentage,
                                 List<Instance<Double, Integer>> trainSet, List<Instance<Double, Integer>> testSet){

        int trainSize = (int) (instances.size() * trainPercentage);
        trainSet.addAll(instances.subList(0, trainSize));
        testSet.addAll(instances.subList(trainSize, instances.size()));
    }
}
