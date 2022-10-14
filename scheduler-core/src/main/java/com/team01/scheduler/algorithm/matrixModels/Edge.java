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

    public Edge(Node srcNode, Node destNode, int weight){
        this.srcNode = srcNode;
        this.destNode = destNode;
        this.weight = weight;
    }

    public Node getSource() {
        return srcNode;
    }

    public Node getTarget() {
        return destNode;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return weight == edge.weight && Objects.equals(srcNode, edge.srcNode) && Objects.equals(destNode, edge.destNode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(srcNode, destNode, weight);
    }

    @Override
    public String toString() {
        return "Edge{" +
                "srcNode=" + srcNode.getName() +
                ", destNode=" + destNode.getName() +
                ", weight=" + weight +
                '}';
    }
}
