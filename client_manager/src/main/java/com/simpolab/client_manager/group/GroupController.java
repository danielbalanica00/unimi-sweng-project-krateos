package com.simpolab.client_manager.group;

import com.simpolab.client_manager.domain.Elector;
import com.simpolab.client_manager.domain.Group;
import com.simpolab.client_manager.utils.AuthHandler;
import com.simpolab.client_manager.utils.HttpUtils;
import com.simpolab.client_manager.utils.JsonUtils;
import com.simpolab.client_manager.utils.SceneUtils;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

public class GroupController implements Initializable {

  private static Group selectedGroup;

  @FXML
  private Text lblGroupName;

  @FXML
  private ListView<Elector> lvElectors;

  public static void initGroup(Group group) {
    selectedGroup = group;
  }

  @FXML
  private void onBtnEditGroupClicked(ActionEvent event) throws Exception {
    AddElectorsController.initGroup(selectedGroup);

    SceneUtils.switchTo("group/add_electors.fxml");
  }

  @FXML
  private void onBtnBackClicked(ActionEvent event) throws Exception {
    selectedGroup = null;
    SceneUtils.switchTo("group/groups.fxml");
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    lblGroupName.setText(selectedGroup.getName());

    String electorsJson = HttpUtils.get(
      "/api/v1/group/" + selectedGroup.getId() + "/elector",
      Map.of("Authorization", "Bearer " + AuthHandler.getAccessToken())
    );

    List<Elector> electors = JsonUtils.parseJsonArray(electorsJson, Elector.class);

    lvElectors.getItems().addAll(electors);
  }
}
