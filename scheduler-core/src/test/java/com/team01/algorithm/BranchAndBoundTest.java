package com.team01.algorithm;

import com.team01.scheduler.algorithm.BranchAndBound;
import com.team01.scheduler.algorithm.Schedule;


import com.team01.scheduler.algorithm.matrixModels.Edge;
import com.team01.scheduler.algorithm.matrixModels.Graph;
import com.team01.scheduler.algorithm.matrixModels.Node;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class BranchAndBoundTest {

    @Test
    void testBranchAndBound() {

        int shortestPath = 8;

        ArrayList<Node> nodes = new ArrayList<>();
        Node a = new Node(0,"a", 2);
        Node b = new Node(1,"b", 3);
        Node c = new Node(2,"c", 1);
        Node d = new Node(3,"d", 2);
        nodes.add(a);
        nodes.add(b);
        nodes.add(c);
        nodes.add(d);

        ArrayList<Edge> edges = new ArrayList<>();
        edges.add(new Edge(a, b, 1));
        edges.add(new Edge(a, c, 2));
        edges.add(new Edge(b, d, 2));
        edges.add(new Edge(c, d,1));

        Graph graph = new Graph(nodes,edges);

        BranchAndBound bnb = new BranchAndBound();
        Schedule s = bnb.run(graph, 2,1);

        Assertions.assertEquals(shortestPath, s.getPathLength());

    }

    @Test
    void testBranchAndBound2() {

        int shortestPath = 4;

        ArrayList<Node> nodes = new ArrayList<>();
        Node a = new Node(0,"a", 1);
        Node b = new Node(1,"b", 2);
        Node c = new Node(2,"c", 1);
        nodes.add(a);
        nodes.add(b);
        nodes.add(c);

        ArrayList<Edge> edges = new ArrayList<>();
        edges.add(new Edge(a, b, 1));
        edges.add(new Edge(a, c, 2));
        edges.add(new Edge(b, c, 1));

        Graph graph = new Graph(nodes,edges);

        BranchAndBound bnb = new BranchAndBound();
        Schedule s = bnb.run(graph, 2,1);

        Assertions.assertEquals(shortestPath, s.getPathLength());

    }

    @Test
    void testTaskName() {

        BranchAndBound bnb = new BranchAndBound();

        Assertions.assertEquals("Scheduler - DFS Branch and Bound", bnb.getTaskName());

    }
}
