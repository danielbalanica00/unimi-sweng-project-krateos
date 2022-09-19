package com.simpolab.client_manager.session.session_types;

import com.simpolab.client_manager.domain.Option;
import com.simpolab.client_manager.session.AddGroupsController;
import com.simpolab.client_manager.session.NewSessionController;
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

public class AddCategoricAndOrdinalController implements Initializable {

  private static long sessionId;
  private List<Option> options;

  @FXML
  private TextField txtOption;

  @FXML
  private ListView<Option> lvOptions;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    lvOptions.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    refreshLists();
  }

  /**
   * Adds an option with txtOption's as value if not blank
   * @param event
   */
  @FXML
  private void onBtnAddOptionClicked(ActionEvent event) {
    String prompt = txtOption.getText();
    if (prompt.isBlank()) return;

    if (
      options.stream().anyMatch(opt -> opt.getValue().toLowerCase().equals(prompt.toLowerCase()))
    ) {
      AlertUtils.alert(Alert.AlertType.ERROR, "Cannot insert duplicate options");
      return;
    }

    Option option = new Option(txtOption.getText());
    HttpUtils.put("/api/v1/session/" + sessionId + "/option", option);

    txtOption.clear();
    refreshLists();
  }

  @FXML
  private void onBtnBackClicked(ActionEvent event) throws Exception {
    NewSessionController.init(sessionId);
    SceneUtils.switchTo("session/new_session.fxml");
  }

  @FXML
  private void onBtnNextClicked(ActionEvent event) throws Exception {
    AddGroupsController.init(sessionId, "session/add_categoric.fxml");
    SceneUtils.switchTo("session/add_groups.fxml");
  }

  /**
   * Deletes the selected option
   * @param event
   */
  @FXML
  private void onBtnDeleteOptionClicked(ActionEvent event) {
    Option selectedOption = lvOptions.getSelectionModel().getSelectedItem();
    HttpUtils.delete("/api/v1/session/option/" + selectedOption.getId());
    refreshLists();
  }

  public static void init(long initSessionId) {
    sessionId = initSessionId;
  }

  /**
   * Loads the current session's options and displays them in the option list view
   */
  private void refreshLists() {
    String optionsJson = HttpUtils.get("/api/v1/session/" + sessionId + "/option");
    options = JsonUtils.parseJsonArray(optionsJson, Option.class);

    if (options.isEmpty()) return;

    lvOptions.getItems().clear();
    lvOptions.getItems().addAll(options);
  }
}
