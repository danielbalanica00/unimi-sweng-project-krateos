package com.simpolab.client_elector.homepage;

import com.simpolab.client_elector.domain.Session;
import com.simpolab.client_elector.session.SessionController;
import com.simpolab.client_elector.utils.HttpUtils;
import com.simpolab.client_elector.utils.JsonUtils;
import com.simpolab.client_elector.utils.SceneUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HomepageController implements Initializable {
  @FXML
  private Text lblWelcome;
  @FXML
  private ListView<Session> lvSessions;

  @FXML
  private void onBtnOpenClicked(ActionEvent event) throws Exception{
    Session session = lvSessions.getSelectionModel().getSelectedItem();
    SessionController.init(session);
    SceneUtils.switchTo("session/session.fxml");
  }

  @FXML
  private void onBtnSessionsHistoryClicked(ActionEvent event) throws Exception {
    SceneUtils.switchTo("session/sessions_history.fxml");
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    lvSessions.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    String sessionsJson = HttpUtils.get("/api/v1/session");
    List<Session> sessions = JsonUtils.parseJsonArray(sessionsJson, Session.class);

    lvSessions.getItems().addAll(sessions.stream().filter(session -> session.getState().equals(Session.State.ACTIVE)).toList());
  }
}
