package com.prototypes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A pseudo weighted graph that has weighted nodes (task-time) and weighted edges (parallel-time)
 */
public class WeightedGraph {

    /**
     * Edge constructor
     */
    static class Edge implements Comparable<Edge> {
        int source;
        int destination;
        int weight;

        /**
         * Creates a new Edge object
         * @param source        Source node of edge
         * @param destination   Destination node of edge
         * @param weight        Weight of edge
         */
        public Edge(int source, int destination, int weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }

        /**
         * @param e the object to be compared.
         *
         * @return true or false
         */
        @Override public int compareTo(Edge e) {
            if (e.weight < weight) {
                return 1;
            }
            else {
                return -1;
            }
        }
    }

    /**
     * Node Constructor
     */
    static class Node implements Comparable<Node>{
        int id;
        int weight;

        /**
         * Creates a new Node object
         *
         * @param id        The node number
         * @param weight    The weight of the node
         */
        public Node (int id, int weight) {
            this.id = id;
            this.weight = weight;
        }

        /**
         * @param n the object to be compared.
         *
         * @return true or false
         */
        @Override public int compareTo(Node n) {
            if (n.weight < weight) {
                return 1;
            }
            else {
                return -1;
            }
        }

        public int getWeight() {
            return this.weight;
        }

        public int getId() {
            return this.id;
        }

    }

    /**
     * The graph class that creates edges and nodes as well as instantiate the graph space. it keeps track of
     * all nodes and edges within this graph
     */
    static class Graph{
        int vertices;
        LinkedList<Edge> [] edgeList;
        Node [] nodeList;

        /**
         * Instantiates the graph object with a number of expected vertices
         *
         * @param vertices  The number of vertices in the graph
         */
        Graph(int vertices) {
            this.vertices = vertices;
            edgeList = new LinkedList[vertices];
            //initialize adjacency lists for all the vertices
            for (int i = 0; i <vertices ; i++) {
                edgeList[i] = new LinkedList<>();
            }

            nodeList = new Node [100];
        }

        /**
         * Adds an edge to the graph. Note that since a source node can have multiple outgoing edges, each node
         * has a sublist that contains unique destinations and weights
         *
         * @param source        Source node of edge
         * @param destination   Destination node of edge
         * @param weight        Weight of edge
         */
        public void addEdge(int source, int destination, int weight) {
            Edge edge = new Edge(source, destination, weight);
            edgeList[source].addFirst(edge); //for directed graph
        }

        /**
         * Adds a node to the graph and keeps track of the id and weight of that node.
         *
         * @param id        The id of the node
         * @param weight    The weight of the node
         */
        public void addNode(int id ,int weight) {
            Node node = new Node(id, weight);
            nodeList[id] = node;
        }

        /**
         * Returns the node valid in the nodeList.
         * @param id    The id of the node
         * @return      The node object or null
         */
        public Node getNode(int id) {
            if (!nodeList[id].equals(null)) return nodeList[id];
            else {
                System.out.println("Node does not exist!");
                return null;
            }
        }

        /**
         * Gets the adjacent nodes of the current node
         * @param id    The id of the node
         * @return      An array list of all nodes that are adjacent and ingoing to the source node
         */
        public List<Integer> getAdjacentNodes(int id) {
            ArrayList<Integer> adjacentNodes = new ArrayList<Integer>();

            //Consider all edges in edgeList
            for (LinkedList<Edge> edges : edgeList) {
                if (edges.size() > 0) {

                    //Compare if source node in edge
                    for (Edge edge : edges) {
                        if (edge.source == id) adjacentNodes.add(edge.destination);
                    }
                }
            }

            return adjacentNodes;
        }


        /**
         * Prints the graph with node and edge information
         */
        public void printGraph(){
            for (int i = 0; i < vertices ; i++) {
                //System.out.println(edgeList.length);
                //System.out.println(nodeList.length);
                LinkedList<Edge> currentEdge = edgeList[i];
                for (Edge edge : currentEdge) {
                    System.out.println("vertex " + i + " with node weight " + nodeList[i].weight + " is connected to " +
                            edge.destination + " with edge weight " + edge.weight);
                }
            }
        }
    }
}