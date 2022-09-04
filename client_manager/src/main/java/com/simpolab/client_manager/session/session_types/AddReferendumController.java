package com.simpolab.client_manager.session.session_types;

import com.simpolab.client_manager.domain.Option;
import com.simpolab.client_manager.session.AddGroupsController;
import com.simpolab.client_manager.session.NewSessionController;
import com.simpolab.client_manager.utils.HttpUtils;
import com.simpolab.client_manager.utils.SceneUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AddReferendumController {

  private static long sessionId;

  @FXML
  private Button btnBack;

  @FXML
  private TextField txtPrompt;

  public static void init(long initSessionId) {
    sessionId = initSessionId;
  }

  @FXML
  private void onBtnBackClicked(ActionEvent event) throws Exception {
    NewSessionController.init(sessionId);
    SceneUtils.switchTo("session/new_session.fxml");
  }

  @FXML
  private void onBtnNextClicked() throws Exception {
    HttpUtils.put("/api/v1/session/" + sessionId + "/option", new Option(txtPrompt.getText()));

    AddGroupsController.init(sessionId, "session/new_session.fxml");
    SceneUtils.switchTo("session/add_groups.fxml");
  }
}
