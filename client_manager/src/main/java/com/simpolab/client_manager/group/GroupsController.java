package com.simpolab.client_manager.group;

import com.google.gson.JsonSyntaxException;
import com.simpolab.client_manager.domain.Group;
import com.simpolab.client_manager.utils.AuthHandler;
import com.simpolab.client_manager.utils.HttpUtils;
import com.simpolab.client_manager.utils.JsonUtils;
import com.simpolab.client_manager.utils.SceneUtils;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GroupsController implements Initializable {

  @FXML
  private ListView<Group> lvGroups;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    lvGroups.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    String groupsJson = HttpUtils.get(
      "/api/v1/group",
      Map.of("Authorization", "Bearer " + AuthHandler.getAccessToken())
    );

    try {
      var groups = JsonUtils.parseJsonArray(groupsJson, Group.class);
      lvGroups.getItems().addAll(groups);
    } catch (JsonSyntaxException e) {
      log.error("Error parsing json array" + groupsJson);
    }
  }

  //  @FXML
  //  private void onBtnCreateGroupClicked(ActionEvent event) throws Exception {
  //    SceneUtils.switchTo("group/new_group.fxml");
  //  }

  @FXML
  private void onBtnOpenGroupClicked(ActionEvent event) throws Exception {
    int selectedIndex = lvGroups.getSelectionModel().getSelectedIndex();
    Group selectedGroup = lvGroups.getItems().get(selectedIndex);
    GroupController.initGroup(selectedGroup);

    SceneUtils.switchTo("group/group.fxml");
  }

  @FXML
  private void onBtnBackClicked(ActionEvent event) throws Exception {
    SceneUtils.switchToHomepage();
  }
}
