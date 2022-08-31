package com.simpolab.client_manager.electors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.simpolab.client_manager.utils.Api;
import com.simpolab.client_manager.utils.SceneSwitch;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class ElectorsController implements Initializable {
  private Stage stage;
  private Scene scene;

  @FXML
  private ListView<Elector> lvElectors;
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
    String res = Api.get("/api/v1/elector", Map.of("Authorization", "Bearer "  + Api.token));

//    List<Elector> electors = new ArrayList<>();

    List<Elector> electors = new Gson().fromJson(res,  new TypeToken<List<Elector>>(){}.getType());
//
//    ObjectMapper mapper = new ObjectMapper();
//    try {
//      electors = mapper.readValue(res, new TypeReference<List<Elector>>() {});
//    } catch (JsonProcessingException e) {
//      throw new RuntimeException(e);
//    }

    lvElectors.getItems().addAll(electors);
    Elector e = lvElectors.getItems().get(0);
    System.out.println(e.getId()); // fuck yes
  }
}
