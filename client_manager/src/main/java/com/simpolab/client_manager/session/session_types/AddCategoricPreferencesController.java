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

  private List<Option> allOptions;
  private List<Option> options;
  private List<Option> suboptions;

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
    String prompt = txtPrompt.getText();
    if (prompt.isBlank()) {
      return;
    }

    if (
      options.stream().anyMatch(opt -> opt.getValue().toLowerCase().equals(prompt.toLowerCase()))
    ) {
      AlertUtils.alert(Alert.AlertType.ERROR, "Cannot insert duplicate option");
      return;
    }

    Option option = new Option(prompt);
    HttpUtils.put("/api/v1/session/" + sessionId + "/option", option);

    txtPrompt.clear();
    refreshOptions();
  }

  @FXML
  private void onBtnAddSuboptionClicked(ActionEvent event) throws Exception {
    // end if there is no option available
    if (lvOptions.getItems().isEmpty()) {
      AlertUtils.alert(
        Alert.AlertType.ERROR,
        "You first have to select an option to add a suboption"
      );
      return;
    }

    // if no option is selected then select the first one
    if (lvOptions.getSelectionModel().getSelectedIndex() < 0) lvOptions
      .getSelectionModel()
      .select(0);

    // end if the prompt is empty
    String prompt = txtPrompt.getText();
    if (prompt.isBlank()) {
      return;
    }

    if (
      suboptions.stream().anyMatch(opt -> opt.getValue().toLowerCase().equals(prompt.toLowerCase()))
    ) {
      AlertUtils.alert(Alert.AlertType.ERROR, "Cannot insert duplicate suboption");
      return;
    }

    //create suboption
    Option selectedOption = lvOptions.getSelectionModel().getSelectedItem();
    Option suboption = new Option(prompt);

    HttpUtils.put(
      "/api/v1/session/" + sessionId + "/option/" + selectedOption.getId() + "/suboption",
      suboption
    );

    txtPrompt.clear();
    refreshSuboptions();
  }

  /**
   * Deletes the selected entry on the option list view
   * @param event
   */
  @FXML
  private void onBtnDeleteOptionClicked(ActionEvent event) {
    Option selectedOption = lvOptions.getSelectionModel().getSelectedItem();
    HttpUtils.delete("/api/v1/session/option/" + selectedOption.getId());
    refreshOptions();
    refreshSuboptions();
  }

  /**
   * Deletes the selected entry on the suboption view
   * @param event
   */
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

  /**
   * Loads the session's options and displays only the parent options on the option's list view
   */
  private void refreshOptions() {
    int lastIndex = lvOptions.getSelectionModel().getSelectedIndex();
    lvOptions.getItems().clear();

    String optionsJson = HttpUtils.get("/api/v1/session/" + sessionId + "/option");
    allOptions = JsonUtils.parseJsonArray(optionsJson, Option.class);
    if (allOptions.isEmpty()) return;

    options = allOptions.stream().filter(opt -> opt.getParentOptionId() < 0).toList();
    lvOptions.getItems().addAll(options);
    if (lvOptions.getSelectionModel().getSelectedIndex() < 0) lvOptions
      .getSelectionModel()
      .select(0); else lvOptions.getSelectionModel().select(lastIndex);
  }

  /**
   * If no option is available for the current session ends,
   * loads the session's options and displays only the children of the option selected on the option list view in the
   * suboption list view otherwise
   */
  private void refreshSuboptions() {
    lvSuboptions.getItems().clear();

    String optionsJson = HttpUtils.get("/api/v1/session/" + sessionId + "/option");
    allOptions = JsonUtils.parseJsonArray(optionsJson, Option.class);
    if (allOptions.isEmpty()) return;

    // selects the first option if none is selected
    if (lvOptions.getSelectionModel().getSelectedIndex() < 0) lvOptions
      .getSelectionModel()
      .select(0);
    // filters only the selected option's children
    Option selectedOption = lvOptions.getSelectionModel().getSelectedItem();
    suboptions =
      allOptions
        .stream()
        .filter(opt -> opt.getParentOptionId().intValue() == selectedOption.getId())
        .toList();

    lvSuboptions.getItems().addAll(suboptions);
  }
}
