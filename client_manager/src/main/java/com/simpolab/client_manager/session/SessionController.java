package com.simpolab.client_manager.session;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SessionController {
    private Stage stage;
    private Scene scene;

    @FXML
    private Text lblSessionName;
    @FXML
    private Text lblRemainingTime;
    @FXML
    private Button btnStop;
    @FXML
    private Button btnAbort;

    @FXML
    private void onBtnStopClicked(ActionEvent event) throws Exception{

    }

    @FXML
    private void onBtnAbortClicked(ActionEvent event) throws Exception{

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
