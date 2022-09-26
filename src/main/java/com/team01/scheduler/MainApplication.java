package com.team01.scheduler;

import com.team01.scheduler.io.InputController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void attachVisualisation(String[] args) {
        launch(args);
    }

    public static void main(String[] args) {
        InputController ic = new InputController(args);

        switch (ic.getInvocationType())
        {
            case DEBUG:
                launch(args);
                break;

            case VISUALIZATION:
                attachVisualisation(args);
                ic.runScheduler();
                break;

            case HEADLESS:
                ic.runScheduler();
                break;
        }

        Platform.exit();
    }
}