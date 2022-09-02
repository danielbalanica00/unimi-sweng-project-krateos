package com.simpolab.client_manager.group;

import com.simpolab.client_manager.electors.Elector;
import com.simpolab.client_manager.login.LoginSession;
import com.simpolab.client_manager.utils.HttpUtils;
import com.simpolab.client_manager.utils.JsonUtils;
import com.simpolab.client_manager.utils.SceneSwitch;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class AddElectorsController implements Initializable {
  private Stage stage;
  private static Group selectedGroup;

  @FXML
  private ListView<Elector> lvAvailableElectors;
  @FXML
  private ListView<Elector> lvAddedElectors;

  public static void initGroup(Group group){
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
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    SceneSwitch.switchTo("../group/group.fxml", stage);
  }

  @FXML
  private void onBtnAddElectorsClicked(ActionEvent event) throws Exception{
    ObservableList<Elector> selectedElectors = new ReadOnlyListWrapper<>();
    selectedElectors = lvAvailableElectors.getSelectionModel().getSelectedItems();

    // api put request

    refreshLists();
  }

  @FXML
  private void onBtnRemoveElectorsClicked(ActionEvent event) throws Exception{
    ObservableList<Elector> selectedElectors = new ReadOnlyListWrapper<>();
    selectedElectors = lvAddedElectors.getSelectionModel().getSelectedItems();

    // api del request

    refreshLists();
  }

  private void refreshLists(){
    deleteListEntries();

    String electorsJson = HttpUtils.get("/api/v1/elector", Map.of("Authorization", "Bearer " + LoginSession.getAccessToken()));
    String groupElectorsJson = HttpUtils.get("/api/v1/group/"+selectedGroup.getId()+"/elector", Map.of("Authorization", "Bearer " + LoginSession.getAccessToken()));

    List<Elector> electors = JsonUtils.parseJsonArray(electorsJson, Elector.class);
    List<Elector> groupElectors = JsonUtils.parseJsonArray(groupElectorsJson, Elector.class);

    electors.removeIf(curEl -> groupElectors.stream().anyMatch(reqEl-> reqEl.getId().equals(curEl.getId())));

    lvAvailableElectors.getItems().addAll(electors);
    lvAddedElectors.getItems().addAll(groupElectors);
  }

  private void deleteListEntries(){
    lvAvailableElectors.getItems().clear();
    lvAddedElectors.getItems().clear();
  }

}
