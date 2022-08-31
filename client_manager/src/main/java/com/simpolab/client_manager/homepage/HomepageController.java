package com.simpolab.client_manager.homepage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simpolab.client_manager.session.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HomepageController implements Initializable {
  private Stage stage;

  @FXML
  private ListView<Session> lvSessions;
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

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    String res = "";

    List<Session> activeSessions = new ArrayList<>();
    ObjectMapper mapper = new ObjectMapper();
    try {
      activeSessions = mapper.readValue(res, new TypeReference<List<Session>>() {});
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    lvSessions.getItems().addAll(activeSessions);
  }

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
