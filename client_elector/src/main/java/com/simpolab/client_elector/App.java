package com.simpolab.client_elector;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login/login-view.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 600, 300);

    stage.setScene(scene);
    stage.show();
  }

  // !DO NOT START FROM HERE, USE LAUNCHER INSTEAD
  public static void main(String[] args) {
    launch();
  }
}
