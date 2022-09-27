package com.team01.graphmodel

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GraphTest {

    private Graph graph;

    @BeforeEach
    void createGraph() {
        List<Node> nodes = new ArrayList<>();
        Node a = new Node("a", 2);
        Node b = new Node("b", 3);
        Node c = new Node("c", 1);
        Node d = new Node("d", 2);
        nodes.add(a);
        nodes.add(b);
        nodes.add(c);
        nodes.add(d);

        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(a, b, 1));
        edges.add(new Edge(a, c, 2));
        edges.add(new Edge(b, d, 2));
        edges.add(new Edge(c, d,1));

        Graph graph = new Graph(edges, nodes);
    }

    @Test
    void testThreeNodeTaskGraph() {

        int numNodes = 3;
        int countNodes = 0;

        for (Node node : graph.getNodes()) {
            countNodes++;
        }

        assertEquals(numNodes, countNodes);

    }
}