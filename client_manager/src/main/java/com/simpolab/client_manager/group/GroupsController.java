package com.simpolab.client_manager.group;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simpolab.client_manager.utils.Api;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.simpolab.client_manager.utils.SceneSwitch;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

    String res = Api.get("/api/v1/group", Map.of("Authorization", "Bearer " + Api.token));

    List<Group> groups = Api.parseJsonArray(res, Group.class);

    lvGroups.getItems().addAll(groups);
  }
}
