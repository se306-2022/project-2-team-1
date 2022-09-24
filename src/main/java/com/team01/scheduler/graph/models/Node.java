package com.team01.scheduler.graph.models;

import java.util.Objects;

public class Node implements Comparable<Node>{
    private Integer value;
    private String name;
    private Node next;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Node(String name, int value){
        this.value = value;
        this.name = name;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Node getNext() {
        return this.next;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(value, node.value) && Objects.equals(name, node.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, name, next);
    }

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
}
