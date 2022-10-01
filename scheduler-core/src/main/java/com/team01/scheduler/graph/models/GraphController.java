package com.team01.scheduler.graph.models;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GraphController {

    private Graph graph;

    public GraphController(String fileName) throws IOException {
        List<Node> nodes = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();

        File file = new File(fileName);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String input;

        while ((input = br.readLine()) != null) {

            // Ignore first and last line
            if (input.contains("}") || input.contains("{") || !input.contains("Weight")) {
                continue;
            }
            input = input.trim();
            String[] split = input.split("\\s+");
            // Reading in a node
            if (split.length == 2) {
                split[1]=split[1].replace("[Weight=","").trim();
                split[1]=split[1].replaceAll("];","");
                split[1]=split[1].replaceAll("\"/[^,]*$\"","");
                nodes.add(new Node(split[0], Integer.parseInt(split[1])));
            } else { // Reading in an Edge
                Node source;
                Node target;

                // Check if source node has already been read
                Optional<Node> tempSource = Node.containsName(nodes, split[0]);
                if (tempSource.isPresent()) {
                    source = tempSource.get();
                } else {
                    source = new Node(split[0], 0);
                    nodes.add(source);
                }

                // Check if target node has already been read
                Optional<Node> tempTarget = Node.containsName(nodes, split[2]);
                if (tempTarget.isPresent()) {
                    target = tempTarget.get();
                } else {
                    target = new Node(split[2], 0);
                    nodes.add(target);
                }

                // Add edge
                split[3]=split[3].replace("[Weight=","").trim();
                split[3]=split[3].replaceAll("];","");
                split[3]=split[3].replaceAll("\"/[^,]*$\"","").trim();
                edges.add(new Edge(source, target, Integer.parseInt(split[3])));
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
