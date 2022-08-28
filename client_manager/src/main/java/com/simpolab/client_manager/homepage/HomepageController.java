package com.simpolab.client_manager.homepage;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class HomepageController {
    private Scene scene;
    private Stage stage;

    @FXML
    private TableView tableActiveSessions;
    @FXML
    private TableColumn columnName;
    @FXML
    private TableColumn columnAction;
    @FXML
    private Button btnNewSession;
    @FXML
    private Button btnManageSession;
    @FXML
    private Button btnNewGroup;
    @FXML
    private Button btnManageGroup;

    /**
     * Switches to create_session.fxml view
     * @param event
     * @throws Exception
     */
    @FXML
    private void onNewSessionClicked(ActionEvent event) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../session/new_session.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Switches to sessions.fxml view
     * @param event
     * @throws Exception
     */
    @FXML
    private void onManageSessionClicked(ActionEvent event) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../session/sessions.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Switches to create_group.fxml view
     * @param event
     * @throws Exception
     */
    @FXML
    private void onNewGroupClicked(ActionEvent event) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../group/create.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Switches to groups.fxml view
     * @param event
     * @throws Exception
     */
    @FXML
    private void onManageGroupClicked(ActionEvent event) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../group/groups.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }


//    private static class RowButton{
//        private final SimpleStringProperty name;
//        private final Button button;
//
//        public RowButton(SimpleStringProperty name) {
//            this.name = name;
//            button = new Button("Do something");
//        }
//
//        public String getName() {
//            return name.get();
//        }
//
//        public SimpleStringProperty nameProperty() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name.set(name);
//        }
//
//        public Button getBtn() {
//            return button;
//        }
//    }

}

