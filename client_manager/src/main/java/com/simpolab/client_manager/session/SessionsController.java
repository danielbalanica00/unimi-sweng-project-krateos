package com.simpolab.client_manager.session;

import com.simpolab.client_manager.login.AuthHandler;
import com.simpolab.client_manager.utils.HttpUtils;
import com.simpolab.client_manager.utils.JsonUtils;
import com.simpolab.client_manager.utils.SceneUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class SessionsController implements Initializable {
  private Stage stage;

  @FXML
  private Button btnCreateSession;
  @FXML
  private ListView<Session> lvSessions;
  @FXML
  private Button btnOpenSession;

  @FXML
  private void onBtnCreateSessionClicked(ActionEvent event) throws Exception {}

  @FXML
  private void onBtnOpenSessionClicked(ActionEvent event) throws Exception{
    Session selectedSession = lvSessions.getSelectionModel().getSelectedItems().get(0);

    SessionController.initSession(selectedSession);

    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    SceneUtils.switchTo("session/session.fxml", stage);
  }

  @FXML
  private void onBtnBackClicked(ActionEvent event) throws Exception {
    SceneUtils.switchTo("homepage/homepage.fxml");
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    lvSessions.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    String sessionsJson = HttpUtils.get("/api/v1/session", Map.of("Authorization", "Bearer " + AuthHandler.getAccessToken()));
    List<Session> sessions = JsonUtils.parseJsonArray(sessionsJson, Session.class);

    lvSessions.getItems().addAll(sessions);
  }
}
