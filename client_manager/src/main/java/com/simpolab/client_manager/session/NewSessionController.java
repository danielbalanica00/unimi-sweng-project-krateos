package com.simpolab.client_manager.session;

import com.simpolab.client_manager.domain.Session;
import com.simpolab.client_manager.session.session_types.AddCategoricAndOrdinalController;
import com.simpolab.client_manager.session.session_types.AddCategoricPreferencesController;
import com.simpolab.client_manager.session.session_types.AddReferendumController;
import com.simpolab.client_manager.utils.AlertUtils;
import com.simpolab.client_manager.utils.HttpUtils;
import com.simpolab.client_manager.utils.JsonUtils;
import com.simpolab.client_manager.utils.SceneUtils;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

public class NewSessionController implements Initializable {

  @FXML
  private TextField txtName;

  @FXML
  private DatePicker dateEndsOn;

  @FXML
  private ChoiceBox<Session.Type> choiceSessionType;

  @FXML
  private CheckBox cbQuorum;

  @FXML
  private CheckBox cbAbsoluteMajority;

  @FXML
  private Button btnNext;

  private static Long sessionId;

  @FXML
  private void onBtnBackClicked(ActionEvent event) throws Exception {
    SceneUtils.switchToHomepage();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    choiceSessionType.getItems().add(Session.Type.ORDINAL);
    choiceSessionType.getItems().add(Session.Type.CATEGORIC);
    choiceSessionType.getItems().add(Session.Type.CATEGORIC_WITH_PREFERENCES);
    choiceSessionType.getItems().add(Session.Type.REFERENDUM);

    if (sessionId == null) return;

    // get previously created session to prefill the fields
    String sessionJson = HttpUtils.get("/api/v1/session/" + sessionId);
    var session = JsonUtils.parseJson(sessionJson, Session.class);
    fillSession(session);

    // delete the session
    HttpUtils.delete("/api/v1/session/" + sessionId);
    resetState();
  }

  public static void init(long sessionId) {
    if (NewSessionController.sessionId != null) throw new IllegalStateException(
      "Trying to init an already initialized controller"
    );

    NewSessionController.sessionId = sessionId;
  }

  private static void resetState() {
    sessionId = null;
  }

  private void fillSession(Session session) {
    txtName.setText(session.getName());
    dateEndsOn.setValue(
      LocalDate.ofInstant(
        Instant.ofEpochSecond(session.getEndsOn()),
        OffsetDateTime.now().getOffset()
      )
    );
    cbQuorum.setSelected(session.isHasQuorum());
    cbAbsoluteMajority.setSelected(session.isNeedAbsoluteMajority());
    choiceSessionType.setValue(session.getType());
  }

  @FXML
  private void onBtnNextClicked(ActionEvent event) throws Exception {
    // check name
    String name = txtName.getText();
    if (name.isBlank()) {
      AlertUtils.alert(Alert.AlertType.ERROR, "The session name cannot be blank");
      return;
    }

    // check type was selected
    Session.Type type = choiceSessionType.getValue();
    if (type == null) {
      AlertUtils.alert(Alert.AlertType.ERROR, "You have to select a session type");
      return;
    }

    // check selected time is in the future
    long epochSeconds = dateEndsOn
      .getValue()
      .toEpochSecond(LocalTime.now(), OffsetDateTime.now().getOffset());
    if (epochSeconds < Instant.now().getEpochSecond()) {
      AlertUtils.alert(Alert.AlertType.ERROR, "The session can not end in the past");
      return;
    }

    boolean hasQuorum = cbQuorum.isSelected();
    boolean hasAbsoluteMajority = cbAbsoluteMajority.isSelected();

    final var sessionToCreate = Session
      .builder()
      .name(name)
      .endsOn(epochSeconds)
      .needAbsoluteMajority(hasAbsoluteMajority)
      .hasQuorum(hasQuorum)
      .type(type)
      .build();

    String sessionJson = HttpUtils.postJson("/api/v1/session", sessionToCreate);
    long sessionId = JsonUtils.parseJson(sessionJson, Session.class).getId();

    switch (type) {
      case CATEGORIC, ORDINAL -> {
        AddCategoricAndOrdinalController.init(sessionId);
        SceneUtils.switchTo("session/add_categoric.fxml");
      }
      case CATEGORIC_WITH_PREFERENCES -> {
        AddCategoricPreferencesController.init(sessionId);
        SceneUtils.switchTo("session/add_categoric_preferences.fxml");
      }
      case REFERENDUM -> {
        AddReferendumController.init(sessionId);
        SceneUtils.switchTo("session/add_referendum.fxml");
      }
    }
  }
}
