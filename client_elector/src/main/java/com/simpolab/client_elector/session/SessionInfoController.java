package com.simpolab.client_elector.session;

import com.simpolab.client_elector.domain.Session;
import com.simpolab.client_elector.utils.SceneUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class SessionInfoController implements Initializable {
    private static Session session;

    @FXML
    private Text lblSessionName;
    @FXML
    private Text lblEndsOn;
    @FXML
    private Text lblState;

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
    }


}
