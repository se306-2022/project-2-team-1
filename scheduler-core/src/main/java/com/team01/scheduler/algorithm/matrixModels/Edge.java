package com.team01.scheduler.algorithm.matrixModels;

import java.util.Objects;

/**
 * The edge class is used to parse in edges from the graphviz parser.
 * The adjacency matrix implementation does not directly use this class,
 * however it is required to make this graph model compatible with the
 * graphviz parser class.
 */
public class Edge {
    private Node srcNode;
    private Node destNode;
    private int weight;

    /**
     * Constructor
     * @param srcNode
     * @param destNode
     * @param weight
     */
    public Edge(Node srcNode, Node destNode, int weight){
        this.srcNode = srcNode;
        this.destNode = destNode;
        this.weight = weight;
    }

    /**
     * Getter for source node
     * @return source node
     */
    public Node getSource() {
        return srcNode;
    }

    /**
     * Getter for target node
     * @return target node
     */
    public Node getTarget() {
        return destNode;
    }

    /**
     * Getter for node weight
     * @return int: node weight
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Function to compare two edge objects
     * @param o: edge object
     * @return boolean: True if objects are the same, False otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return weight == edge.weight && Objects.equals(srcNode, edge.srcNode) && Objects.equals(destNode, edge.destNode);
    }

    /**
     * Function to generate the hashcode for an edge object
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(srcNode, destNode, weight);
    }

    /**
     * Function to convert an edge object into a human-readable string
     * @return edge object as a human-readable string
     */
    @Override
    public String toString() {
        return "Edge{" +
                "srcNode=" + srcNode.getName() +
                ", destNode=" + destNode.getName() +
                ", weight=" + weight +
                '}';
    }
}
