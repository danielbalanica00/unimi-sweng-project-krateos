package com.simpolab.client_manager.session;

import java.awt.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.chrono.Chronology;
import java.util.ResourceBundle;

import com.simpolab.client_manager.utils.AlertUtils;
import com.simpolab.client_manager.utils.SceneUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class NewSessionController implements Initializable {

  private Stage stage;
  private Scene scene;

  @FXML
  private TextField txtName;

  @FXML
  private DatePicker dateEndsOn;

  @FXML
  private ChoiceBox<Session.SessionType> choiceSessionType;

  @FXML
  private CheckBox cbQuorum;

  @FXML
  private CheckBox cbAbsoluteMajority;

  @FXML
  private Button btnNext;

  @FXML
  private void onBtnBackClicked(ActionEvent event) throws Exception {
    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    SceneUtils.switchTo("../homepage/homepage.fxml", stage);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    choiceSessionType.getItems().add(Session.SessionType.ORDINAL);
    choiceSessionType.getItems().add(Session.SessionType.CATEGORIC);
    choiceSessionType.getItems().add(Session.SessionType.CATEGORIC_WITH_PREFERENCES);
    choiceSessionType.getItems().add(Session.SessionType.REFERENDUM);


  }

  @FXML
  private void onBtnNextClicked(ActionEvent event) throws Exception{
    String name = txtName.getText();
    Long epochDay = dateEndsOn.getValue().toEpochDay();
    boolean hasQuorum = cbQuorum.isSelected();
    boolean hasAbsoluteMajority = cbAbsoluteMajority.isSelected();
    Session.SessionType type = choiceSessionType.getValue();

    if(name.isBlank() || name == null){
      AlertUtils.alert(Alert.AlertType.ERROR, "The session name cannot be blank");
      return;
    }
    if(epochDay < LocalDate.now().toEpochDay()){
      AlertUtils.alert(Alert.AlertType.ERROR, "The session can not end in the past");
      return;
    }
    if(type == null || type.toString().isBlank()){
      AlertUtils.alert(Alert.AlertType.ERROR, "You have to select a session type");
      return;
    }

    Session session = new Session(name, epochDay.toString(), hasAbsoluteMajority, hasQuorum, type.toString());

    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    switch (type){
      case CATEGORIC:
        SceneUtils.switchTo("../session/add_categoric.fxml", stage);
        break;
      case CATEGORIC_WITH_PREFERENCES:
        SceneUtils.switchTo("../session/add_categoric_preferences.fxml", stage);
        break;
      case ORDINAL:
        SceneUtils.switchTo("../session/add_ordinal.fxml", stage);
        break;
      case REFERENDUM:
        SceneUtils.switchTo("../session/add_referendum.fxml", stage);
        break;
    }
  }
}
