package gui;

import evaluation.Accuracy;
import evaluation.ConfusionMatrix;
import evaluation.Precision;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
        model = ModelFactory.getModel(classifierType, hyperParam1 + ',' + hyperParam2);
        if (scaleFeaturesCheckBox.isSelected()) {
            FeatureScaler.minMaxScale(instances);
        }
        ArrayList<Instance<Double, Integer>> trainSet = new ArrayList<>();
        testSet = new ArrayList<>();
        TrainTestDataSplitter.splitData(instances, trainTestPercentage, trainSet, testSet);
        model.train(trainSet);
    }

    @FXML
    void handleTest() {
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

        txtResults.setText("Accuracy: " + acc + "\nPrecision: " + prec) ;
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