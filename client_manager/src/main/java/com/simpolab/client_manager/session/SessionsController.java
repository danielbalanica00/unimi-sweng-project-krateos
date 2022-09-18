package com.simpolab.client_manager.session;

import com.simpolab.client_manager.domain.Session;
import com.simpolab.client_manager.utils.HttpUtils;
import com.simpolab.client_manager.utils.JsonUtils;
import com.simpolab.client_manager.utils.SceneUtils;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

public class SessionsController implements Initializable {

  @FXML
  private Button btnCreateSession;

  @FXML
  private ListView<Session> lvSessions;

  @FXML
  private Button btnOpenSession;

  @FXML
  private void onBtnCreateSessionClicked(ActionEvent event) throws Exception {}

  @FXML
  private void onBtnOpenSessionClicked(ActionEvent event) throws Exception {
    Session selectedSession = lvSessions.getSelectionModel().getSelectedItem();

    switch(selectedSession.getType()){
      case CATEGORIC, ORDINAL, REFERENDUM -> {
        SessionController.init(selectedSession);
        SceneUtils.switchTo("session/session.fxml");
      }
      case CATEGORIC_WITH_PREFERENCES -> {
        SessionCategoricPreferenceController.init(selectedSession);
        SceneUtils.switchTo("session/session_categoric_preference.fxml");
      }
    }
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

    lvSessions.getItems().addAll(sessions);
  }
}
