package com.simpolab.client_manager.utils;

import javafx.scene.control.Alert;

public class AlertUtils {

  public static void alert(Alert.AlertType type, String body) {
    alert(type, null, null, body);
  }

  public static void alert(Alert.AlertType type, String title, String header, String body) {
    var alert = new Alert(type);
    alert.setTitle(null);
    alert.setHeaderText(null);
    alert.setContentText(body);
    alert.showAndWait();
  }
}
