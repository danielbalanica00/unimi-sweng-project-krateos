package com.simpolab.client_elector.login;

import com.simpolab.client_elector.data_access.TestApi;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

  @FXML
  private PasswordField txtPassword;

  @FXML
  private TextField txtUsername;

  @FXML
  private Button btnLogin;

  @FXML
  void onLoginClick(ActionEvent e) {
    String username = txtUsername.getText();
    String password = txtPassword.getText();
    String url = "http://127.0.0.1:8080/api/v1/login";

    boolean res = TestApi.sendPost(url, username, password);

    Alert alert;
    if (res) {
      alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setContentText("Login Successful!");
    } else {
      alert = new Alert(Alert.AlertType.ERROR);
      alert.setContentText("Login Failed!");
    }
    alert.setTitle(null);
    alert.setHeaderText(null);
    alert.showAndWait();
  }
}
