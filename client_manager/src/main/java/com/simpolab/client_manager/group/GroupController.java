package com.simpolab.client_manager.group;

import com.simpolab.client_manager.utils.HttpUtils;
import com.simpolab.client_manager.utils.JsonUtils;
import com.simpolab.client_manager.utils.SceneSwitch;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import com.simpolab.client_manager.electors.Elector;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class GroupController implements Initializable {
  private Stage stage;

  private static Group selectedGroup;

  @FXML
  private Text lblGroupName;
  @FXML
  private ListView<Elector> lvElectors;
  @FXML
  private Button btnEditGroup;

  public static void initGroup(Group group){
    selectedGroup = group;
  }

  @FXML
  private void onBtnEditGroupClicked(ActionEvent event) throws Exception{
    AddElectorsController.initGroup(selectedGroup);

    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    SceneSwitch.switchTo("../group/add_electors.fxml", stage);
  }

  @FXML
  private void onBtnBackClicked(ActionEvent event) throws Exception {
    selectedGroup = null;

    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    SceneSwitch.switchTo("../group/groups.fxml", stage);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    lblGroupName.setText(selectedGroup.getName());

    String electorsJson = HttpUtils.get("/api/v1/group/"+selectedGroup.getId()+"/elector", Map.of("Authorization", "Bearer "+ HttpUtils.token));

    List<Elector> electors = JsonUtils.parseJsonArray(electorsJson, Elector.class);

    lvElectors.getItems().addAll(electors);
  }
}
