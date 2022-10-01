package com.team01.scheduler.matrix.model;

import java.util.Objects;

public class Edge {
    private Node srcNode;
    private Node destNode;
    private int weight;

    public Edge(Node srcNode, Node destNode, int weight){
        this.srcNode = srcNode;
        this.destNode = destNode;
        this.weight = weight;
    }

    public Node getSrcNode() {
        return srcNode;
    }

    public Node getDestNode() {
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
