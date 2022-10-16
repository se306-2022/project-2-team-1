package com.team01.graph;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GraphModelTest {

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

        this.graph = new Graph(edges, nodes);
    }

    @Test
    void testFourNodeTaskGraph() {

        int numNodes = 4;

        Assertions.assertEquals(numNodes, graph.getNodes().size());

    }

    @Test
    void testNoDependencies() {

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

        Assertions.assertThrows(IllegalArgumentException.class, () -> this.graph = new Graph(edges, nodes));

    }

    @Test
    void testNoNodes() {

        List<Node> nodes = new ArrayList<>();
        Node a = new Node("a", 2);
        Node b = new Node("b", 3);
        Node c = new Node("c", 1);
        Node d = new Node("d", 2);


        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(a, b, 1));
        edges.add(new Edge(a, c, 2));
        edges.add(new Edge(b, d, 2));
        edges.add(new Edge(c, d,1));

        Assertions.assertThrows(IllegalArgumentException.class, () -> this.graph = new Graph(edges, nodes));

    }

    @Test
    void testNoContent() {

        List<Node> nodes = new ArrayList<>();

        List<Edge> edges = new ArrayList<>();

        Assertions.assertThrows(IllegalArgumentException.class, () -> this.graph = new Graph(edges, nodes));

    }
}