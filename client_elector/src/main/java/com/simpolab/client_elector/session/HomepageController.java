package com.simpolab.client_elector.session;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HomepageController {

  private Stage stage;
  private Scene scene;

  @FXML
  private Text lblWelcome;

  @FXML
  private TableView tableSessions;

  @FXML
  private Button btnSessionsHistory;

  @FXML
  private void onBtnSessionsHistoryClicked(ActionEvent event) throws Exception {}

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
