package com.simpolab.client_manager.session;

import com.simpolab.client_manager.domain.Group;
import com.simpolab.client_manager.utils.AuthHandler;
import com.simpolab.client_manager.utils.HttpUtils;
import com.simpolab.client_manager.utils.JsonUtils;
import com.simpolab.client_manager.utils.SceneUtils;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AddGroupsController implements Initializable {

  private List<Group> selectedGroups;
  private static Long sessionId;

  private static String referer;

  @FXML
  private ListView<Group> lvAvailableGroups;

  @FXML
  private ListView<Group> lvAddedGroups;

  @FXML
  private void onBtnAddGroupsClicked(ActionEvent event) throws Exception {
    ObservableList<Group> groups = lvAvailableGroups.getSelectionModel().getSelectedItems();

    // imagine checking before shoving shit into lists
    for (Group group : groups) HttpUtils.put(
      "/api/v1/session/" + sessionId + "/group/" + group.getId(),
      Map.of("Authorization", "Bearer " + AuthHandler.getAccessToken()),
      null
    );

    selectedGroups.addAll(groups);

    refreshLists();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    if (sessionId == null) throw new IllegalStateException(
      "The init method should be called before activating this controller"
    );

    lvAddedGroups.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    lvAvailableGroups.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    selectedGroups = new ArrayList<>();

    refreshLists();
  }

  public static void init(long sessionId, String referer) {
    if (AddGroupsController.sessionId != null) throw new IllegalStateException(
      "Trying to init an already initialized controller"
    );

    AddGroupsController.sessionId = sessionId;
    AddGroupsController.referer = referer;
  }

  private static void resetState() {
    sessionId = null;
    referer = null;
  }

  @FXML
  private void onBtnRemoveGroupsClicked(ActionEvent event) throws Exception {
    ObservableList<Group> groups = lvAddedGroups.getSelectionModel().getSelectedItems();

    for (Group group : groups) HttpUtils.delete(
      "/api/v1/session/" + sessionId + "/group/" + group.getId(),
      Map.of("Authorization", "Bearer " + AuthHandler.getAccessToken())
    );

    selectedGroups.removeIf(group ->
      groups.stream().anyMatch(selGroup -> selGroup.getId() == group.getId())
    );

    refreshLists();
  }

  @FXML
  private void onBtnBackClicked(ActionEvent event) throws Exception {
    var referer = AddGroupsController.referer;

    resetState();
    SceneUtils.switchTo(referer);
  }

  @FXML
  private void onBtnCreateClicked(ActionEvent event) throws Exception {
    resetState();
    SceneUtils.switchToHomepage();
  }

  private void refreshLists() {
    // clear lists
    lvAvailableGroups.getItems().clear();
    lvAddedGroups.getItems().clear();

    // get new values
    String availableGroupsJson = HttpUtils.get("/api/v1/group");
    List<Group> availableGroups = JsonUtils.parseJsonArray(availableGroupsJson, Group.class);

    String selectedGroupsJson = HttpUtils.get(("/api/v1/session/" + sessionId + "/group"));
    List<Group> selectedGroups = JsonUtils.parseJsonArray(selectedGroupsJson, Group.class);

    // remove from the available groups all the ones that have already been added
    availableGroups.removeAll(selectedGroups);

    // set the new lists' values
    lvAvailableGroups.getItems().addAll(availableGroups);
    lvAddedGroups.getItems().addAll(selectedGroups);
  }
}
