package gui;

import controller.GuiController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GuiMain extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        // hello world\
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/guifxml/gui.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("Machine Learning Application");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
