package com.simpolab.client_manager.group;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GroupController {

    private Stage stage;
    private Scene scene;

    @FXML
    private void onBtnAddElectorsClicked(ActionEvent actionEvent) {
    }

    @FXML
    private void onBtnRemoveElectorsSelected(ActionEvent actionEvent) {
    }

    @FXML
    private void onBtnBackClicked(ActionEvent event) throws Exception{
        System.out.println(getClass().getName());
        Parent root = FXMLLoader.load(getClass().getResource("groups.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
