package com.digraph.weighted.controller;

import com.digraph.weighted.models.Edge;
import com.digraph.weighted.models.Graph;
import com.digraph.weighted.models.Node;
import com.digraph.weighted.util.GraphLogger;

import java.util.ArrayList;
import java.util.List;

public class Runner {

    public static void main(String[] args) {

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

        System.out.println(graph);

    }
}
