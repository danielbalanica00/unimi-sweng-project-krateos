package com.simpolab.client_manager.homepage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simpolab.client_manager.session.Session;
import com.simpolab.client_manager.utils.SceneSwitch;
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

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HomepageController implements Initializable {
  private Stage stage;

  @FXML
  private ListView<Session> lvSessions;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    String res = "";

    List<Session> activeSessions = new ArrayList<>();

    lvSessions.getItems().addAll(activeSessions);
  }

  /**
   *
   * @param event
   * @throws Exception
   */
  @FXML
  private void onBtnNewElectorClicked(ActionEvent event) throws Exception{
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    SceneSwitch.switchTo("../electors/new_elector.fxml", stage);
  }

  /**
   *
   * @param event
   * @throws Exception
   */
  @FXML
  private void onBtnManageElectorsClicked(ActionEvent event) throws Exception{
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    SceneSwitch.switchTo("../electors/electors.fxml", stage);
  }

  /**
   * Switches to create_session.fxml view
   * @param event
   * @throws Exception
   */
  @FXML
  private void onBtnNewSessionClicked(ActionEvent event) throws Exception {
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    SceneSwitch.switchTo("../session/new_session.fxml", stage);
  }

  /**
   * Switches to sessions.fxml view
   * @param event
   * @throws Exception
   */
  @FXML
  private void onBtnManageSessionClicked(ActionEvent event) throws Exception {
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    SceneSwitch.switchTo("../session/sessions.fxml", stage);
  }

  /**
   * Switches to create_group.fxml view
   * @param event
   * @throws Exception
   */
  @FXML
  private void onBtnNewGroupClicked(ActionEvent event) throws Exception {
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    SceneSwitch.switchTo("../group/new_group.fxml", stage);
  }

  /**
   * Switches to groups.fxml view
   * @param event
   * @throws Exception
   */
  @FXML
  private void onBtnManageGroupClicked(ActionEvent event) throws Exception {
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    SceneSwitch.switchTo("../group/groups.fxml", stage);
  }


}
