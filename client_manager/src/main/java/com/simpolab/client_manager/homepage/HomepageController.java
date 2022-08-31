package com.simpolab.client_manager.homepage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;

public class HomepageController {

  private Scene scene;
  private Stage stage;

  @FXML
  private ListView lvSessions;

  @FXML
  private TableColumn columnName;

  @FXML
  private TableColumn columnAction;

  @FXML
  private Button btnNewSession;

  @FXML
  private Button btnManageSession;

  @FXML
  private Button btnNewGroup;

  @FXML
  private Button btnManageGroup;

  @FXML
  private Button btnOpenSession;

  /**
   * Switches to create_session.fxml view
   * @param event
   * @throws Exception
   */
  @FXML
  private void onNewSessionClicked(ActionEvent event) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("../session/new_session.fxml"));
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(new Scene(root));
    stage.show();
  }

  /**
   * Switches to sessions.fxml view
   * @param event
   * @throws Exception
   */
  @FXML
  private void onManageSessionClicked(ActionEvent event) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("../session/sessions.fxml"));
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(new Scene(root));
    stage.show();
  }

  /**
   * Switches to create_group.fxml view
   * @param event
   * @throws Exception
   */
  @FXML
  private void onNewGroupClicked(ActionEvent event) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("../group/create.fxml"));
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(new Scene(root));
    stage.show();
  }

  /**
   * Switches to groups.fxml view
   * @param event
   * @throws Exception
   */
  @FXML
  private void onManageGroupClicked(ActionEvent event) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("../group/groups.fxml"));
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(new Scene(root));
    stage.show();
  }
}
