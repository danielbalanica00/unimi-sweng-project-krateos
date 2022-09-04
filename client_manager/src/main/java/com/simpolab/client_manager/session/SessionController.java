package com.simpolab.client_manager.session;

import com.simpolab.client_manager.domain.Session;
import com.simpolab.client_manager.utils.HttpUtils;
import com.simpolab.client_manager.utils.JsonUtils;
import com.simpolab.client_manager.utils.SceneUtils;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SessionController implements Initializable {

  private static Session session;

  @FXML
  private Text lblSessionName;

  @FXML
  private Text lblEndsOn;

  @FXML
  private Text lblState;

  @FXML
  private Button btnStop;

  @FXML
  private Button btnAbort;

  @FXML
  private Button btnStart;

  public static void init(Session initSession) {
    session = initSession;
  }

  @FXML
  private void onBtnStopClicked(ActionEvent event) throws Exception {
    HttpUtils.patch(
      "/api/v1/session/" +
      session.getId() +
      "/state/" +
      Session.State.ENDED.toString().toLowerCase()
    );
    refreshSession();
  }

  @FXML
  private void onBtnAbortClicked(ActionEvent event) throws Exception {
    HttpUtils.patch(
      "/api/v1/session/" +
      session.getId() +
      "/state/" +
      Session.State.CANCELLED.toString().toLowerCase()
    );
    refreshSession();
  }

  @FXML
  private void onBtnStartClicked(ActionEvent event) throws Exception {
    HttpUtils.patch(
      "/api/v1/session/" +
      session.getId() +
      "/state/" +
      Session.State.ACTIVE.toString().toLowerCase()
    );
    refreshSession();
  }

  @FXML
  private void onBtnDeleteClicked(ActionEvent event) throws Exception {
    HttpUtils.delete("/api/v1/session/" + session.getId());
    SceneUtils.switchTo("session/sessions.fxml");
  }

  @FXML
  private void onBtnBackClicked(ActionEvent event) throws Exception {
    session = null;

    SceneUtils.switchTo("session/sessions.fxml");
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    refreshSessionInfo();
  }

  private void refreshSession() throws Exception {
    String newSessionJson = HttpUtils.get("/api/v1/session/" + session.getId());
    Session newSession = JsonUtils.parseJson(newSessionJson, Session.class);

    init(newSession);
    SceneUtils.switchTo("session/session.fxml");
    refreshSessionInfo();
  }

  private void refreshSessionInfo() {
    lblSessionName.setText(session.getName());
    lblEndsOn.setText("Ends on: " + new Date(session.getEndsOn() * 1000).toString());
    lblState.setText(session.getState().toString());

    if (!session.getState().equals(Session.State.INACTIVE)) {
      btnStart.setDisable(true);
    }

    if (!session.getState().equals(Session.State.ACTIVE)) {
      btnStop.setDisable(true);
      btnAbort.setDisable(true);
    }
  }
}
