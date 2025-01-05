package utils;

import modelDomain.Model;

public class ModelFactory {
    public static Model<Double, Integer> getModel(String modelName, String hyperparameters) {
        Model<Double, Integer> model = null;
        switch (modelName) {
            case "KNN": {
                String[] params = hyperparameters.split(",");
                int k = Integer.parseInt(params[0]);
                model = new models.KNN(k);
                break;
            }
            case "Perceptron": {
                String[] params = hyperparameters.split(",");
                int generations = Integer.parseInt(params[0]);
                double learningRate = Double.parseDouble(params[1]);
                model = new models.Perceptron(generations, learningRate);
                break;
            }
            case "Logistic Regression": {
                String[] params = hyperparameters.split(",");
                int generations = Integer.parseInt(params[0]);
                double learningRate = Double.parseDouble(params[1]);
                model = new models.LogisticRegression(generations, learningRate);
                break;
            }
            default: {
                System.out.println("Invalid model name.");
                break;
            }
        }
        return model;
    }
}
