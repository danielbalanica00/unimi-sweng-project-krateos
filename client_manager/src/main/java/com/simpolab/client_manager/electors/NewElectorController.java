package com.simpolab.client_manager.electors;

import java.util.*;

import com.simpolab.client_manager.utils.SceneSwitch;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

    Map<String, String> params = new TreeMap<>();
    params.put("username", txtUsername.getText());
    params.put("firstName", txtUsername.getText());
    params.put("username", txtUsername.getText());
    params.put("username", txtUsername.getText());
    params.put("username", txtUsername.getText());
    params.put("username", txtUsername.getText());

    // do shit

    Alert alert;
    //check if anything went wrong
    alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setContentText("Group created successfully");

    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    SceneSwitch.switchTo("../homepage/homepage.fxml", stage);
  }

  @FXML
  private void onBtnBackClicked(ActionEvent event) throws Exception {
    System.out.println(getClass().getName());
    Parent root = FXMLLoader.load(getClass().getResource("../homepage/homepage.fxml"));
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }
}
