package com.simpolab.client_manager.group;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simpolab.client_manager.electors.Elector;
import com.simpolab.client_manager.utils.SceneSwitch;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddElectorsController implements Initializable {
  private Stage stage;
  private static Group selectedGroup;

  @FXML
  private ListView<Elector> lvAvailableElectors;
  @FXML
  private ListView<Elector> lvAddedElectors;
  @FXML
  private Button btnAddElectors;
  @FXML
  private Button btnRemoveElectors;

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

    // api post request

    refreshLists();
  }

  @FXML
  private void onBtnRemoveElectorsClicked(ActionEvent event) throws Exception{
    ObservableList<Elector> selectedElectors = new ReadOnlyListWrapper<>();
    selectedElectors = lvAddedElectors.getSelectionModel().getSelectedItems();

    // api post request

    refreshLists();
  }

  private void refreshLists(){
    deleteListEntries();

    String electorsJson = "";
    String groupElectorsJson = "";

    List<Elector> electors = new ArrayList<>();
    List<Elector> groupElectors = new ArrayList<>();

    ObjectMapper mapper = new ObjectMapper();

    try {
      electors = mapper.readValue(electorsJson, new TypeReference<List<Elector>>() {});
      groupElectors = mapper.readValue(groupElectorsJson, new TypeReference<List<Elector>>() {});
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    electors.remove(groupElectors);

    lvAvailableElectors.getItems().addAll(electors);
    lvAddedElectors.getItems().addAll(groupElectors);
  }

  private void deleteListEntries(){
    lvAvailableElectors.getItems().removeAll();
    lvAddedElectors.getItems().removeAll();
  }

}
