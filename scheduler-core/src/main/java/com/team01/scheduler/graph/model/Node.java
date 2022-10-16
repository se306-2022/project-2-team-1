package com.team01.scheduler.graph.model;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Node implements Comparable<Node>{
    /**
     * Each node has a unique id which is used as the index for
     * the adjacency matrix
     */
    private int id;
    /**
     * The name of each node in the graph
     */
    private String name;
    /**
     * The computation cost for each node in the graph
     */
    private int computationCost;

    public Node(){

    }

    public Node(int id, String name, int computationCost) {
        this.id = id;
        this.name = name;
        this.computationCost = computationCost;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getComputationCost() {
        return computationCost;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setComputationCost(int computationCost) {
        this.computationCost = computationCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return id == node.id && computationCost == node.computationCost && Objects.equals(name, node.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, computationCost);
    }

    /**
     * Implementing the Comparable interface allows the Node objects in a
     * collection to be ordered using Collection.sort() method.
     * @param o the object to be compared.
     * @return
     */
    @Override
    public int compareTo(Node o) {
        if (this.computationCost == o.computationCost)
            return 0;
        return (this.computationCost > o.computationCost) ? 1 : -1;
    }

    @Override
    public String toString() {
        return "Node: " + name;
    }

    /**
     * Method to check if Node already exists in the list of nodes
     * for the current graph being read
     * @param list
     * @param name
     * @return Optional<Node> that will the node object if it exists in the list
     */
    static public Optional<Node> containsName(final List<Node> list, final String name) {
        return list.stream().filter(o -> o.getName().equals(name)).findFirst();
    }
}
