# 🤖 JavaFX Machine Learning Classifier

**MLClassifierFX** is a Java project with a JavaFX GUI that allows you to train and evaluate multiple binary classification algorithms on datasets. It’s designed for ease of use and learning about classic machine learning models.

 With this tool, you can build machine learning models to make predictions on **Diabetes** or **Breast Cancer** datasets using a variety of classifiers.


## ✨ Features

- **Binary Classification Algorithms**:
	- K-Nearest Neighbors (KNN)
	- Perceptron
	- Logistic Regression
	- Decision Tree
	- Naive Bayes

- **Evaluation Measures**:
	- Accuracy
	- Precision
	- Recall
	- F1 Score
	- Confusion Matrix

- **GUI Features**:
	- Select input dataset (`diabetes.csv` or `breast-cancer.csv`)
	- Choose a classifier and set hyperparameters
	- Adjust train-test split percentage
	- Train the classifier
	- Display test results, including confusion matrix and evaluation metrics

## 🛠️ Technologies Used

- **Java** – Core programming language
- **JavaFX** – GUI framework for building user interfaces
- **Maven** – Project build and dependency management

## 📁 Project Structure
- `src/main/java/` – Java source code
- `src/main/resources/` – Datasets and FXML GUI files

## 🧪 How to Use

Follow these steps to use **MLClassifierFX**:

### 1. Launch the App
- Open the project in your IDE and run the main class (`GuiMain.java`).
- The JavaFX GUI will appear.

### 2. Load a Dataset
- From the dropdown or file selector in the GUI, choose one of the included datasets:
  - `diabetes.csv` – For predicting the presence of diabetes.
  - `breast-cancer.csv` – For predicting breast cancer diagnosis.
- You can also use your own `.csv` file if you want to make predictions on different data.
  - **Note:** Your custom CSV must match the format of the existing datasets (e.g., label column, number of features, etc.).
  - If your dataset uses a different format, you may need to modify the `CVReader.java` class to parse it correctly.

### 3. Choose a Classifier
- Select a machine learning algorithm from the available options:
  - K-Nearest Neighbors (KNN)
  - Perceptron
  - Logistic Regression
  - Decision Tree
  - Naive Bayes

### 4. Set Hyperparameters
- Depending on the classifier, you may be able to set parameters:
  - Number of neighbors (for KNN)
  - Learning rate and epochs (for Perceptron)

### 5. Adjust Train-Test Split
- Use the provided slider or input field to set the percentage of data used for training (recommended values: 80% training / 20% testing).

### 6. Train the Classifier
- Click the **"Train"** button to train your selected model on the dataset.

### 7. View Results
- Once training is complete, press the **Test** button, the app will display:
  - **Confusion Matrix**
  - **Accuracy**
  - **Precision**
  - **Recall**
  - **F1 Score**

### 8. Experiment and Compare
- Try different classifiers, hyperparameters, or datasets to compare performance.
