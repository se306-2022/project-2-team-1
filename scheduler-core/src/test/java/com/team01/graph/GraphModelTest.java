package com.team01.graph;

import java.util.ArrayList;
import java.util.List;

import com.team01.scheduler.graph.models.Edge;
import com.team01.scheduler.graph.models.Graph;
import com.team01.scheduler.graph.models.Node;
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
        int countNodes = 0;

        for (Node node : graph.getNodes()) {
            countNodes++;
        }

        Assertions.assertEquals(numNodes, countNodes);

    }
}