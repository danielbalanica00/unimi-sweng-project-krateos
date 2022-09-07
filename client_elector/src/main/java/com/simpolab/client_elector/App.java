package com.simpolab.client_elector;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

  public static Stage primaryStage;

  @Override
  public void start(Stage primaryStage) throws IOException {
    App.primaryStage = primaryStage;

    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("login/login.fxml"));
    Scene scene = new Scene(fxmlLoader.load());
    primaryStage.setTitle("Krateos Elector");
    primaryStage.setScene(scene);
    primaryStage.setHeight(600);
    primaryStage.setWidth(800);
    primaryStage.show();
  }

  // !DO NOT START FROM HERE, USE LAUNCHER INSTEAD
  public static void main(String[] args) {
    launch();
  }
}
