package com.simpolab.client_manager.homepage;

import com.simpolab.client_manager.domain.Session;
import com.simpolab.client_manager.session.SessionController;
import com.simpolab.client_manager.utils.AuthHandler;
import com.simpolab.client_manager.utils.HttpUtils;
import com.simpolab.client_manager.utils.JsonUtils;
import com.simpolab.client_manager.utils.SceneUtils;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;

public class HomepageController implements Initializable {

  @FXML
  private ListView<Session> lvSessions;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    lvSessions.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    String sessionsJson = HttpUtils.get(
      "/api/v1/session",
      Map.of("Authorization", "Bearer " + AuthHandler.getAccessToken())
    );

    List<Session> sessions = JsonUtils.parseJsonArray(sessionsJson, Session.class);
    List<Session> activeSessions = sessions
      .stream()
      .filter(s -> s.getState().equals(Session.State.ACTIVE))
      .toList();

    lvSessions.getItems().addAll(activeSessions);
  }

  @FXML
  private void onBtnOpenSessionClicked(ActionEvent event) throws Exception {
    Session session = lvSessions.getSelectionModel().getSelectedItem();

    SessionController.init(session);

    SceneUtils.switchTo("session/session.fxml");
  }

  /**
   *
   * @param event
   * @throws Exception
   */
  @FXML
  private void onBtnNewElectorClicked(ActionEvent event) throws Exception {
    SceneUtils.switchTo("electors/new_elector.fxml");
  }

  /**
   *
   * @param event
   * @throws Exception
   */
  @FXML
  private void onBtnManageElectorsClicked(ActionEvent event) throws Exception {
    SceneUtils.switchTo("electors/electors.fxml");
  }

  /**
   * Switches to create_session.fxml view
   * @param event
   * @throws Exception
   */
  @FXML
  private void onBtnNewSessionClicked(ActionEvent event) throws Exception {
    SceneUtils.switchTo("session/new_session.fxml");
  }

  /**
   * Switches to sessions.fxml view
   * @param event
   * @throws Exception
   */
  @FXML
  private void onBtnManageSessionClicked(ActionEvent event) throws Exception {
    SceneUtils.switchTo("session/sessions.fxml");
  }

  /**
   * Switches to create_group.fxml view
   * @param event
   * @throws Exception
   */
  @FXML
  private void onBtnNewGroupClicked(ActionEvent event) throws Exception {
    SceneUtils.switchTo("group/new_group.fxml");
  }

  /**
   * Switches to groups.fxml view
   * @param event
   * @throws Exception
   */
  @FXML
  private void onBtnManageGroupClicked(ActionEvent event) throws Exception {
    SceneUtils.switchTo("group/groups.fxml");
  }
}
