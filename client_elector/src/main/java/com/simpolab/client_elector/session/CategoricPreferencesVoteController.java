package com.simpolab.client_elector.session;

import com.simpolab.client_elector.domain.Option;
import com.simpolab.client_elector.domain.Session;
import com.simpolab.client_elector.domain.Vote;
import com.simpolab.client_elector.utils.AlertUtils;
import com.simpolab.client_elector.utils.HttpUtils;
import com.simpolab.client_elector.utils.JsonUtils;
import com.simpolab.client_elector.utils.SceneUtils;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class CategoricPreferencesVoteController implements Initializable {

  private static Session session;
  private static List<Option> options;

  @FXML
  private Text lblSessionName;

  @FXML
  private ListView<Option> lvOptions;

  @FXML
  private ListView<Option> lvSuboptions;

  public static void init(Session initSession) {
    session = initSession;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    lblSessionName.setText(session.getName());
    lvOptions.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    lvSuboptions.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    refreshOptions();
  }

  @FXML
  private void onLvOptionsClicked(MouseEvent mouseEvent) throws Exception {
    refreshSuboptions();
  }

  @FXML
  private void onBtnVoteClicked(ActionEvent event) throws Exception {
    // check if the user selected the required options
    if (lvOptions.getSelectionModel().getSelectedIndex() < 0) {
      AlertUtils.alert(Alert.AlertType.ERROR, "Select an option and one or more suboptions");
      return;
    }

    if (lvSuboptions.getSelectionModel().getSelectedIndex() < 0) {
      AlertUtils.alert(Alert.AlertType.ERROR, "Select at least one suboption");
      return;
    }

    // confirmation
    Alert alert = new Alert(
      Alert.AlertType.CONFIRMATION,
      "Send vote?",
      ButtonType.YES,
      ButtonType.NO
    );
    alert.showAndWait();
    if (alert.getResult() == ButtonType.NO) {
      return;
    }

    // retrieve choices
    Option option = lvOptions.getSelectionModel().getSelectedItem();
    List<Option> suboptions = lvSuboptions.getSelectionModel().getSelectedItems();

    //build vote
    List<Vote> votes = new ArrayList<>();
    votes.add(new Vote(option.getId()));
    for (Option opt : suboptions) {
      votes.add(new Vote(opt.getId()));
    }

    HttpUtils.postJson("/api/v1/session/" + session.getId() + "/vote", votes);

    SceneUtils.switchToHomepage();
  }

  @FXML
  private void onBtnBackClicked(ActionEvent event) throws Exception {
    SceneUtils.switchTo("session/session.fxml");
  }

  private void refreshOptions() {
    // save last selection before clearing
    int lastIndex = lvOptions.getSelectionModel().getSelectedIndex();
    lvOptions.getItems().clear();

    // retrieve updated options
    String optionsJson = HttpUtils.get("/api/v1/session/" + session.getId() + "/option");
    options = JsonUtils.parseJsonArray(optionsJson, Option.class);
    if (options.isEmpty()) return;

    // filter out only superoptions
    lvOptions
      .getItems()
      .addAll(options.stream().filter(opt -> opt.getParentOptionId() < 0).toList());

    // restore selection or force first position
    if (lvOptions.getSelectionModel().getSelectedIndex() < 0) lvOptions
      .getSelectionModel()
      .select(0); else lvOptions.getSelectionModel().select(lastIndex);
  }

  private void refreshSuboptions() {
    lvSuboptions.getItems().clear();

    // retrieve updated options
    String optionsJson = HttpUtils.get("/api/v1/session/" + session.getId() + "/option");
    options = JsonUtils.parseJsonArray(optionsJson, Option.class);
    if (options.isEmpty()) return;

    // filter out suboptions of the selected superoption
    Option selectedOption = lvOptions.getSelectionModel().getSelectedItem();
    List<Option> suboptions = options
      .stream()
      .filter(opt -> opt.getParentOptionId().intValue() == selectedOption.getId())
      .toList();

    lvSuboptions.getItems().addAll(suboptions);
  }
}
