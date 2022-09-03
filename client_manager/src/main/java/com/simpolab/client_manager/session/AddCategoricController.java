package com.simpolab.client_manager.session;

import com.simpolab.client_manager.login.AuthHandler;
import com.simpolab.client_manager.utils.HttpUtils;
import com.simpolab.client_manager.utils.JsonUtils;
import com.simpolab.client_manager.utils.SceneUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class AddCategoricController implements Initializable {
  private static int sessionId;

  @FXML
  private TextField txtOption;
  @FXML
  private ListView<Option> lvOptions;

  @FXML
  private void onBtnAddOptionClicked(ActionEvent event) throws Exception{
    Option option = new Option(txtOption.getText());
    HttpUtils.put("/api/v1/session/" + sessionId + "/option", Map.of("Authorization", "Bearer " + AuthHandler.getAccessToken()), option);

    txtOption.clear();
  }

  @FXML
  private void onBtnBackClicked(ActionEvent event) throws Exception{
    SceneUtils.switchTo("session/new_session.fxml");
  }

  @FXML
  private void onBtnNextClicked(ActionEvent event) throws Exception{
    AddGroupsController.initSession(sessionId);
    SceneUtils.switchTo("session/add_groups.fxml");
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    lvOptions.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
  }

  public static void initSession(int initSessionId){
    sessionId = initSessionId;
  }

  private void refreshLists(){
    String optionsJson = HttpUtils.get("/api/v1/session/"+sessionId+"/option", Map.of("Authorization", "Bearer " + AuthHandler.getAccessToken()));
    List<Option> options = JsonUtils.parseJsonArray(optionsJson, Option.class);

    lvOptions.getItems().clear();
    lvOptions.getItems().addAll(options);
  }
}
