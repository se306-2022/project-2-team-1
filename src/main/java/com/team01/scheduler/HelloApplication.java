package com.team01.scheduler;

import com.digraph.weighted.models.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws IOException {

        GraphController graphController = new GraphController("src/main/resources/graph.dot");
        Graph graph = graphController.getGraph();

        Map<Node, EdgesLinkedList> graphStructure;
        graphStructure = graph.getGraph();

        for (EdgesLinkedList currentList: graphStructure.values()) {
            int listSize = currentList.size();
            Edge currentEdge = currentList.get(0);

            for (int i = 0; i < listSize; i++) {
                currentEdge = currentList.get(i);

                System.out.println(currentEdge.toString());
            }
        }

        launch();
    }
}