package com.team01.algorithm;

import com.team01.scheduler.algorithm.BranchAndBound;
import com.team01.scheduler.graph.models.Edge;
import com.team01.scheduler.graph.models.Graph;
import com.team01.scheduler.graph.models.Node;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class BranchAndBoundTest {

    @Test
    void testBranchAndBound() {

        int shortestPath = 8;

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
        BranchAndBound bnb = new BranchAndBound();
        bnb.run(graph);

    }
}
