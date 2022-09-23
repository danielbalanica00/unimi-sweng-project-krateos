package com.simpolab.client_elector.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public final class AlertUtils {

  private AlertUtils() {}

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

  public static boolean confirmAlert(String prompt) {
    // confirmation
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, prompt, ButtonType.YES, ButtonType.NO);
    alert.showAndWait();
    if (alert.getResult() == ButtonType.NO) {
      return false;
    }
    return true;
  }
}
