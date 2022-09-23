package com.digraph.weighted.models;

import java.util.Objects;

public class Edge {

    private Node src;
    private Node dest;
    private int weight;

    public Edge(Node src, Node dest, int weight) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }

    public Node getSrc() {
        return src;
    }

    public void setSrc(Node src) {
        this.src = src;
    }

    public Node getDest() {
        return dest;
    }

    public void setDest(Node dest) {
        this.dest = dest;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return weight == edge.weight && src.equals(edge.src) && dest.equals(edge.dest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(src, dest, weight);
    }

    @Override
    public String toString() {
        return "Edge{" +
                "src=" + src +
                ", dest=" + dest +
                ", weight=" + weight +
                '}';
    }
}
