package com.simpolab.client_manager.electors;

import com.simpolab.client_manager.domain.Elector;
import com.simpolab.client_manager.utils.AuthHandler;
import com.simpolab.client_manager.utils.HttpUtils;
import com.simpolab.client_manager.utils.SceneUtils;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NewElectorController {

  private Stage stage;
  private Scene scene;

  @FXML
  private TextField txtFirstName;

  @FXML
  private TextField txtUsername;

  @FXML
  private TextField txtLastName;

  @FXML
  private TextField txtEmail;

  @FXML
  private PasswordField txtPassword;

  @FXML
  private PasswordField txtPasswordReEntered;

  @FXML
  private Button btnCreate;

  @FXML
  private void onBtnCreateClicked(ActionEvent event) throws Exception {
    String username = txtUsername.getText();
    String firstName = txtFirstName.getText();
    String lastName = txtLastName.getText();
    String email = txtEmail.getText();
    String password = txtPassword.getText();
    String passwordReEntered = txtPasswordReEntered.getText();

    Alert alert;

    if (!password.equals(passwordReEntered)) {
      alert = new Alert(Alert.AlertType.ERROR);
      alert.setContentText("Passwords do not match");
      txtPassword.clear();
      txtPasswordReEntered.clear();
      return;
    }

    Elector elector = new Elector(firstName, lastName, email, username, password);

    String response = HttpUtils.postJson(
      "/api/v1/elector",
      Map.of("Authorization", "Bearer " + AuthHandler.getAccessToken()),
      elector
    );

    alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setContentText("Elector created successfully");

    SceneUtils.switchTo("electors/electors.fxml");
  }

  @FXML
  private void onBtnBackClicked(ActionEvent event) throws Exception {
    SceneUtils.switchToHomepage();
  }
}
