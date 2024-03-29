package org.levy.filecollector;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(this.getClass().getClassLoader().getResource("sample.fxml"));
        primaryStage.setTitle("File Collector");
        primaryStage.setScene(new Scene(root, 400, 280));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
