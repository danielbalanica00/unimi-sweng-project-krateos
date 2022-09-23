package com.simpolab.client_manager.session;

import com.google.gson.Gson;
import com.simpolab.client_manager.domain.Option;
import com.simpolab.client_manager.domain.Session;
import com.simpolab.client_manager.utils.*;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class SessionCategoricPreferenceController implements Initializable {

  private static Session session;
  private static List<Option> options;

  @FXML
  private Text lblSessionName;

  @FXML
  private Text lblEndsOn;

  @FXML
  private Text lblState;

  @FXML
  private Button btnStop;

  @FXML
  private Button btnAbort;

  @FXML
  private Button btnStart;

  @FXML
  private TableView<Option> tableOptions;

  @FXML
  private TableView<Option> tableSuboptions;

  @FXML
  private TableColumn<Option, String> columnOption;

  @FXML
  private TableColumn<Option, Integer> columnOptionVotes;

  @FXML
  private TableColumn<Option, String> columnSuboption;

  @FXML
  private TableColumn<Option, Integer> columnSuboptionVotes;

  @FXML
  private VBox vboxResults;

  @FXML
  private Text lblWinner;

  @FXML
  private HBox hboxVotes;

  @FXML
  private VBox vboxContainer;

  @FXML
  private Button btnDelete;

  public static void init(Session initSession) {
    session = initSession;
  }

  @FXML
  private void onBtnStopClicked(ActionEvent event) throws Exception {
    HttpUtils.patch(
      "/api/v1/session/" +
      session.getId() +
      "/state/" +
      Session.State.ENDED.toString().toLowerCase()
    );
    refreshSession();
  }

  @FXML
  private void onBtnAbortClicked(ActionEvent event) throws Exception {
    HttpUtils.patch(
      "/api/v1/session/" +
      session.getId() +
      "/state/" +
      Session.State.CANCELLED.toString().toLowerCase()
    );
    refreshSession();
  }

  @FXML
  private void onBtnStartClicked(ActionEvent event) throws Exception {
    HttpUtils.patch(
      "/api/v1/session/" +
      session.getId() +
      "/state/" +
      Session.State.ACTIVE.toString().toLowerCase()
    );
    refreshSession();
  }

  @FXML
  private void onBtnDeleteClicked(ActionEvent event) throws Exception {
    if (!AlertUtils.confirmAlert("Do you want to delete this session?")) return;
    HttpUtils.delete("/api/v1/session/" + session.getId());
    SceneUtils.switchTo("session/sessions.fxml");
  }

  @FXML
  private void onBtnBackClicked(ActionEvent event) throws Exception {
    session = null;
    SceneUtils.switchTo("session/sessions.fxml");
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    refreshSessionInfo();

    // do not look for results until the session has ended
    if (
      session.getState().equals(Session.State.CANCELLED) ||
      session.getState().equals(Session.State.ACTIVE) ||
      session.getState().equals(Session.State.INACTIVE)
    ) {
      vboxResults.getChildren().remove(hboxVotes);
      vboxResults.getChildren().remove(lblWinner);
      return;
    }

    columnOption.setCellValueFactory(new PropertyValueFactory<>("value"));
    columnOptionVotes.setCellValueFactory(new PropertyValueFactory<>("votes"));
    columnSuboption.setCellValueFactory(new PropertyValueFactory<>("value"));
    columnSuboptionVotes.setCellValueFactory(new PropertyValueFactory<>("votes"));

    refreshOptions();
    refreshSuboptions();

    String optionsJson = HttpUtils.get("/api/v1/session/" + session.getId() + "/option");
    options = JsonUtils.parseJsonArray(optionsJson, Option.class);

    // set winner text
    var winnerResponse = HttpUtils.getWithCode(
      "/api/v1/session/" + session.getId() + "/result/winner",
      null
    );

    if (winnerResponse.code() == 403) {
      ErrorBody errorBody = JsonUtils.parseJson(winnerResponse.body(), ErrorBody.class);

      switch (errorBody.getCode()) {
        case 0 -> lblWinner.setText("Ballot");
        case 1 -> {
          Option winOption = options
            .stream()
            .filter(opt -> opt.getId().equals(errorBody.getWinningTopOption()))
            .findFirst()
            .get();
          lblWinner.setText("Candidate ballot, winning option: " + winOption.getValue());
        }
        case 2 -> lblWinner.setText("Quorum has not been reached");
        case 3 -> lblWinner.setText("Absolute majority has not been reached");
        default -> lblWinner.setText("Unknown message");
      }
      return;
    }

    List<Integer> winnerId = JsonUtils.parseJsonArray(winnerResponse.body(), Integer.class);
    Option winnerOption = options
      .stream()
      .filter(opt -> opt.getId().equals(winnerId.get(0)))
      .findFirst()
      .get();
    Option winnerSuboption = options
      .stream()
      .filter(opt -> opt.getId().equals(winnerId.get(1)))
      .findFirst()
      .get();
    lblWinner.setText(
      "Winner: " + winnerOption.getValue() + "\nCandidate: " + winnerSuboption.getValue()
    );
  }

  private void refreshSession() throws Exception {
    String newSessionJson = HttpUtils.get("/api/v1/session/" + session.getId());
    Session newSession = JsonUtils.parseJson(newSessionJson, Session.class);

    init(newSession);
    SceneUtils.switchTo("session/session_categoric_preference.fxml");
    refreshSessionInfo();
  }

  private void refreshSessionInfo() {
    lblSessionName.setText(session.getName());
    lblEndsOn.setText("Ends on: " + new Date(session.getEndsOn() * 1000).toString());
    lblState.setText(session.getState().toString());

    if (!session.getState().equals(Session.State.INACTIVE)) {
      btnStart.setDisable(true);
      vboxContainer.getChildren().remove(btnDelete);
    }

    if (!session.getState().equals(Session.State.ACTIVE)) {
      btnStop.setDisable(true);
    }

    if (session.getState().equals(Session.State.INVALID)) {
      btnAbort.setDisable(true);
    }
  }

  private void refreshOptions() {
    // save last selection before clearing
    int lastIndex = tableOptions.getSelectionModel().getSelectedIndex();
    tableOptions.getItems().clear();

    // retrieve updated options
    String optionsJson = HttpUtils.get("/api/v1/session/" + session.getId() + "/option");
    options = JsonUtils.parseJsonArray(optionsJson, Option.class);
    if (options.isEmpty()) return;

    // retrieve votes per option
    String votesJson = HttpUtils.get("/api/v1/session/" + session.getId() + "/result/option");
    Map votes = new Gson().fromJson(votesJson, Map.class);

    for (var key : votes.keySet()) {
      options
        .stream()
        .filter(opt -> opt.getId().equals(Integer.parseInt((String) key)))
        .findFirst()
        .get()
        .setVotes(((Double) votes.get(key)).intValue());
    }

    // filter out only superoptions
    List<Option> superoptions = options
      .stream()
      .filter(opt -> opt.getParentOptionId() < 0)
      .toList();
    tableOptions.getItems().addAll(superoptions);

    // restore selection or force first position
    if (tableOptions.getSelectionModel().getSelectedIndex() < 0) tableOptions
      .getSelectionModel()
      .select(0); else tableOptions.getSelectionModel().select(lastIndex);
  }

  private void refreshSuboptions() {
    tableSuboptions.getItems().clear();

    // retrieve updated options
    String optionsJson = HttpUtils.get("/api/v1/session/" + session.getId() + "/option");
    options = JsonUtils.parseJsonArray(optionsJson, Option.class);
    if (options.isEmpty()) return;

    // retrieve votes per option
    String votesJson = HttpUtils.get("/api/v1/session/" + session.getId() + "/result/option");
    Map votes = new Gson().fromJson(votesJson, Map.class);
    for (var key : votes.keySet()) {
      options
        .stream()
        .filter(opt -> opt.getId().equals(Integer.parseInt((String) key)))
        .findFirst()
        .get()
        .setVotes(((Double) votes.get(key)).intValue());
    }

    // filter out suboptions of the selected superoption
    Option selectedOption = tableOptions.getSelectionModel().getSelectedItem();
    List<Option> suboptions = options
      .stream()
      .filter(opt -> opt.getParentOptionId().intValue() == selectedOption.getId())
      .toList();

    tableSuboptions.getItems().addAll(suboptions);
  }

  public void onTableOptionsClicked(MouseEvent mouseEvent) {
    refreshSuboptions();
  }
}
