package com.simpolab.client_manager.login;

import com.simpolab.client_manager.utils.AlertUtils;
import com.simpolab.client_manager.utils.AuthHandler;
import com.simpolab.client_manager.utils.SceneUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class LoginController {

  @FXML
  private PasswordField txtPassword;

  @FXML
  private TextField txtUsername;

  @FXML
  private void onLoginClicked(ActionEvent event) throws Exception {
    int result = AuthHandler.login(txtUsername.getText(), txtPassword.getText());

    switch (result) {
      case 0 -> AlertUtils.alert(Alert.AlertType.ERROR, "Your username or password is invalid");
      case 1 -> AlertUtils.alert(Alert.AlertType.ERROR, "Unauthorized User");
      case 2 -> SceneUtils.switchToHomepage();
    }
  }

  @FXML
  private void onSignUpClicked() {}
}
