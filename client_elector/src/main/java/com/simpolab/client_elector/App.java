package com.simpolab.client_elector;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        System.setProperty("prism.lcdtext", "false");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 300);
        scene.getStylesheets().add("http://fonts.googleapis.com/css?family=Roboto");
        scene.getStylesheets().add(getClass().getResource("login/font.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
    }

    // !DO NOT START FROM HERE, USE LAUNCHER INSTEAD
    public static void main(String[] args) {
        launch();
    }
}