package com.simpolab.client_manager.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simpolab.client_manager.utils.Api;

import java.util.Map;
import java.util.TreeMap;

import com.simpolab.client_manager.utils.SceneSwitch;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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
    String username = txtUsername.getText();
    String password = txtPassword.getText();
    String url = "http://127.0.0.1:8080/api/v1/login";

    Map<String, String> credentials = new TreeMap();
    credentials.put("username", username);
    credentials.put("password", password);

    String res = Api.sendPost(url, credentials);
    ObjectMapper mapper = new ObjectMapper();
    Map<String, String> map = mapper.readValue(res, Map.class);

    Alert alert;
    if (!map.get("accessToken").isBlank()) {
      Api.token = map.get("accessToken"); // ONLY FOR TEST PURPOSES

      Api.verifyToken(map.get("accessToken"));

      alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setContentText("Login Successful!");

      stage = (Stage)((Node)event.getSource()).getScene().getWindow();
      SceneSwitch.switchTo("../electors/electors.fxml", stage);
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
