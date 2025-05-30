package controller;

import evaluation.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import domain.Instance;
import domain.Model;
import utils.CSVReader;
import utils.FeatureScaler;
import utils.ModelFactory;
import utils.TrainTestDataSplitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GuiController {
    String classifierType;
    @FXML
    private ComboBox<String> comboBoxClassifier;

    @FXML
    private ComboBox<String> txtInputFilePath;


    @FXML
    private TextField txtHyperParam1;

    @FXML
    private TextField txtHyperParam2;

    @FXML
    private TextArea txtResults;

    @FXML
    private TableView<String[]> tableConfusionMatrix;

    @FXML
    private TableColumn<String[], String> colActual;

    @FXML
    private TableColumn<String[], String> colPredicted0;

    @FXML
    private TableColumn<String[], String> colPredicted1;

    double trainTestPercentage;
    ArrayList<Instance<Double, Integer>> testSet;
    Model<Double, Integer> model;

    @FXML
    private CheckBox scaleFeaturesCheckBox;

    @FXML
    private Slider sliderTrainTestSplit;

    @FXML
    private void initialize() {
        comboBoxClassifier.getItems().addAll("KNN", "Perceptron", "Logistic Regression", "Decision Tree", "Naive Bayes");
        txtInputFilePath.getItems().addAll("diabetes.csv", "breast-cancer.csv");
    }


    @FXML
    void handleTrain() {
        if (comboBoxClassifier.getSelectionModel().isEmpty()) {
            showAlert("Please select a classifier type.");
            return;
        }

        classifierType = comboBoxClassifier.getSelectionModel().getSelectedItem();
        System.out.println("Selected classifier: " + classifierType);

        if (txtInputFilePath.getSelectionModel().isEmpty()) {
            showAlert("Please select an input file.");
            return;
        }

        System.out.println("Selected input file: " + txtInputFilePath.getValue());

        String inputFilePath = txtInputFilePath.getValue();
        List<Instance<Double, Integer>> instances = CSVReader.load("src/main/resources/data/" + inputFilePath);

        trainTestPercentage = sliderTrainTestSplit.getValue();
        if (trainTestPercentage < 20 || trainTestPercentage > 100) {
            showAlert("Train Test Split should be between 20 and 100");
            return;
        }
        trainTestPercentage = trainTestPercentage / 100;

        // Handle classifier-specific hyperparameter UI
        boolean needsParams = true;
        switch (classifierType) {
            case "Naive Bayes":
            case "Decision Tree":
                needsParams = false;
                txtHyperParam1.clear();
                txtHyperParam2.clear();
                txtHyperParam1.setPromptText("Not required");
                txtHyperParam2.setPromptText("Not required");
                break;
            case "KNN":
                txtHyperParam1.setPromptText("K (integer)");
                txtHyperParam2.setPromptText("Not required");
                break;
            case "Perceptron":
            case "Logistic Regression":
                txtHyperParam1.setPromptText("Max generations (integer)");
                txtHyperParam2.setPromptText("Learning rate (double)");
                break;
        }

        String hyperParam1 = txtHyperParam1.getText().trim();
        String hyperParam2 = txtHyperParam2.getText().trim();

        if (needsParams) {
            // Validate parameters for the classifiers that need them
            if (classifierType.equals("KNN")) {
                try {
                    Integer.parseInt(hyperParam1);
                } catch (NumberFormatException e) {
                    showAlert("K should be an integer value");
                    return;
                }
            } else if (classifierType.equals("Perceptron") || classifierType.equals("Logistic Regression")) {
                try {
                    Integer.parseInt(hyperParam1);
                } catch (NumberFormatException e) {
                    showAlert("Max generations should be an integer value");
                    return;
                }
                try {
                    Double.parseDouble(hyperParam2);
                } catch (NumberFormatException e) {
                    showAlert("Learning rate should be a double value");
                    return;
                }
            }
        } else {
            // Set to null or empty for models that don’t use hyperparameters
            hyperParam1 = "";
            hyperParam2 = "";
        }

        model = ModelFactory.getModel(classifierType, hyperParam1 + "," + hyperParam2);

        if (scaleFeaturesCheckBox.isSelected()) {
            FeatureScaler.minMaxScale(instances);
        }

        ArrayList<Instance<Double, Integer>> trainSet = new ArrayList<>();
        testSet = new ArrayList<>();
        TrainTestDataSplitter.splitData(instances, trainTestPercentage, trainSet, testSet);

        model.train(trainSet);
        txtResults.setText("Data loaded successfully from " + inputFilePath + "\n" +
                "Model trained successfully with " + classifierType + "\n" +
                "Train Test Split: " + (trainTestPercentage * 100) + "%\n" +
                "Hyperparameters: " + (needsParams ? hyperParam1 + ", " + hyperParam2 : "Not required"));
    }


    @FXML
    void handleTest() {
        if (model == null) {
            showAlert("Please train the model first.");
            return;
        }
        List<Integer> predictions = model.test(testSet);
        ConfusionMatrix confusionMatrix = new ConfusionMatrix();
        List<Integer> actual = new ArrayList<>();
        for (Instance<Double, Integer> instance : testSet) {
            actual.add(instance.getOutput());
        }
        confusionMatrix.update(actual, predictions);

        ObservableList<String[]> data = FXCollections.observableArrayList(confusionMatrix.getMatrixAsList());
        tableConfusionMatrix.setItems(data);

        colActual.setCellValueFactory(x -> new SimpleStringProperty(x.getValue()[0]));
        colPredicted0.setCellValueFactory(x -> new SimpleStringProperty(x.getValue()[1]));
        colPredicted1.setCellValueFactory(x -> new SimpleStringProperty(x.getValue()[2]));

        Accuracy<Double, Integer> accuracy = new Accuracy<>();
        double acc = accuracy.evaluate(testSet, predictions);

        Precision<Double, Integer> precision = new Precision<>();
        double prec = precision.evaluate(testSet, predictions);

        Recall<Double, Integer> recall = new Recall<>();
        double rec = recall.evaluate(testSet, predictions);

        F1Score<Double, Integer> f1Score = new F1Score<>();
        double f1 = f1Score.evaluate(testSet, predictions);

        txtResults.setText("Accuracy: " + acc + "\nPrecision: " + prec) ;
        txtResults.appendText("\nRecall: " + rec + "\nF1 Score: " + f1);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}