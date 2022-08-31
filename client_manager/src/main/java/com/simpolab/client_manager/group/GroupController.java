package com.simpolab.client_manager.group;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.simpolab.client_manager.utils.SceneSwitch;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import com.simpolab.client_manager.electors.Elector;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
    // non riporto selectedGroup a null perche' non quando avra' finito di
    // modificare questo gruppo tornera' su questa scena e dovra' vedere
    // ancora questo gruppo
    AddElectorsController.initGroup(selectedGroup);

    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    SceneSwitch.switchTo("../group/groups.fxml", stage);
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

    String res = "";
    List<Elector> electors = new ArrayList<>();
    ObjectMapper mapper = new ObjectMapper();

    try {
      electors = mapper.readValue(res, new TypeReference<List<Elector>>() {});
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    lvElectors.getItems().addAll(electors);
  }
}
