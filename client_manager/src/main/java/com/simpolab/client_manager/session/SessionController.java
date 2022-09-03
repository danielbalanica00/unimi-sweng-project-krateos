package com.simpolab.client_manager.session;

import com.simpolab.client_manager.domain.Session;
import com.simpolab.client_manager.utils.SceneUtils;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SessionController implements Initializable {

  private Stage stage;
  private static Session selectedSession;

  @FXML
  private Text lblSessionName;

  @FXML
  private Text lblRemainingTime;

  @FXML
  private Button btnStop;

  @FXML
  private Button btnAbort;

  public static void initSession(Session session) {
    selectedSession = session;
  }

  @FXML
  private void onBtnStopClicked(ActionEvent event) throws Exception {}

  @FXML
  private void onBtnAbortClicked(ActionEvent event) throws Exception {}

  @FXML
  private void onBtnBackClicked(ActionEvent event) throws Exception {
    selectedSession = null;

    SceneUtils.switchTo("session/sessions.fxml");
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    lblSessionName.setText(selectedSession.getName());
    lblRemainingTime.setText(new Date(selectedSession.getEndsOn() * 1000).toString());
  }
}
