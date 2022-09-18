package com.simpolab.client_elector.session;

import com.simpolab.client_elector.domain.Option;
import com.simpolab.client_elector.domain.Session;
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

    // check for errors
    if (session.getState().equals(Session.State.ENDED)) {
      // retrieve options
      String optionsJson = HttpUtils.get("/api/v1/session/" + session.getId() + "/option");
      List<Option> options = JsonUtils.parseJsonArray(optionsJson, Option.class);

      // set winner text
      String winnerJson = HttpUtils.get("/api/v1/session/" + session.getId() + "/result/winner");
      List<Integer> winnerId = JsonUtils.parseJsonArray(winnerJson, Integer.class);
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
