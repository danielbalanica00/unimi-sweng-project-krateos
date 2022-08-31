package com.simpolab.client_manager.session;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SessionsController {
    private Stage stage;
    private Scene scene;

    @FXML
    private TextField txtSearchSession;
    @FXML
    private Button btnSearchSession;
    @FXML
    private Button btnCreateSession;
    @FXML
    private ListView lvSessions;
    @FXML
    private Button btnOpenSession;

    @FXML
    private void onBtnSearchSessionClicked(ActionEvent event) throws Exception{

    }

    @FXML
    private void onBtnCreateSessionClicked(ActionEvent event) throws Exception{

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