package com.simpolab.client_manager.session;

import com.simpolab.client_manager.utils.SceneUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AddCategoricPreferencesController implements Initializable {

  private static Session session;
  private List<Option> options;

  @FXML
  private TextField txtPrompt;
  @FXML
  private ListView<Option> lvOptions;
  @FXML
  private ListView<Option> lvSuboption;

  public static void initSession(Session initSession){
    session = initSession;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    lvOptions.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    lvSuboption.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
  }

  @FXML
  private void onBtnAddOptionClicked(ActionEvent event) throws Exception{
    Option newOption = new Option(txtPrompt.getText());
    options.add(newOption);
    lvOptions.getItems().add(newOption);
  }

  @FXML
  private void onBtnAddSubobtionClicked(ActionEvent event) throws Exception{
    Option suboption = new Option(txtPrompt.getText());
    Option option = lvOptions.getSelectionModel().getSelectedItem();
  }

  @FXML
  private void onBtnBackClicked(ActionEvent event) throws Exception{
    SceneUtils.switchTo("session/new_session.fxml");
  }

}
