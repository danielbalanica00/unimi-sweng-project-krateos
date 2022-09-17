package com.simpolab.client_manager.session.session_types;

import com.simpolab.client_manager.domain.Option;
import com.simpolab.client_manager.session.AddGroupsController;
import com.simpolab.client_manager.utils.AlertUtils;
import com.simpolab.client_manager.utils.HttpUtils;
import com.simpolab.client_manager.utils.JsonUtils;
import com.simpolab.client_manager.utils.SceneUtils;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class AddCategoricPreferencesController implements Initializable {

  private static long sessionId;
  private List<Option> options;

  @FXML
  private TextField txtPrompt;

  @FXML
  private ListView<Option> lvOptions;

  @FXML
  private ListView<Option> lvSuboptions;

  public static void init(long initSessionId) {
    sessionId = initSessionId;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    lvOptions.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    lvSuboptions.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
  }

  @FXML
  private void onLvOptionsClicked(MouseEvent mouseEvent) throws Exception {
    refreshSuboptions();
  }

  @FXML
  private void onBtnAddOptionClicked(ActionEvent event) throws Exception {
    Option option = new Option(txtPrompt.getText());
    HttpUtils.put("/api/v1/session/" + sessionId + "/option", option);

    txtPrompt.clear();
    refreshOptions();
  }

  @FXML
  private void onBtnAddSuboptionClicked(ActionEvent event) throws Exception {
    if (lvOptions.getItems().isEmpty()) {
      AlertUtils.alert(
        Alert.AlertType.ERROR,
        "You first have to select an option to add a suboption"
      );
      return;
    }
    if (lvOptions.getSelectionModel().getSelectedIndex() < 0) lvOptions
      .getSelectionModel()
      .select(0);

    Option selectedOption = lvOptions.getSelectionModel().getSelectedItem();
    Option suboption = new Option(txtPrompt.getText());

    HttpUtils.put(
      "/api/v1/session/" + sessionId + "/option/" + selectedOption.getId() + "/suboption",
      suboption
    );

    txtPrompt.clear();
    refreshSuboptions();
  }

  @FXML
  private void onBtnDeleteOptionClicked(ActionEvent event) {
    Option selectedOption = lvOptions.getSelectionModel().getSelectedItem();
    HttpUtils.delete("/api/v1/session/option/" + selectedOption.getId());
    refreshOptions();
    refreshSuboptions();
  }

  @FXML
  private void onBtnDeleteSuboptionClicked(ActionEvent event) {
    Option selectedOption = lvSuboptions.getSelectionModel().getSelectedItem();
    HttpUtils.delete("/api/v1/session/option/" + selectedOption.getId());
    refreshSuboptions();
  }

  @FXML
  private void onBtnBackClicked(ActionEvent event) throws Exception {
    SceneUtils.switchTo("session/new_session.fxml");
  }

  @FXML
  private void onBtnNextClicked(ActionEvent event) throws Exception {
    AddGroupsController.init(sessionId, "session/add_categoric_preferences");
    SceneUtils.switchTo("session/add_groups.fxml");
  }

  private void refreshOptions() {
    int lastIndex = lvOptions.getSelectionModel().getSelectedIndex();
    lvOptions.getItems().clear();

    String optionsJson = HttpUtils.get("/api/v1/session/" + sessionId + "/option");
    options = JsonUtils.parseJsonArray(optionsJson, Option.class);
    if (options.isEmpty()) return;

    lvOptions
      .getItems()
      .addAll(options.stream().filter(opt -> opt.getParentOptionId() < 0).toList());
    if (lvOptions.getSelectionModel().getSelectedIndex() < 0) lvOptions
      .getSelectionModel()
      .select(0); else lvOptions.getSelectionModel().select(lastIndex);
  }

  private void refreshSuboptions() {
    lvSuboptions.getItems().clear();

    String optionsJson = HttpUtils.get("/api/v1/session/" + sessionId + "/option");
    options = JsonUtils.parseJsonArray(optionsJson, Option.class);
    if (options.isEmpty()) return;

    Option selectedOption = lvOptions.getSelectionModel().getSelectedItem();
    List<Option> suboptions = options
      .stream()
      .filter(opt -> opt.getParentOptionId().intValue() == selectedOption.getId())
      .toList();

    lvSuboptions.getItems().addAll(suboptions);
  }
}
