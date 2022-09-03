package com.simpolab.client_manager.session;

import com.simpolab.client_manager.electors.Elector;
import com.simpolab.client_manager.group.Group;
import com.simpolab.client_manager.login.AuthHandler;
import com.simpolab.client_manager.utils.HttpUtils;
import com.simpolab.client_manager.utils.JsonUtils;
import com.simpolab.client_manager.utils.SceneUtils;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class AddGroupsController implements Initializable {
    private List<Group> selectedGroups;
    private static int sessionId;

    @FXML
    private ListView<Group> lvAvailableGroups;
    @FXML
    private ListView<Group> lvAddedGroups;

    @FXML
    private void onBtnAddGroupsClicked(ActionEvent event) throws Exception{
        ObservableList<Group> groups = lvAvailableGroups.getSelectionModel().getSelectedItems();

        // imagine checking before shoving shit into lists
        for(Group group : groups)
            HttpUtils.put("/api/v1/session/"+sessionId+"/group/" + group.getId(), Map.of("Authorization", "Bearer " + AuthHandler.getAccessToken()), null);

        selectedGroups.addAll(groups);

        refreshLists();
    }

    public static void initSession(int initSessionId){
        sessionId = initSessionId;
    }

    @FXML
    private void onBtnRemoveGroupsClicked(ActionEvent event) throws Exception{
        ObservableList<Group> groups = lvAddedGroups.getSelectionModel().getSelectedItems();

        for(Group group : groups)
            HttpUtils.delete("/api/v1/session/"+sessionId+"/group/"+group.getId(), Map.of("Authorization", "Bearer " + AuthHandler.getAccessToken()));

        selectedGroups.removeIf(group ->
                groups.stream().anyMatch(selGroup -> selGroup.getId() == group.getId())
        );

        refreshLists();
    }

    @FXML
    private void onBtnBackClicked(ActionEvent event) throws Exception{
        SceneUtils.switchTo("homepage/homepage.fxml");
    }

    @FXML
    private void onBtnCreateClicked(ActionEvent event) throws Exception{
        SceneUtils.switchToHomepage();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lvAddedGroups.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        lvAvailableGroups.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        selectedGroups = new ArrayList<>();

        refreshLists();
    }

    private void refreshLists(){
        deleteEntries();

        String availableGroupsJson = HttpUtils.get("/api/v1/group", Map.of("Authorization", "Bearer " + AuthHandler.getAccessToken()));
        List<Group> availableGroups = JsonUtils.parseJsonArray(availableGroupsJson, Group.class);

        availableGroups.removeIf(group ->
                selectedGroups.stream().anyMatch(selGroup -> selGroup.getId() == group.getId())
        );

        lvAvailableGroups.getItems().addAll(availableGroups);
        lvAddedGroups.getItems().addAll(selectedGroups);
    }

    private void deleteEntries(){
        lvAvailableGroups.getItems().clear();
        lvAddedGroups.getItems().clear();
    }
}
