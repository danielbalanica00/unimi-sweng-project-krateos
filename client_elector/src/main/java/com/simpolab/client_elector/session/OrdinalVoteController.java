package com.simpolab.client_elector.session;

import com.simpolab.client_elector.domain.Option;
import com.simpolab.client_elector.domain.Session;
import com.simpolab.client_elector.utils.HttpUtils;
import com.simpolab.client_elector.utils.JsonUtils;
import com.simpolab.client_elector.utils.SceneUtils;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class OrdinalVoteController implements Initializable {
  private static Session session;

  @FXML
  private Text lblSessionName;
  @FXML
  private ListView<Option> lvOptions;

  @FXML
  private void onBtnBackClicked(ActionEvent event) throws Exception{
    SessionController.init(session);
    SceneUtils.switchTo("session/session.fxml");
  }

  @FXML
  private void onBtnMoveUpClicked(ActionEvent event){
    if(lvOptions.getSelectionModel().getSelectedIndex() <= 0) return;
    int selectedIndex = lvOptions.getSelectionModel().getSelectedIndex();
    List<Option> options = new ArrayList<>(lvOptions.getItems().stream().toList());

    Option temp = options.get(selectedIndex - 1);
    options.set(selectedIndex-1, options.get(selectedIndex));
    options.set(selectedIndex, temp);

    lvOptions.setItems(new ObservableListWrapper<>(options));
  }

  @FXML
  private void onBtnMoveDownClicked(ActionEvent event){
    if(lvOptions.getSelectionModel().getSelectedIndex() >= lvOptions.getItems().stream().count()-1) return;
    int selectedIndex = lvOptions.getSelectionModel().getSelectedIndex();
    List<Option> options = new ArrayList<>(lvOptions.getItems().stream().toList());

    Option temp = options.get(selectedIndex + 1);
    options.set(selectedIndex+1, options.get(selectedIndex));
    options.set(selectedIndex, temp);

    lvOptions.setItems(new ObservableListWrapper<>(options));
  }

  @FXML
  private void onBtnVoteClicked(ActionEvent event) throws Exception{
    List<Option> options = lvOptions.getItems().stream().toList();
    // vote
    SceneUtils.switchToHomepage();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    lblSessionName.setText(session.getName());

    String optionsJson = HttpUtils.get("/api/v1/session/"+session.getId()+"/option");
    List<Option> options = JsonUtils.parseJsonArray(optionsJson, Option.class);

    if(options.isEmpty()) return;

    lvOptions.getItems().addAll(options);
    lvOptions.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    lvOptions.getSelectionModel().select(0);
  }

  public static void init(Session initSession){
    session = initSession;
  }
}
