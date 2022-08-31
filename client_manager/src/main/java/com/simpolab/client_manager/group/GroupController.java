package com.simpolab.client_manager.group;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class GroupController {

  private Stage stage;
  private Scene scene;

  @FXML
  private ListView lvElectors;

  @FXML
  private Button btnEditGroup;

  @FXML
  private void onBtnBackClicked(ActionEvent event) throws Exception {
    System.out.println(getClass().getName());
    Parent root = FXMLLoader.load(getClass().getResource("groups.fxml"));
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }
}
