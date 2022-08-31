package com.simpolab.client_manager.electors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simpolab.client_manager.utils.Api;
import com.simpolab.client_manager.utils.SceneSwitch;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class ElectorsController implements Initializable {

  private Stage stage;
  private Scene scene;

  @FXML
  private ListView lvElectors;

  @FXML
  private Button btnRevokeSelected;

  @FXML
  private void onBtnRevokeSelectedClicked(ActionEvent event) throws Exception {}

  @FXML
  private void onBtnBackClicked(ActionEvent event) throws Exception {
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    SceneSwitch.switchTo("../homepage/homepage.fxml", stage);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    String res = Api.sendGet("http://127.0.0.1:8080/api/v1/elector", Api.token);

    ObjectMapper mapper = new ObjectMapper();
    List<Elector> electors = new ArrayList<>();
    try {
      electors = mapper.readValue(res, new TypeReference<List<Elector>>() {});
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    lvElectors.getItems().addAll(electors);
  }
}
