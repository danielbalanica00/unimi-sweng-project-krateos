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

public class NewGroupController {

  private Stage stage;
  private Scene scene;

  @FXML
  private TextField txtGroupName;

  @FXML
  private Button btnCreateGroup;

  @FXML
  private ListView lvAvailableElectors;

  @FXML
  private ListView lvAddedElectors;

  @FXML
  private Button btnAddElectors;

  @FXML
  private Button btnRemoveElectors;

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
