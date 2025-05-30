package utils;

import domain.Instance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
    public static List<Instance<Double, Integer>> load(String path) {
        List<Instance<Double, Integer>> instances = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line = br.readLine(); // Read the header row
            if (line != null) {
                System.out.println("Loaded dataset.");
            }

            boolean isBreastCancerDataset = line.toLowerCase().contains("diagnosis");

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                List<Double> features = new ArrayList<>();

                // For breast-cancer.csv, skip the first column (ID)
                int startIndex = isBreastCancerDataset ? 2 : 0;

                for (int i = startIndex; i < values.length - 1; i++) {
                    if (values[i].isEmpty()) {
                        System.out.println("Warning: Empty feature value found, replacing with 0.0");
                        features.add(0.0);
                        continue;
                    }
                    features.add(Double.parseDouble(values[i]));
                }

                int label;
                if (isBreastCancerDataset) {
                    // Convert 'M' to 1 and 'B' to 0
                    label = values[1].equalsIgnoreCase("M") ? 1 : 0;
                } else {
                    // For diabetes.csv, parse the last column as an integer
                    label = Integer.parseInt(values[values.length - 1]);
                }

                instances.add(new Instance<>(features, label));
            }
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return instances;
    }
}