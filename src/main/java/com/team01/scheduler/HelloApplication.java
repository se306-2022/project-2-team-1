package com.team01.scheduler;

import com.digraph.weighted.models.Edge;
import com.digraph.weighted.models.EdgesLinkedList;
import com.digraph.weighted.models.Graph;
import com.digraph.weighted.models.Node;
import com.digraph.weighted.util.ExportToDotFile;

import com.digraph.weighted.exceptions.InvalidInputException;
import com.digraph.weighted.io.InputController;
import com.digraph.weighted.models.*;
import com.digraph.weighted.util.ExportToDotFile;
import com.digraph.weighted.io.*;
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

    public static void main(String[] args) throws IOException, InvalidInputException {
        List<Node> nodes = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();

        GraphController graphController = new GraphController("graph.dot");
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
  
        InputController ic = InputController.getInstance();
        ic.run(args);
  
        launch();
    }
}