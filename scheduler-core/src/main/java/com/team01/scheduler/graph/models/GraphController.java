package com.team01.scheduler.graph.models;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GraphController {

    private Graph graph;

    /**
     * Method to read in graph from dot file and create a graph object
     * @param br: buffered reader
     * @throws IOException
     */

    private void parseGraphviz(BufferedReader br) throws IOException {
        List<Node> nodes = new ArrayList<>();
        List<Node> possibleStartNodes = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();
        String input;

        while ((input = br.readLine()) != null) {

            // Ignore first and last line or those that don't have "weight"
            if (input.contains("}") || input.contains("{") || !input.contains("Weight")) {
                continue;
            }

            input = input.replaceAll("\\s+","");
            input = input.trim();
            // Get index to determine whether it's an edge or a node later on
            int arrowIndex = input.indexOf("->");
            int bracketIndex = input.indexOf("[");
            int weightIndex = input.indexOf("Weight=");

            // Get weight of current edge or node
            int weight;
            int endIndex = -1;
            for (int i = weightIndex + 7; i < input.length(); i++) {
                try {
                    Integer.parseInt(input.substring(i, i + 1));
                } catch (NumberFormatException e) {
                    endIndex = i;
                    break;
                }
            }
            weight = Integer.parseInt(input.substring(weightIndex+7,endIndex));

            if (arrowIndex!= -1) {
                // Reading in an edge
                String source = input.substring(0,arrowIndex);
                String target = input.substring(arrowIndex+2, bracketIndex);

                Optional<Node> tempSource = Node.containsName(nodes, source);
                Node sourceNode;
                Node targetNode;
                if (tempSource.isPresent()) {
                    sourceNode = tempSource.get();
                } else {
                    sourceNode = new Node(source, 0);
                    nodes.add(sourceNode);
                }

                // Check if target node has already been read
                Optional<Node> tempTarget = Node.containsName(nodes, target);
                if (tempTarget.isPresent()) {
                    targetNode = tempTarget.get();
                } else {
                    targetNode = new Node(target, 0);
                    nodes.add(targetNode);
                }
                edges.add(new Edge(sourceNode, targetNode, weight));

                // Ff a node has a parent, it cannot be a start node
                possibleStartNodes.remove(targetNode);

            } else {
                // Reading in a node
                String source = input.substring(0,bracketIndex);
                nodes.add(new Node(source, weight));

                // All nodes could possibly be a start node
                possibleStartNodes.add(new Node(source, weight));
            }

        }


        this.graph = new Graph(edges, nodes, possibleStartNodes);
    }

    /**
     * Constructor for Graph Controller
     * @param contents
     * @throws IOException
     */
    public GraphController(String contents) throws IOException {
        var reader = new BufferedReader(new StringReader(contents));
        parseGraphviz(reader);
    }

    /**
     * Constructor for Graph Controller
     * @param file
     * @throws IOException
     */
    public GraphController(File file) throws IOException {
        var reader = new BufferedReader(new FileReader(file));
        parseGraphviz(reader);
    }

    /**
     * Getter for graph object
     * @return graph object
     */
    public Graph getGraph() {
        return this.graph;
    }

    /**
     * Setter for the graph object
     * @param graph
     */
    public void setGraph(Graph graph) {
        this.graph = graph;
    }

}
