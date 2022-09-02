package com.simpolab.client_manager.utils;

import static java.util.Objects.requireNonNull;

import com.simpolab.client_manager.App;
import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.NonNull;

public final class SceneUtils {

  private SceneUtils() {}

  public static void switchTo(@NonNull String scenePath) throws IOException {
    Parent pane = loadFXML(scenePath);
    App.primaryStage.getScene().setRoot(pane);
  }

  private static Parent loadFXML(@NonNull String scenePath) throws IOException {
    URL resource = requireNonNull(
      SceneUtils.class.getResource("../" + scenePath),
      "La risorsa al path" + scenePath + " non esiste"
    );
    return FXMLLoader.load(resource);
  }

  public static void switchTo(String scenePath, Stage stage) throws IOException {
    Parent root = loadFXML(scenePath);

    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  public static void switchToHomepage() throws IOException {
    switchTo("homepage/homepage.fxml");
  }
}
