package com.simpolab.client_manager.electors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.*;


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
    private void onBtnCreateClicked(ActionEvent event) throws Exception{
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
    }

    @FXML
    private void onBtnBackClicked(ActionEvent event) throws Exception{
        System.out.println(getClass().getName());
        Parent root = FXMLLoader.load(getClass().getResource("../homepage/homepage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
