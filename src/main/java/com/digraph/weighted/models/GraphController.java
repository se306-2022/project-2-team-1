package com.digraph.weighted.models;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GraphController {

    private Graph graph;

    public GraphController(String fileName) throws IOException {
        String filePath = "src/main/resources/" + fileName;
        List<Node> nodes = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();

        File file = new File(filePath);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String input;

        while ((input = br.readLine()) != null) {

            // Ignore first and last line
            if (input.contains("}") || input.contains("{")) {
                continue;
            }

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

        this.graph = new Graph(edges, nodes);

    }

    public Graph getGraph() {
        return this.graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

}
