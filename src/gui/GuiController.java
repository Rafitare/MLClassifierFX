package gui;

import evaluation.Accuracy;
import evaluation.Precision;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import modelDomain.Instance;
import modelDomain.Model;
import utils.CSVReader;
import utils.FeatureScaler;
import utils.ModelFactory;
import utils.TrainTestDataSplitter;

import java.util.ArrayList;
import java.util.List;

public class GuiController {
    String classifierType;
    @FXML
    private ComboBox<String> comboBoxClassifier;

    @FXML
    private TextField txtHyperParam1;

    @FXML
    private TextField txtHyperParam2;

    @FXML
    private TextField txtInputFilePath;

    @FXML
    private TextArea txtResults;

    @FXML
    private TextField txtTrainTestSplit;

    @FXML
    void handleTest() {
        txtResults.setText("Accuracy: " + modelAccuracy + "\nPrecision: " + modelPrecision);
    }

    double trainTestPercentage;
    double modelAccuracy;
    double modelPrecision;

    @FXML
    private CheckBox scaleFeaturesCheckBox;


    @FXML
    void handleTrain() {
        String trainTestPercentageString = txtTrainTestSplit.getText();
        trainTestPercentage = Double.parseDouble(trainTestPercentageString);
        if (trainTestPercentage < 20 || trainTestPercentage > 100) {
            showAlert("Train Test Split should be between 20 and 100");
        }
        trainTestPercentage = trainTestPercentage / 100;
        classifierType = comboBoxClassifier.getSelectionModel().getSelectedItem();
        String hyperParam1 = txtHyperParam1.getText();
        String hyperParam2 = txtHyperParam2.getText();
        String inputFilePath = txtInputFilePath.getText();
        List<Instance<Double, Integer>> instances = CSVReader.load("data/" + inputFilePath);
        Model<Double, Integer> model = ModelFactory.getModel(classifierType, hyperParam1 + ',' + hyperParam2);
        if (scaleFeaturesCheckBox.isSelected()) {
            FeatureScaler.minMaxScale(instances);
        }
        ArrayList<Instance<Double, Integer>> trainSet = new ArrayList<>();
        ArrayList<Instance<Double, Integer>> testSet = new ArrayList<>();
        TrainTestDataSplitter.splitData(instances, trainTestPercentage, trainSet, testSet);
        model.train(trainSet);
        List<Integer> predictions = model.test(testSet);
        Accuracy<Double, Integer> accuracyEvaluator = new Accuracy<>();
        modelAccuracy = accuracyEvaluator.evaluate(testSet, predictions);
        Precision<Double, Integer> precisionEvaluator = new Precision<>();
        modelPrecision = precisionEvaluator.evaluate(testSet, predictions);

    }


    @FXML
    private void initialize() {
        comboBoxClassifier.getItems().addAll("KNN", "Perceptron", "Logistic Regression");

    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

}
