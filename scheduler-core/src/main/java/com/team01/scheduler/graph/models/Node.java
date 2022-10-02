package com.team01.scheduler.graph.models;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Node implements Comparable<Node>{
    private Integer value;
    private String name;
    private Node next;

    /**
     * Getter for Node name
     * @return Node name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for Node name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Constructor
     * @param name
     * @param value
     */
    public Node(String name, int value){
        this.value = value;
        this.name = name;
    }

    /**
     * Setter for next Node
     * @param next
     */
    public void setNext(Node next) {
        this.next = next;
    }

    /**
     * Getter for next Node
     * @return
     */
    public Node getNext() {
        return this.next;
    }

    /**
     * Getter for Node weight
     * @return Node weight
     */
    public Integer getValue() {
        return value;
    }

    /**
     * Setter for Node weight
     * @param value
     */
    public void setValue(Integer value) {
        this.value = value;
    }

    /**
     * Method to compare two node objects
     * @param o
     * @return true or false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(value, node.value) && Objects.equals(name, node.name);
    }

    /**
     * Method to compute hash code for a node object
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(value, name, next);
    }

    /**
     * Method to convert a node object to a readable string
     * @return Node object as string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{Name: ");
        sb.append(name).append(" value: ").append(value).append("}");
        return sb.toString();
    }

    /**
     * Implementing the Comparable interface allows the Node objects in a
     * collection to be ordered using Collection.sort() method.
     * @param o the object to be compared.
     * @return
     */
    @Override
    public int compareTo(Node o) {
        if (this.value == o.value)
            return 0;
        return (this.value > o.value) ? 1 : -1;
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
