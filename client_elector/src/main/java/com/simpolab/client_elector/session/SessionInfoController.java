package com.simpolab.client_elector.session;

import com.simpolab.client_elector.domain.Option;
import com.simpolab.client_elector.domain.Session;
import com.simpolab.client_elector.utils.ErrorBody;
import com.simpolab.client_elector.utils.HttpUtils;
import com.simpolab.client_elector.utils.JsonUtils;
import com.simpolab.client_elector.utils.SceneUtils;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class SessionInfoController implements Initializable {

  private static Session session;

  @FXML
  private Text lblSessionName;

  @FXML
  private Text lblEndsOn;

  @FXML
  private Text lblState;

  @FXML
  private Text lblWinner;

  @FXML
  private VBox vboxContainer;

  public static void init(Session initSession) {
    session = initSession;
  }

  @FXML
  private void onBtnBackClicked(ActionEvent event) throws Exception {
    SceneUtils.switchTo("session/sessions_history.fxml");
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    lblSessionName.setText(session.getName());
    lblEndsOn.setText("Ends on: " + new Date(session.getEndsOn() * 1000).toString());
    lblState.setText(session.getState().toString());
    if (
      session.getState().equals(Session.State.ACTIVE) ||
      session.getState().equals(Session.State.INACTIVE)
    ) {
      vboxContainer.getChildren().remove(lblWinner);
    }

    String optionsJson = HttpUtils.get("/api/v1/session/" + session.getId() + "/option");
    List<Option> options = JsonUtils.parseJsonArray(optionsJson, Option.class);

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
        default -> lblWinner.setText("wtf");
      }
      return;
    }

    // check for errors
    if (session.getState().equals(Session.State.ENDED)) {
      // set winner text
      List<Integer> winnerId = JsonUtils.parseJsonArray(winnerResponse.body(), Integer.class);
      Option winnerOption = options
        .stream()
        .filter(opt -> opt.getId().equals(winnerId.get(0)))
        .findFirst()
        .get();

      String winMsg = "Winner: " + winnerOption.getValue();

      if (winnerId.size() == 2) {
        // retrieve winner candidate
        Option winnerSuboption = options
          .stream()
          .filter(opt -> opt.getId().equals(winnerId.get(1)))
          .findFirst()
          .get();
        // extend win message
        winMsg += "\nCandidate: " + winnerSuboption.getValue();
      }

      lblWinner.setText(winMsg);
    }
  }
}
