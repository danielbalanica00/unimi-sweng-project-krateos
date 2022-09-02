package com.simpolab.client_manager.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneUtils {
    public static void switchTo(String scenePath, Stage stage) throws Exception{
        Scene scene;

        Parent root = FXMLLoader.load(SceneUtils.class.getResource(scenePath));
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
