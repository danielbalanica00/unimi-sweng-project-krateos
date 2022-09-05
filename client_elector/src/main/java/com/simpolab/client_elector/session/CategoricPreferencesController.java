package com.simpolab.client_elector.session;

import com.simpolab.client_elector.domain.Option;
import com.simpolab.client_elector.domain.Session;
import com.simpolab.client_elector.utils.AlertUtils;
import com.simpolab.client_elector.utils.HttpUtils;
import com.simpolab.client_elector.utils.JsonUtils;
import com.simpolab.client_elector.utils.SceneUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CategoricPreferencesController implements Initializable {
  private static Session session;
  private static List<Option> options;
  @FXML
  private Text lblSessionName;
  @FXML
  private ListView<Option> lvOptions;

  @FXML
  private ListView<Option> lvSuboptions;

  public static void init(Session initSession){
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
  private void onBtnVoteClicked(ActionEvent event) throws Exception{
    if(lvOptions.getSelectionModel().getSelectedIndex() < 0){
      AlertUtils.alert(Alert.AlertType.ERROR, "Select an option and one or more suboptions");
      return;
    }

    if(lvSuboptions.getSelectionModel().getSelectedIndex() < 0){
      AlertUtils.alert(Alert.AlertType.ERROR, "Select at least one suboption");
      return;
    }

    Option option = lvOptions.getSelectionModel().getSelectedItem();
    List<Option> suboptions = lvSuboptions.getSelectionModel().getSelectedItems();

    //build vote

    SceneUtils.switchToHomepage();
  }

  @FXML
  private void onBtnBackClicked(ActionEvent event) throws Exception{
    SessionController.init(session);
    SceneUtils.switchTo("session/session.fxml");
  }

  private void refreshOptions() {
    int lastIndex = lvOptions.getSelectionModel().getSelectedIndex();
    lvOptions.getItems().clear();

    String optionsJson = HttpUtils.get("/api/v1/session/" + session.getId() + "/option");
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

    String optionsJson = HttpUtils.get("/api/v1/session/" + session.getId() + "/option");
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
