package com.team01.scheduler;


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
        // "src/main/resources/graph.dot"

        /*
            List<Node> nodes = new ArrayList<>();
            Node a = new Node("a", 2);
            Node b = new Node("b", 3);
            Node c = new Node("c", 1);
            Node d = new Node("d", 2);
            nodes.add(a);
            nodes.add(b);
            nodes.add(c);
            nodes.add(d);

            List<Edge> edges = new ArrayList<>();
            edges.add(new Edge(a, b, 1));
            edges.add(new Edge(a, c, 2));
            edges.add(new Edge(b, d, 2));
            edges.add(new Edge(c, d,1));
        */

        InputController ic = InputController.getInstance();
        ic.run(args);
    }
}