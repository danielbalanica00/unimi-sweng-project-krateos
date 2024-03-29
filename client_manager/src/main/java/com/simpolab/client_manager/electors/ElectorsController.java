package com.simpolab.client_manager.electors;

import com.google.gson.JsonSyntaxException;
import com.simpolab.client_manager.domain.Elector;
import com.simpolab.client_manager.utils.*;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ElectorsController implements Initializable {

  @FXML
  private ListView<Elector> lvElectors;

  /**
   * retrieves and displays every user
   *
   * @param location
   * The location used to resolve relative paths for the root object, or
   * {@code null} if the location is not known.
   *
   * @param resources
   * The resources used to localize the root object, or {@code null} if
   * the root object was not localized.
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    lvElectors.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    refreshList();
  }

  private void refreshList() {
    String electorsJson = HttpUtils.get(
      "/api/v1/elector",
      Map.of("Authorization", "Bearer " + AuthHandler.getAccessToken())
    );

    try {
      var electors = JsonUtils.parseJsonArray(electorsJson, Elector.class);
      lvElectors.getItems().addAll(electors);
    } catch (JsonSyntaxException e) {
      log.error("Error parsing json array" + electorsJson);
    }
  }

  /**
   * Revokes credentials to the selected user
   */
  @FXML
  private void onBtnRevokeSelectedClicked(ActionEvent event) throws Exception {
    if (
      !AlertUtils.confirmAlert("Do you want to revoke the selected elector's credentials?")
    ) return;

    ObservableList<Elector> selectedElectors = lvElectors.getSelectionModel().getSelectedItems();
    for (Elector el : selectedElectors) {
      HttpUtils.delete(
        "/api/v1/elector/" + el.getId(),
        Map.of("Authorization", "Bearer " + AuthHandler.getAccessToken())
      );
    }
    refreshList();
  }

  @FXML
  private void onBtnBackClicked(ActionEvent event) throws Exception {
    SceneUtils.switchToHomepage();
  }
}
