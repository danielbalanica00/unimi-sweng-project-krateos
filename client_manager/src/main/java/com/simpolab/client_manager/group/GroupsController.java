package com.simpolab.client_manager.group;

import com.simpolab.client_manager.login.LoginSession;
import com.simpolab.client_manager.utils.HttpUtils;
import com.simpolab.client_manager.utils.JsonUtils;
import com.simpolab.client_manager.utils.SceneSwitch;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class GroupsController implements Initializable {

  private Stage stage;
  private Scene scene;

  @FXML
  private TextField txtSearchGroup;

  @FXML
  private Button btnSearchGroup;

  @FXML
  private Button btnCreateGroup;

  @FXML
  private ListView<Group> lvGroups;

  @FXML
  private Button btnOpenGroup;

  @FXML
  private void onBtnSearchGroupClicked(ActionEvent event) throws Exception {
  }

  @FXML
  private void onBtnCreateGroupClicked(ActionEvent event) throws Exception {
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    SceneSwitch.switchTo("../group/new_group.fxml", stage);
  }

  @FXML
  private void onBtnOpenGroupClicked(ActionEvent event) throws Exception{
    int selectedIndex = lvGroups.getSelectionModel().getSelectedIndex();
    Group selectedGroup = lvGroups.getItems().get(selectedIndex);
    GroupController.initGroup(selectedGroup);

    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    SceneSwitch.switchTo("../group/group.fxml", stage);
  }

  @FXML
  private void onBtnBackClicked(ActionEvent event) throws Exception {
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    SceneSwitch.switchTo("../homepage/homepage.fxml", stage);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    lvGroups.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    String res = HttpUtils.get("/api/v1/group", Map.of("Authorization", "Bearer " + LoginSession.getAccessToken()));

    List<Group> groups = JsonUtils.parseJsonArray(res, Group.class);

    lvGroups.getItems().addAll(groups);
  }
}
