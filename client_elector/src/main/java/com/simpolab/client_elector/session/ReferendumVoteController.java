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
import javafx.scene.control.CheckBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class ReferendumVoteController implements Initializable {
  private static Session session;

  @FXML
  private Text lblSessionName;
  @FXML
  private Text lblPrompt;
  @FXML
  private CheckBox cbAgree;
  @FXML
  private CheckBox cbNotAgree;

  public static void init(Session initSession){
    session = initSession;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    String promptJson = HttpUtils.get("/api/v1/session/"+session.getId()+"/option");
    Option prompt = JsonUtils.parseJsonArray(promptJson, Option.class).get(0);

    lblSessionName.setText(session.getName());
    lblPrompt.setText(prompt.getValue());
  }

  @FXML
  private void onBtnBackClicked(ActionEvent actionEvent) throws Exception {
    SessionController.init(session);
    SceneUtils.switchTo("session/session.fxml");
  }

  @FXML
  private void onCbAgreeClicked(ActionEvent event){
    cbAgree.setSelected(true);
    cbNotAgree.setSelected(false);
  }

  @FXML
  private void onCbNotAgreeClicked(ActionEvent event){
    cbAgree.setSelected(false);
    cbNotAgree.setSelected(true);
  }

  @FXML
  private void onBtnVoteClicked(ActionEvent event) throws Exception{
    if(!(cbNotAgree.isSelected() ^ cbAgree.isSelected())){
      AlertUtils.alert(Alert.AlertType.ERROR, "Select one option");
      return;
    }

    if(cbAgree.isSelected()){
      //vote
    } else {
      //vote
    }

    SceneUtils.switchToHomepage();
  }
}
