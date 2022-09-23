package com.digraph.weighted.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {

    private static final Logger LOGGER = LoggerFactory.getLogger(Graph.class);

    // Adjacency Map
    private Map<Node, EdgesLinkedList> adjacencyMap;

    // Root Node
    private Node root;

    // List of Nodes
    private List<Node> nodes;

    // List of Edges
    private List<Edge> edges;

    public Graph(List<Edge> edges, List<Node> nodes) {
        if (edges.isEmpty() || nodes.isEmpty()) {
            throw new IllegalArgumentException("Edges or Nodes Empty");
        }

        this.nodes = nodes;
        this.edges = edges;

        // Create adjacency map
        adjacencyMap = new HashMap<>();
        int i = 0;
        for (Edge edge : edges) {

            Node source = edge.getSource();

            if (!adjacencyMap.containsKey(source)) {
                adjacencyMap.put(source, new EdgesLinkedList());
            }

            adjacencyMap.get(source).append(edge);

            if (i == 0) {
                root = source;
            }

            i++;
        }
    }

    /**
     * Method to return Graph in as an Adjacency Map
     * @return the Adjacency Map containing the graph
     */
    public Map<Node, EdgesLinkedList> getGraph() {
        return this.adjacencyMap;
    }

    /**
     * Method to return list of Nodes in the graph
     * @return list of Nodes
     */
    public List<Node> getNodes() {
        return this.nodes;
    }

    /**
     * Method to return list of Edges in the graph
     * @return list of Edges
     */
    public List<Edge> getEdges() {
        return this.edges;
    }

}
