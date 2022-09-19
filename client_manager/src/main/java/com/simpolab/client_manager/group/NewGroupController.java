package com.simpolab.client_manager.group;

import com.simpolab.client_manager.domain.Elector;
import com.simpolab.client_manager.domain.Group;
import com.simpolab.client_manager.utils.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NewGroupController implements Initializable {

  private List<Elector> requiredElectors;

  @FXML
  private TextField txtGroupName;

  @FXML
  private ListView lvAvailableElectors;

  @FXML
  private ListView lvAddedElectors;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    lvAddedElectors.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    lvAvailableElectors.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    requiredElectors = new ArrayList<>();

    refreshLists();
  }

  @FXML
  private void onBtnCreateGroupClicked(ActionEvent event) throws Exception {
    // check if name and electors inserted
    String groupName = txtGroupName.getText();
    if (groupName.isBlank()) {
      AlertUtils.alert(Alert.AlertType.ERROR, "The group's name cannot be blank");
      return;
    }
    if (requiredElectors.isEmpty()) {
      AlertUtils.alert(Alert.AlertType.ERROR, "Insert at least one elector");
      return;
    }

    // create group
    String groupJson = HttpUtils.postJson(
      "/api/v1/group",
      Map.of("Authorization", "Bearer " + AuthHandler.getAccessToken()),
      new Group(groupName)
    );
    Group newGroup = JsonUtils.parseJson(groupJson, Group.class);

    for (Elector el : requiredElectors) {
      HttpUtils.put(
        "/api/v1/group/" + newGroup.getId() + "/elector/" + el.getId(),
        Map.of("Authorization", "Bearer " + AuthHandler.getAccessToken()),
        null
      );
    }

    SceneUtils.switchToHomepage();
  }

  @FXML
  private void onBtnBackClicked(ActionEvent event) throws Exception {
    SceneUtils.switchToHomepage();
  }

  @FXML
  private void onBtnAddElectorsClicked(ActionEvent event) throws Exception {
    // retrieve selected electors
    ObservableList<Elector> selectedElectors = lvAvailableElectors
      .getSelectionModel()
      .getSelectedItems();

    // remove duplicates electors if any
    ObservableList<Elector> finalSelectedElectors = selectedElectors;
    requiredElectors.removeIf(curEl ->
      finalSelectedElectors.stream().anyMatch(reqEl -> reqEl.getId().equals(curEl.getId()))
    );

    // add electors
    requiredElectors.addAll(selectedElectors);

    refreshLists();
  }

  @FXML
  private void onBtnRemoveElectorsClicked(ActionEvent event) throws Exception {
    // retrieve selected electors
    ObservableList<Elector> selectedElectors = lvAddedElectors
      .getSelectionModel()
      .getSelectedItems();

    // remove selected electors if found
    ObservableList<Elector> finalSelectedElectors = selectedElectors;
    requiredElectors.removeIf(curEl ->
      finalSelectedElectors.stream().anyMatch(reqEl -> reqEl.getId().equals(curEl.getId()))
    );

    refreshLists();
  }

  /**
   *
   */
  private void refreshLists() {
    // clear list view
    deleteListEntries();

    String electorsJson = HttpUtils.get(
      "/api/v1/elector",
      Map.of("Authorization", "Bearer " + AuthHandler.getAccessToken())
    );

    List<Elector> electors = JsonUtils.parseJsonArray(electorsJson, Elector.class);

    log.debug(electors.toString());
    log.debug(requiredElectors.toString());
    List<Elector> finalRequiredElectors = requiredElectors;
    electors.removeIf(curEl ->
      finalRequiredElectors.stream().anyMatch(reqEl -> reqEl.getId().equals(curEl.getId()))
    );

    lvAvailableElectors.getItems().addAll(electors);
    lvAddedElectors.getItems().addAll(requiredElectors);
  }

  private void deleteListEntries() {
    lvAvailableElectors.getItems().clear();
    lvAddedElectors.getItems().clear();
  }
}
