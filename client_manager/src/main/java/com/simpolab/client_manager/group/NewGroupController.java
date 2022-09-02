package com.simpolab.client_manager.group;

import com.simpolab.client_manager.electors.Elector;
import com.simpolab.client_manager.login.AuthHandler;
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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
  private void onBtnCreateGroupClicked(ActionEvent event) throws Exception {
    String groupJson = HttpUtils.postJson(
      "/api/v1/group",
      Map.of("Authorization", "Bearer " + AuthHandler.getAccessToken()),
      new Group(txtGroupName.getText())
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
    ObservableList<Elector> selectedElectors = lvAvailableElectors
      .getSelectionModel()
      .getSelectedItems();

    ObservableList<Elector> finalSelectedElectors = selectedElectors;
    requiredElectors.removeIf(curEl ->
      finalSelectedElectors.stream().anyMatch(reqEl -> reqEl.getId().equals(curEl.getId()))
    );
    requiredElectors.addAll(selectedElectors);

    refreshLists();
  }

  @FXML
  private void onBtnRemoveElectorsClicked(ActionEvent event) throws Exception {
    ObservableList<Elector> selectedElectors = lvAddedElectors
      .getSelectionModel()
      .getSelectedItems();

    ObservableList<Elector> finalSelectedElectors = selectedElectors;
    requiredElectors.removeIf(curEl ->
      finalSelectedElectors.stream().anyMatch(reqEl -> reqEl.getId().equals(curEl.getId()))
    );

    refreshLists();
  }

  private void refreshLists() {
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
