package com.simpolab.client_manager.electors;

import com.simpolab.client_manager.login.LoginSession;
import com.simpolab.client_manager.utils.HttpUtils;
import com.simpolab.client_manager.utils.JsonUtils;
import com.simpolab.client_manager.utils.SceneUtils;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ElectorsController implements Initializable {
  private Stage stage;
  private Scene scene;

  @FXML
  private ListView<Elector> lvElectors;
  @FXML
  private Button btnRevokeSelected;

  @FXML
  private void onBtnRevokeSelectedClicked(ActionEvent event) throws Exception {
    ObservableList<Elector> selectedElectors = lvElectors.getSelectionModel().getSelectedItems();
    for(Elector el : selectedElectors){
      HttpUtils.delete("/api/v1/elector/"+el.getId(), Map.of("Authorization", "Bearer " + HttpUtils.token));
    }
  }

  @FXML
  private void onBtnBackClicked(ActionEvent event) throws Exception {
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    SceneUtils.switchTo("../homepage/homepage.fxml", stage);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    lvElectors.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    String res = HttpUtils.get("/api/v1/elector", Map.of("Authorization", "Bearer "  + HttpUtils.token));

    System.out.println(HttpUtils.token);
    System.out.println(res);
    List<Elector> electors = JsonUtils.parseJsonArray(res, Elector.class);

    lvElectors.getItems().addAll(electors);
  }
}
