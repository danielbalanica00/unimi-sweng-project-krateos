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
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CategoricVoteController implements Initializable {
  private static Session session;

  @FXML
  private Text lblSessionName;
  @FXML
  private ListView<Option> lvOptions;

  @FXML
  private void onBtnBackClicked(ActionEvent event) throws Exception{
    SceneUtils.switchTo("session/session.fxml");
  }

  @FXML
  private void onBtnVoteClicked(ActionEvent event) throws Exception{
    // ensure option selection
    if(lvOptions.getSelectionModel().getSelectedIndex() < 0){
      AlertUtils.alert(Alert.AlertType.ERROR, "Select an option");
      return;
    }

    Option selctedOption = lvOptions.getSelectionModel().getSelectedItem();

    //build vote

    SceneUtils.switchToHomepage();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    lblSessionName.setText(session.getName());

    lvOptions.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    String optionsJson = HttpUtils.get("/api/v1/session/"+session.getId()+"/option");
    List<Option> options = JsonUtils.parseJsonArray(optionsJson, Option.class);

    if(options.isEmpty()) return;

    lvOptions.getItems().addAll(options);
  }

  public static void init(Session initSession){
    session = initSession;
  }
}
