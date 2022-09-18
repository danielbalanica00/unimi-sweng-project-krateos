package com.simpolab.client_manager.electors;

import com.google.gson.Gson;
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

  /**
   * Creates a user given the information inserted in the text fields.
   * If fields are not properly filled the user is not created and a pop message
   * warns of such event.
   * @param event
   * @throws Exception
   */
  @FXML
  private void onBtnCreateClicked(ActionEvent event) throws Exception {
    String username = txtUsername.getText();
    String firstName = txtFirstName.getText();
    String lastName = txtLastName.getText();
    String email = txtEmail.getText();
    String password = txtPassword.getText();
    String passwordReEntered = txtPasswordReEntered.getText();

    Alert alert;

    if (
      username.isBlank() ||
      firstName.isBlank() ||
      lastName.isBlank() ||
      email.isBlank() ||
      password.isBlank()
    ) {
      alert = new Alert(Alert.AlertType.ERROR);
      alert.setContentText("Please fill every field");
      txtPassword.clear();
      txtPasswordReEntered.clear();
      return;
    } else if (!password.equals(passwordReEntered)) {
      alert = new Alert(Alert.AlertType.ERROR);
      alert.setContentText("Passwords do not match");
      txtPassword.clear();
      txtPasswordReEntered.clear();
      return;
    }

    Elector elector = new Elector(firstName, lastName, username, email, password);
    String response = HttpUtils.postJson("/api/v1/elector", elector);

    alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setContentText("Elector created successfully");

    SceneUtils.switchTo("electors/electors.fxml");
  }

  @FXML
  private void onBtnBackClicked(ActionEvent event) throws Exception {
    SceneUtils.switchToHomepage();
  }
}
