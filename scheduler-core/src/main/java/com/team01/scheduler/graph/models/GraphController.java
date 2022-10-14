package com.team01.scheduler.graph.models;

import com.team01.scheduler.algorithm.matrixModels.Node;
import com.team01.scheduler.algorithm.matrixModels.Graph;
import com.team01.scheduler.algorithm.matrixModels.Edge;
import java.io.*;
import java.util.ArrayList;
import java.util.Optional;

public class GraphController {

    private Graph graph;

    private static int ID = 0;

    /**
     * Method to read in graph from dot file and create a graph object
     * @param br: buffered reader
     * @throws IOException
     */

    private void parseGraphviz(BufferedReader br) throws IOException {
        ArrayList<Node> nodes = new ArrayList<>();
        ArrayList<Edge> edges = new ArrayList<>();
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
                    sourceNode = new Node(incrementAndGetId(),source, 0);
                    nodes.add(sourceNode);
                }

                // Check if target node has already been read
                Optional<Node> tempTarget = Node.containsName(nodes, target);
                if (tempTarget.isPresent()) {
                    targetNode = tempTarget.get();
                } else {
                    targetNode = new Node(incrementAndGetId(),target, 0);
                    nodes.add(targetNode);
                }
                edges.add(new Edge(sourceNode, targetNode, weight));

            } else {
                // Reading in a node
                String source = input.substring(0,bracketIndex);
                nodes.add(new Node(incrementAndGetId(),source, weight));
            }

        }


        this.graph = new Graph(nodes,edges);
    }

    public int incrementAndGetId(){
        int currentIdValue = ID;
        ID++;
        return currentIdValue;
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
