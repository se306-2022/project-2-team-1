package com.digraph.weighted.models;

import java.util.Objects;

public class Node implements Comparable<Node>{
    private Integer value;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Node(int value, String name){
        this.value = value;
        this.name = name;
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
        return value == node.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "" + value + "";
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
