package com.simpolab.client_manager.login;

import com.simpolab.client_manager.utils.HttpUtils;
import com.simpolab.client_manager.utils.JsonUtils;
import com.simpolab.client_manager.utils.JwtUtils;
import com.simpolab.client_manager.utils.SceneSwitch;
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
  private void onLoginClicked(ActionEvent event) throws Exception{
    var username = txtUsername.getText();
    var password = txtPassword.getText();
    var path = "/api/v1/login"; // Todo extract in config file

    var credentials = Map.of("username", username, "password", password);

    String res = HttpUtils.postUrlParams(path, credentials);
    Map<String, String> map = JsonUtils.parseJson(res, Map.class);

    System.out.println("GOT " + map);

    Alert alert;
    if (map != null && !map.get("accessToken").isBlank()) {
      HttpUtils.token = map.get("accessToken"); // ONLY FOR TEST PURPOSES

      JwtUtils.verifyToken(map.get("accessToken"));

      alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setContentText("Login Successful!");

      stage = (Stage)((Node)event.getSource()).getScene().getWindow();
      SceneSwitch.switchTo("../homepage/homepage.fxml", stage);
    } else {
      alert = new Alert(Alert.AlertType.ERROR);
      alert.setContentText("Login Failed!");
    }

    alert.setTitle(null);
    alert.setHeaderText(null);
    alert.showAndWait();
  }

  @FXML
  private void onSignUpClicked() {}
}
