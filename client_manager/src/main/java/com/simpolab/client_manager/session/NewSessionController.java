package com.simpolab.client_manager.session;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;

public class NewSessionController {
    private Stage stage;
    private Scene scene;

    @FXML
    private TextField txtName;
    @FXML
    private DatePicker dateEndsOn;
    @FXML
    private ChoiceBox choiceSessionType;
    @FXML
    private CheckBox cbQuorum;
    @FXML
    private CheckBox cbAbsoluteMajority;
    @FXML
    private Button btnNext;

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
