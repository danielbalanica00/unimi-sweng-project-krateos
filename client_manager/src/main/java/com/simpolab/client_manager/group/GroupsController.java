package com.simpolab.client_manager.group;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simpolab.client_manager.utils.Api;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
    int selectedIndex = lvGroups.getSelectionModel().getSelectedIndex();
    Group selectedGroup = lvGroups.getItems().get(selectedIndex);
    GroupController.initGroup(selectedGroup);

    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    SceneSwitch.switchTo("../group/group.fxml", stage);
  }

  @FXML
  private void onBtnCreateGroupClicked(ActionEvent event) throws Exception {
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    SceneSwitch.switchTo("../group/new_group.fxml", stage);
  }

  @FXML
  private void onBtnBackClicked(ActionEvent event) throws Exception {
    System.out.println(getClass().getName());
    Parent root = FXMLLoader.load(getClass().getResource("../homepage/homepage.fxml"));
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    lvGroups.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    String res = "";

    ObjectMapper mapper = new ObjectMapper();
    List<Group> groups = new ArrayList<>();

    try {
      groups = mapper.readValue(res, new TypeReference<List<Group>>() {});
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    lvGroups.getItems().addAll(groups);
  }
}
