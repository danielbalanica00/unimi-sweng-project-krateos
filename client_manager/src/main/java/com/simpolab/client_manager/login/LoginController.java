package com.simpolab.client_manager.login;

import com.simpolab.client_manager.utils.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Map;

public class LoginController {

  private Stage stage;
  private Scene scene;
  private Parent root;

  @FXML
  private PasswordField txtPassword;

  @FXML
  private TextField txtUsername;

  @FXML
  private Button btnLogin;

  @FXML
  private Text lblSignUp;

  @FXML
  private void onLoginClicked(ActionEvent event) throws Exception {
    var username = txtUsername.getText();
    var password = txtPassword.getText();
    var path = "/api/v1/login"; // Todo extract in config file

    var credentials = Map.of("username", username, "password", password);

    String tokensJson = HttpUtils.postUrlParams(path, credentials);
    System.out.println("Got: " + tokensJson);

    // CASE 1: Failed login
    if (tokensJson == null || tokensJson.isBlank()) {
      AlertUtils.alert(Alert.AlertType.ERROR, "Your username or password is invalid");
      return;
    }

    AuthTokens tokens = JsonUtils.parseJson(tokensJson, AuthTokens.class);
    String[] roles = JwtUtils.getRoles(tokens.getAccessToken());

    // CASE 2: Login successfully but user is not a manager
    if (!roles[0].equals("MANAGER")) {
      AlertUtils.alert(Alert.AlertType.ERROR, "Unauthorized User");
      return;
    }

    // CASE 3: Login successfully and user is a manager
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    SceneSwitch.switchTo("../homepage/homepage.fxml", stage);
  }

  @FXML
  private void onSignUpClicked() {
  }
}
