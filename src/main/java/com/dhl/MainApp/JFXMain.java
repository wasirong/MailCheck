package com.dhl.MainApp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JFXMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
        primaryStage.setTitle("邮件航班库存数据分析 Version.1.0.0.0");
        primaryStage.setScene(new Scene(root, 990, 440));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
