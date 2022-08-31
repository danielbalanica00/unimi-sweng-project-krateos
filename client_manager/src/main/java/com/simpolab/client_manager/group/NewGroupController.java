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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class NewGroupController implements Initializable {

  private Stage stage;
  private Scene scene;
  private List<Elector> requiredElectors;

  @FXML
  private TextField txtGroupName;
  @FXML
  private Button btnCreateGroup;
  @FXML
  private ListView lvAvailableElectors;
  @FXML
  private ListView lvAddedElectors;
  @FXML
  private Button btnAddElectors;
  @FXML
  private Button btnRemoveElectors;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    lvAddedElectors.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    lvAvailableElectors.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    requiredElectors = new ArrayList<Elector>();

    refreshLists();
  }

  @FXML
  private void onBtnCreateGroupClicked(ActionEvent event) throws Exception{
    // api request

    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    SceneSwitch.switchTo("homepage/homepage.fxml", stage);
  }

  @FXML
  private void onBtnBackClicked(ActionEvent event) throws Exception {
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    SceneSwitch.switchTo("homepage/homepage.fxml", stage);
  }

  @FXML
  private void onBtnAddElectorsClicked(ActionEvent event) throws Exception{
    ObservableList<Elector> selectedElectors = new ReadOnlyListWrapper<>();
    selectedElectors = lvAvailableElectors.getSelectionModel().getSelectedItems();

    requiredElectors.removeAll(selectedElectors);
    requiredElectors.addAll(selectedElectors);

    refreshLists();
  }

  @FXML
  private void onBtnRemoveElectorsClicked(ActionEvent event) throws Exception{
    ObservableList<Elector> selectedElectors = new ReadOnlyListWrapper<>();
    selectedElectors = lvAddedElectors.getSelectionModel().getSelectedItems();

    requiredElectors.removeAll(selectedElectors);

    refreshLists();
  }

  private void refreshLists(){
    deleteListEntries();

    String electorsJson = "";

    List<Elector> electors = new ArrayList<>();

    ObjectMapper mapper = new ObjectMapper();

    try {
      electors = mapper.readValue(electorsJson, new TypeReference<List<Elector>>() {});
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    electors.remove(requiredElectors);

    lvAvailableElectors.getItems().addAll(electors);
    lvAddedElectors.getItems().addAll(requiredElectors);
  }

  private void deleteListEntries(){
    lvAvailableElectors.getItems().removeAll();
    lvAddedElectors.getItems().removeAll();
  }
}
