package com.team01.scheduler.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainApplication extends Application {

    /**
     * JavaFX Start method
     *
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        // Get main view FXML file
        URL uiPath = MainApplication.class.getClassLoader().getResource("main-view.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(uiPath);

        // Create scene and display
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Entry point for Scheduler Debug Console
     * @param args program arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}