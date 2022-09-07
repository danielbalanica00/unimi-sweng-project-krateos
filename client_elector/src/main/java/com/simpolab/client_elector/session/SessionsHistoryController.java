package com.simpolab.client_elector.session;

import com.simpolab.client_elector.domain.Session;
import com.simpolab.client_elector.utils.HttpUtils;
import com.simpolab.client_elector.utils.JsonUtils;
import com.simpolab.client_elector.utils.SceneUtils;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

public class SessionsHistoryController implements Initializable {

  @FXML
  private ListView<Session> lvSessions;

  @FXML
  private void onBtnOpenSessionClicked(ActionEvent event) throws Exception {
    Session selectedSession = lvSessions.getSelectionModel().getSelectedItem();
    if (selectedSession == null) return;

    SessionInfoController.init(selectedSession);
    SceneUtils.switchTo("session/session_info.fxml");
  }

  @FXML
  private void onBtnBackClicked(ActionEvent event) throws Exception {
    SceneUtils.switchToHomepage();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    lvSessions.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    String sessionsJson = HttpUtils.get("/api/v1/session");
    List<Session> sessions = JsonUtils.parseJsonArray(sessionsJson, Session.class);

    // filter out only the ended sessions
    sessions =
      sessions.stream().filter(session -> session.getState().equals(Session.State.ENDED)).toList();

    lvSessions.getItems().addAll(sessions);
  }
}
