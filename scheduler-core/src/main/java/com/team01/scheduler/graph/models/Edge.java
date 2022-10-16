package com.team01.scheduler.graph.models;

import java.util.Objects;

public class Edge {

    // Source Node
    private Node src;

    // Target Node
    private Node dest;

    // Edge Weight
    private int weight;

    // Next Edge in linked list
    private Edge next;

    // Constructor with wight
    public Edge(Node src, Node dest, int weight) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }

    // Constructor without wight
    public Edge(Node src, Node dest) {
        this.src = src;
        this.dest = dest;
        this.weight = 0;
    }

    /**
     * Setter for next edge
     * @param next
     */
    public void setNext(Edge next) {
        this.next = next;
    }

    /**
     * Getter for next edge
     * @return next edge
     */
    public Edge getNext() {
        return this.next;
    }

    /**
     * Getter for source node
     * @return source node
     */
    public Node getSource() {
        return src;
    }

    /**
     * Setter for source node
     * @param src
     */
    public void setSrc(Node src) {
        this.src = src;
    }

    /**
     * Getter for target node
     * @return target node
     */
    public Node getTarget() {
        return dest;
    }

    /**
     * Setter for target node
     * @param dest
     */
    public void setDest(Node dest) {
        this.dest = dest;
    }

    /**
     * Getter for edge weight
     * @return edge weight
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Setter for edge weight
     * @param weight
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * Method to compare edge objects
     * @param o: object
     * @return true or false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return weight == edge.weight && src.equals(edge.src) && dest.equals(edge.dest);
    }

    /**
     * Method to return hash code for the edge object
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(src, dest, weight);
    }

    /**
     * Converts an edge object to a readable string
     * @return string version of edge object
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Edge{src= ");
        sb.append(src.toString()).append(", dest=").append(dest.toString());
        sb.append(", weight=").append(weight).append("}");
        return sb.toString();
    }
}
