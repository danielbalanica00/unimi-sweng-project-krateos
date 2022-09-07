package com.simpolab.client_elector.session;

import com.simpolab.client_elector.domain.Session;
import com.simpolab.client_elector.utils.SceneUtils;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class SessionController implements Initializable {

  private static Session session;

  @FXML
  private Text lblSessionName;

  @FXML
  private Text lblRemainingTime;

  @FXML
  private Text lblDescription;

  @FXML
  private Button btnVote;

  public static void init(Session initSession) {
    session = initSession;
  }

  @FXML
  private void onBtnVoteClicked(ActionEvent event) throws Exception {
    // TODO: 9/4/2022 check if the session ended by the time that they opened the view

    switch (session.getType()) {
      case CATEGORIC -> {
        CategoricVoteController.init(session);
        SceneUtils.switchTo("session/categoric_vote.fxml");
      }
      case ORDINAL -> {
        OrdinalVoteController.init(session);
        SceneUtils.switchTo("session/ordinal_vote.fxml");
      }
      case CATEGORIC_WITH_PREFERENCES -> {
        CategoricPreferencesVoteController.init(session);
        SceneUtils.switchTo("session/categoric_preferences_vote.fxml");
      }
      case REFERENDUM -> {
        ReferendumVoteController.init(session);
        SceneUtils.switchTo("session/referendum_vote.fxml");
      }
    }
  }

  @FXML
  private void onBtnBackClicked(ActionEvent event) throws Exception {
    SceneUtils.switchToHomepage();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    lblSessionName.setText(session.getName());
    lblRemainingTime.setText("Ends on: " + new Date(session.getEndsOn() * 1000));
  }
}
