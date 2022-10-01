package com.team01.scheduler.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        URL uiPath = MainApplication.class.getClassLoader().getResource("main-view.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(uiPath);
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}