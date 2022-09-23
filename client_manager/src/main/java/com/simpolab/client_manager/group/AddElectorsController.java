package com.simpolab.client_manager.group;

import com.simpolab.client_manager.domain.Elector;
import com.simpolab.client_manager.domain.Group;
import com.simpolab.client_manager.utils.AuthHandler;
import com.simpolab.client_manager.utils.HttpUtils;
import com.simpolab.client_manager.utils.JsonUtils;
import com.simpolab.client_manager.utils.SceneUtils;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;

public class AddElectorsController implements Initializable {

  private static Group selectedGroup;

  @FXML
  private ListView<Elector> lvAvailableElectors;

  @FXML
  private ListView<Elector> lvAddedElectors;

  public static void initGroup(Group group) {
    selectedGroup = group;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    lvAddedElectors.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    lvAvailableElectors.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    refreshLists();
  }

  @FXML
  private void onBtnBackClicked(ActionEvent event) throws Exception {
    SceneUtils.switchTo("group/group.fxml");
  }

  @FXML
  private void onBtnAddElectorsClicked(ActionEvent event) throws Exception {
    ObservableList<Elector> selectedElectors = new ReadOnlyListWrapper<>();
    selectedElectors = lvAvailableElectors.getSelectionModel().getSelectedItems();

    for (Elector el : selectedElectors) {
      HttpUtils.put(
        "/api/v1/group/" + selectedGroup.getId() + "/elector/" + el.getId(),
        Map.of("Authorization", "Bearer " + AuthHandler.getAccessToken()),
        null
      );
    }

    refreshLists();
  }

  @FXML
  private void onBtnRemoveElectorsClicked(ActionEvent event) throws Exception {
    ObservableList<Elector> selectedElectors = new ReadOnlyListWrapper<>();
    selectedElectors = lvAddedElectors.getSelectionModel().getSelectedItems();

    for (Elector el : selectedElectors) {
      HttpUtils.delete(
        "/api/v1/group/" + selectedGroup.getId() + "/elector/" + el.getId(),
        Map.of("Authorization", "Bearer " + AuthHandler.getAccessToken())
      );
    }

    refreshLists();
  }

  private void refreshLists() {
    deleteListEntries();

    String electorsJson = HttpUtils.get(
      "/api/v1/elector",
      Map.of("Authorization", "Bearer " + AuthHandler.getAccessToken())
    );
    String groupElectorsJson = HttpUtils.get(
      "/api/v1/group/" + selectedGroup.getId() + "/elector",
      Map.of("Authorization", "Bearer " + AuthHandler.getAccessToken())
    );

    List<Elector> electors = JsonUtils.parseJsonArray(electorsJson, Elector.class);
    List<Elector> groupElectors = JsonUtils.parseJsonArray(groupElectorsJson, Elector.class);

    electors.removeIf(curEl ->
      groupElectors.stream().anyMatch(reqEl -> reqEl.getId().equals(curEl.getId()))
    );

    lvAvailableElectors.getItems().addAll(electors);
    lvAddedElectors.getItems().addAll(groupElectors);
  }

  private void deleteListEntries() {
    lvAvailableElectors.getItems().clear();
    lvAddedElectors.getItems().clear();
  }
}
