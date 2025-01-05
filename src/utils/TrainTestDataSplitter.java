package utils;

import modelDomain.Instance;

import java.util.Collections;
import java.util.List;

// Split data into training and testing sets
public class TrainTestDataSplitter {
    public static void splitData(List<Instance<Double, Integer>> instances, double trainPercentage,
                                 List<Instance<Double, Integer>> trainSet, List<Instance<Double, Integer>> testSet){
        // Shuffle the dataset to randomize the split
        Collections.shuffle(instances);

        int trainSize = (int) (instances.size() * trainPercentage);
        trainSet.addAll(instances.subList(0, trainSize));
        testSet.addAll(instances.subList(trainSize, instances.size()));
    }
}
