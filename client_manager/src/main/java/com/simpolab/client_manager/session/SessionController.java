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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class SessionController implements Initializable {

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
  private Button btnDelete;

  @FXML
  private BarChart<Option, Integer> barChartVotes;

  @FXML
  private Text lblWinner;

  @FXML
  private VBox vboxContainer;

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
      session.getState().equals(Session.State.ACTIVE) ||
      session.getState().equals(Session.State.INACTIVE) ||
      session.getState().equals(Session.State.CANCELLED)
    ) {
      vboxContainer.getChildren().remove(barChartVotes);
      vboxContainer.getChildren().remove(lblWinner);
      return;
    }

    // retrieve options
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
    lblWinner.setText("Winner: " + winnerOption.getValue());

    // retrieve votes
    String votesJson = HttpUtils.get("/api/v1/session/" + session.getId() + "/result/option");
    Map map = new Gson().fromJson(votesJson, Map.class);

    XYChart.Series dataSeries = new XYChart.Series();
    dataSeries.setName("Options results");

    for (var key : map.keySet()) dataSeries
      .getData()
      .add(
        new XYChart.Data(
          options
            .stream()
            .filter(opt -> opt.getId().equals(Integer.parseInt((String) key)))
            .findFirst()
            .get()
            .getValue(),
          map.get(key)
        )
      );

    barChartVotes.getData().add(dataSeries);
  }

  private void refreshSession() throws Exception {
    String newSessionJson = HttpUtils.get("/api/v1/session/" + session.getId());
    Session newSession = JsonUtils.parseJson(newSessionJson, Session.class);

    init(newSession);
    SceneUtils.switchTo("session/session.fxml");
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
}
