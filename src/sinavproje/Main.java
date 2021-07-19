package sinavproje;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static DataController data;
    public static ControllerManager controllers;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("ui/main.fxml"));
        primaryStage.setTitle("Sınav Proje");
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.setMaxWidth(900);
        primaryStage.setMaxHeight(600);
        primaryStage.show();
    }


    public static void main(String[] args) {
        data = new DataController();
        controllers = new ControllerManager();
        launch(args);
    }
}
