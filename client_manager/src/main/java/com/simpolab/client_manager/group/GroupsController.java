package com.simpolab.client_manager.group;

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

public class GroupsController {
    private Stage stage;
    private Scene scene;

    @FXML
    private TextField txtSearchGroup;
    @FXML
    private Button btnSearchGroup;
    @FXML
    private Button btnCreateGroup;
    @FXML
    private ListView lvGroups;
    @FXML
    private Button btnOpenGroup;

    @FXML
    private void onBtnSearchGroupClicked(ActionEvent event) throws Exception{

    }

    @FXML
    private void onBtnCreateGroupClicked(ActionEvent event) throws Exception{

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
