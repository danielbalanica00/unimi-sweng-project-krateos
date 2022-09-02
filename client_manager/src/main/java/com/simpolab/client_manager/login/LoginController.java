package com.simpolab.client_manager.login;

import com.simpolab.client_manager.utils.AlertUtils;
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
    int result = LoginSession.login(txtUsername.getText(), txtPassword.getText());

    switch (result) {
      case 0 -> AlertUtils.alert(Alert.AlertType.ERROR, "Your username or password is invalid");
      case 1 -> AlertUtils.alert(Alert.AlertType.ERROR, "Unauthorized User");
      case 2 -> {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneSwitch.switchTo("../homepage/homepage.fxml", stage);
      }
    }
  }

  @FXML
  private void onSignUpClicked() {
  }
}
