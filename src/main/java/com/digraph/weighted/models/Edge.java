package com.digraph.weighted.models;

import java.util.Objects;

public class Edge {

    private Node src;
    private Node dest;
    private int weight;
    private Edge next;

    public Edge(Node src, Node dest, int weight) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }

    public Edge(Node src, Node dest) {
        this.src = src;
        this.dest = dest;
        this.weight = 0;
    }

    public void setNext(Edge next) {
        this.next = next;
    }

    public Edge getNext() {
        return this.next;
    }

    public Node getSource() {
        return src;
    }

    public void setSrc(Node src) {
        this.src = src;
    }

    public Node getTarget() {
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
        final StringBuilder sb = new StringBuilder("Edge{src= ");
        sb.append(src.toString()).append(", dest=").append(dest.toString());
        sb.append(", weight=").append(weight).append("}");
        return sb.toString();
    }
}
