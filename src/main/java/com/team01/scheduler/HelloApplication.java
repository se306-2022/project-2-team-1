package com.team01.scheduler;

import com.digraph.weighted.models.Edge;
import com.digraph.weighted.models.EdgesLinkedList;
import com.digraph.weighted.models.Graph;
import com.digraph.weighted.models.Node;
import com.digraph.weighted.util.ExportToDotFile;
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

        Graph graph = new Graph(edges, nodes);

        Map<Node, EdgesLinkedList> graphStructure = new HashMap<>();
        graphStructure = graph.getGraph();

        for (EdgesLinkedList currentList: graphStructure.values()) {
            int listSize = currentList.size();
            Edge currentEdge = currentList.get(0);

            for (int i = 0; i < listSize; i++) {
                currentEdge = currentList.get(i);

                System.out.println(currentEdge.toString());
            }
        }
/*
        List<Node> nodes = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();

        File file = new File("graph.dot");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String input;

        while ((input = br.readLine()) != null) {
            String[] split = input.split(" ");

            // Reading in a node
            if (split.length == 3) {
                nodes.add(new Node(split[0], Character.getNumericValue(split[1].charAt(split[1].length() -2))));
            } else { // Reading in an Edge
                Node source;
                Node target;

                // Check if source node has already been read
                Optional<Node> tempSource = Node.containsName(nodes, split[0]);
                if (tempSource.isPresent()) {
                    source = tempSource.get();
                    nodes.add(source);
                } else {
                    source = new Node(split[0], 0);
                    nodes.add(source);
                }

                // Check if target node has already been read
                Optional<Node> tempTarget = Node.containsName(nodes, split[2]);
                if (tempTarget.isPresent()) {
                    target = tempTarget.get();
                    nodes.add(target);
                } else {
                    target = new Node(split[2], 0);
                    nodes.add(target);
                }

                // Add edge
                edges.add(new Edge(source, target, Character.getNumericValue(split[3].charAt(split[3].length() -2))));
            }
        }

        Graph graph = new Graph(edges, nodes);

        Map<Node, EdgesLinkedList> graphStructure = new HashMap<>();
        graphStructure = graph.getGraph();

        for (EdgesLinkedList currentList: graphStructure.values()) {
            int listSize = currentList.size();
            Edge currentEdge = currentList.get(0);

            for (int i = 0; i < listSize; i++) {
                currentEdge = currentList.get(i);

                System.out.println(currentEdge.toString());
            }
        }*/
        ExportToDotFile ex = new ExportToDotFile(graph);
        ex.writeDot();
        launch();
    }
}