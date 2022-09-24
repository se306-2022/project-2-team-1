package com.team01.scheduler;

import com.team01.scheduler.algorithm.DepthFirstSearch;
import com.team01.scheduler.algorithm.IRunnable;
import com.team01.scheduler.graph.models.Edge;
import com.team01.scheduler.graph.models.EdgesLinkedList;
import com.team01.scheduler.graph.models.Graph;
import com.team01.scheduler.graph.models.Node;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        launch();
    }
}