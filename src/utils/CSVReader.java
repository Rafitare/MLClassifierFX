package utils;

import modelDomain.Instance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
    public static List<Instance<Double, Integer>> load(String path) {
        List<Instance<Double, Integer>> instances = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;
            if ((line = br.readLine()) != null) {
                // Skip the header row
                System.out.println("Loaded dataset.");
            }
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                List<Double> features = new ArrayList<>();
                for (int i = 0; i < values.length - 1; i++) {
                    features.add(Double.parseDouble(values[i]));
                }
                int label = Integer.parseInt(values[values.length - 1]);
                instances.add(new Instance<>(features, label));
            }
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return instances;
    }

}
