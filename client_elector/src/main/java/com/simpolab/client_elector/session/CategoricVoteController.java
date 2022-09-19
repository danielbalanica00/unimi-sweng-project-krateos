package com.simpolab.client_elector.session;

import com.google.gson.Gson;
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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;

public class CategoricVoteController implements Initializable {

  private static Session session;

  @FXML
  private Text lblSessionName;

  @FXML
  private ListView<Option> lvOptions;

  @FXML
  private void onBtnBackClicked(ActionEvent event) throws Exception {
    SceneUtils.switchTo("session/session.fxml");
  }

  @FXML
  private void onBtnVoteClicked(ActionEvent event) throws Exception {
    // ensure option selection
    if (lvOptions.getSelectionModel().getSelectedIndex() < 0) {
      AlertUtils.alert(Alert.AlertType.ERROR, "Select an option");
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

    Option selectedOption = lvOptions.getSelectionModel().getSelectedItem();

    Vote vote = new Vote(selectedOption.getId());
    HttpUtils.postJson("/api/v1/session/" + session.getId() + "/vote", List.of(vote));
    SceneUtils.switchToHomepage();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    lblSessionName.setText(session.getName());

    lvOptions.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    String optionsJson = HttpUtils.get("/api/v1/session/" + session.getId() + "/option");
    List<Option> options = JsonUtils.parseJsonArray(optionsJson, Option.class);

    if (options.isEmpty()) return;

    lvOptions.getItems().addAll(options);
  }

  public static void init(Session initSession) {
    session = initSession;
  }
}
