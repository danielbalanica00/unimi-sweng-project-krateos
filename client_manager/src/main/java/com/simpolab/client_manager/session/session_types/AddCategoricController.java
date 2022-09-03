package com.simpolab.client_manager.session.session_types;

import com.simpolab.client_manager.domain.Option;
import com.simpolab.client_manager.session.AddGroupsController;
import com.simpolab.client_manager.session.NewSessionController;
import com.simpolab.client_manager.utils.HttpUtils;
import com.simpolab.client_manager.utils.JsonUtils;
import com.simpolab.client_manager.utils.SceneUtils;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;

public class AddCategoricController implements Initializable {

  private static long sessionId;

  @FXML
  private TextField txtOption;

  @FXML
  private ListView<Option> lvOptions;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    lvOptions.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    refreshLists();
  }

  @FXML
  private void onBtnAddOptionClicked(ActionEvent event) throws Exception {
    Option option = new Option(txtOption.getText());
    HttpUtils.put("/api/v1/session/" + sessionId + "/option", option);

    txtOption.clear();
    refreshLists();
  }

  @FXML
  private void onBtnBackClicked(ActionEvent event) throws Exception {
    NewSessionController.init(sessionId);
    SceneUtils.switchTo("session/new_session.fxml");
  }

  @FXML
  private void onBtnNextClicked(ActionEvent event) throws Exception {
    AddGroupsController.init(sessionId, "session/add_categoric.fxml");
    SceneUtils.switchTo("session/add_groups.fxml");
  }

  public static void init(long initSessionId) {
    sessionId = initSessionId;
  }

  private void refreshLists() {
    String optionsJson = HttpUtils.get("/api/v1/session/" + sessionId + "/option");
    List<Option> options = JsonUtils.parseJsonArray(optionsJson, Option.class);

    lvOptions.getItems().clear();
    lvOptions.getItems().addAll(options);
  }
}
