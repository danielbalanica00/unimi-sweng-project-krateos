package com.simpolab.client_manager.session;

import com.simpolab.client_manager.login.AuthHandler;
import com.simpolab.client_manager.utils.AlertUtils;
import com.simpolab.client_manager.utils.HttpUtils;
import com.simpolab.client_manager.utils.JsonUtils;
import com.simpolab.client_manager.utils.SceneUtils;
//import java.awt.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class NewSessionController implements Initializable {
  private Stage stage;
  private Scene scene;

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

  @FXML
  private void onBtnBackClicked(ActionEvent event) throws Exception {
    SceneUtils.switchToHomepage();
    // TODO: 9/3/2022 delete session 
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    choiceSessionType.getItems().add(Session.Type.ORDINAL);
    choiceSessionType.getItems().add(Session.Type.CATEGORIC);
    choiceSessionType.getItems().add(Session.Type.CATEGORIC_WITH_PREFERENCES);
    choiceSessionType.getItems().add(Session.Type.REFERENDUM);
  }

  @FXML
  private void onBtnNextClicked(ActionEvent event) throws Exception {
    String name = txtName.getText();

    Long epochDay = dateEndsOn.getValue().toEpochSecond(LocalTime.now(), OffsetDateTime.now().getOffset());
    boolean hasQuorum = cbQuorum.isSelected();
    boolean hasAbsoluteMajority = cbAbsoluteMajority.isSelected();
    Session.Type type = choiceSessionType.getValue();

    if (name.isBlank() || name == null) {
      AlertUtils.alert(Alert.AlertType.ERROR, "The session name cannot be blank");
      return;
    }
    if (epochDay < LocalDate.now().toEpochDay()) {
      AlertUtils.alert(Alert.AlertType.ERROR, "The session can not end in the past");
      return;
    }
    if (type == null || type.toString().isBlank()) {
      AlertUtils.alert(Alert.AlertType.ERROR, "You have to select a session type");
      return;
    }

    Session session = new Session(name, epochDay, hasAbsoluteMajority, hasQuorum, type.toString());
    String sessionJson = HttpUtils.postJson("/api/v1/session", Map.of("Authorization", "Bearer " + AuthHandler.getAccessToken()), session);
    int sessionId = JsonUtils.parseJson(sessionJson, Session.class).getId();

    switch (type) {
      case CATEGORIC:
        AddCategoricController.initSession(sessionId);
        SceneUtils.switchTo("session/add_categoric.fxml");
        break;
      case CATEGORIC_WITH_PREFERENCES:
        SceneUtils.switchTo("session/add_categoric_preferences.fxml");
        break;
      case ORDINAL:
        SceneUtils.switchTo("session/add_ordinal.fxml");
        break;
      case REFERENDUM:
        SceneUtils.switchTo("session/add_referendum.fxml");
        break;
    }
  }
}
