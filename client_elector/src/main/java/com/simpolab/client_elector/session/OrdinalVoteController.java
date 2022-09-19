package com.simpolab.client_elector.session;

import com.google.gson.Gson;
import com.simpolab.client_elector.domain.Option;
import com.simpolab.client_elector.domain.Session;
import com.simpolab.client_elector.domain.Vote;
import com.simpolab.client_elector.utils.HttpUtils;
import com.simpolab.client_elector.utils.JsonUtils;
import com.simpolab.client_elector.utils.SceneUtils;
import com.sun.javafx.collections.ObservableListWrapper;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

public class OrdinalVoteController implements Initializable {

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
  private void onBtnMoveUpClicked(ActionEvent event) {
    // ensure the selection is not the top one
    if (lvOptions.getSelectionModel().getSelectedIndex() <= 0) return;
    int selectedIndex = lvOptions.getSelectionModel().getSelectedIndex();
    List<Option> options = new ArrayList<>(lvOptions.getItems().stream().toList());

    // swap selection with the one at the previous index
    Option temp = options.get(selectedIndex - 1);
    options.set(selectedIndex - 1, options.get(selectedIndex));
    options.set(selectedIndex, temp);

    lvOptions.setItems(new ObservableListWrapper<>(options));
  }

  @FXML
  private void onBtnMoveDownClicked(ActionEvent event) {
    // ensure the selection is not the last one
    if (
      lvOptions.getSelectionModel().getSelectedIndex() >= lvOptions.getItems().stream().count() - 1
    ) return;
    int selectedIndex = lvOptions.getSelectionModel().getSelectedIndex();
    List<Option> options = new ArrayList<>(lvOptions.getItems().stream().toList());

    // swap selection with the one at the next index
    Option temp = options.get(selectedIndex + 1);
    options.set(selectedIndex + 1, options.get(selectedIndex));
    options.set(selectedIndex, temp);

    lvOptions.setItems(new ObservableListWrapper<>(options));
  }

  @FXML
  private void onBtnVoteClicked(ActionEvent event) throws Exception {
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

    List<Option> options = lvOptions.getItems().stream().toList();

    List<Vote> votes = new ArrayList<>();
    for (int i = 0; i < options.size(); i++) {
      votes.add(new Vote(options.get(i).getId(), i + 1));
    }
    System.out.println(new Gson().toJson(votes));

    HttpUtils.postJson("/api/v1/session/" + session.getId() + "/vote", votes);
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
    lvOptions.getSelectionModel().select(0);
  }

  public static void init(Session initSession) {
    session = initSession;
  }
}
