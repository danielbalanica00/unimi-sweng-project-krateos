package com.simpolab.client_elector.session;

import com.simpolab.client_elector.domain.Option;
import com.simpolab.client_elector.domain.Session;
import com.simpolab.client_elector.domain.Vote;
import com.simpolab.client_elector.utils.AlertUtils;
import com.simpolab.client_elector.utils.HttpUtils;
import com.simpolab.client_elector.utils.JsonUtils;
import com.simpolab.client_elector.utils.SceneUtils;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.text.Text;
import org.apache.hc.client5.http.classic.methods.HttpPost;

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

  public static void init(Session initSession) {
    session = initSession;
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    String promptJson = HttpUtils.get("/api/v1/session/" + session.getId() + "/option");
    Option prompt = JsonUtils.parseJsonArray(promptJson, Option.class).get(0);

    lblSessionName.setText(session.getName());
    lblPrompt.setText(prompt.getValue());
  }

  @FXML
  private void onBtnBackClicked(ActionEvent actionEvent) throws Exception {
    SceneUtils.switchTo("session/session.fxml");
  }

  @FXML
  private void onCbAgreeClicked(ActionEvent event) {
    cbAgree.setSelected(true);
    cbNotAgree.setSelected(false);
  }

  @FXML
  private void onCbNotAgreeClicked(ActionEvent event) {
    cbAgree.setSelected(false);
    cbNotAgree.setSelected(true);
  }

  @FXML
  private void onBtnVoteClicked(ActionEvent event) throws Exception {
    if (!(cbNotAgree.isSelected() ^ cbAgree.isSelected())) {
      AlertUtils.alert(Alert.AlertType.ERROR, "Select one option");
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

    //retrieve yes/no options
    String optJson = HttpUtils.get("/api/v1/session/" + session.getId() + "/option");
    List<Option> options = JsonUtils
      .parseJsonArray(optJson, Option.class)
      .stream()
      .filter(opt -> opt.getParentOptionId() != null)
      .toList();

    if (cbAgree.isSelected()) {
      HttpUtils.postJson(
        "/api/v1/session/" + session.getId() + "/vote",
        List.of(
          new Vote(
            options
              .stream()
              .filter(opt -> opt.getValue().toLowerCase().equals("yes"))
              .findFirst()
              .get()
              .getId()
          )
        )
      );
    } else {
      HttpUtils.postJson(
        "/api/v1/session/" + session.getId() + "/vote",
        List.of(
          new Vote(
            options
              .stream()
              .filter(opt -> opt.getValue().toLowerCase().equals("no"))
              .findFirst()
              .get()
              .getId()
          )
        )
      );
    }

    SceneUtils.switchToHomepage();
  }
}
