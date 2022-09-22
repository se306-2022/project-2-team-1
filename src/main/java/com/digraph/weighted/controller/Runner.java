package com.digraph.weighted.controller;

import com.digraph.weighted.models.Edge;
import com.digraph.weighted.models.Graph;
import com.digraph.weighted.models.Node;
import com.digraph.weighted.util.GraphLogger;

public class Runner {

    public static void main(String[] args) {

        Graph g = new Graph();

        // add nodes
        g.addNode(new Node(1));
        g.addNode(new Node(2));
        g.addNode(new Node(3));
        g.addNode(new Node(4));
        g.addNodes(new Node(6), new Node(7));

        // add edges
        g.addEdge(new Edge(new Node(1), new Node(2), 11));
        g.addEdge(new Edge(new Node(3), new Node(1), 2));
        g.addEdge(new Edge(new Node(4), new Node(4), 21)); // self loop edge
        g.addEdge(new Edge(new Node(2), new Node(4), 7));
        g.addEdge(new Edge(new Node(2), new Node(3), 15));
        g.addEdge(new Edge(new Node(6), new Node(1), 33));
        g.addEdge(new Edge(new Node(6), new Node(5), 66));
        g.addEdge(new Edge(new Node(6), new Node(6), 55)); // self loop edge
        g.addEdge(new Edge(new Node(7), new Node(1), 69));

        // testing out the addEdges() method
        g.addEdges(new Edge(new Node(7), new Node(1), 69),
                   new Edge(new Node(4), new Node(2), 38),
                   new Edge(new Node(1), new Node(4), 77));

        // testing adding a duplicate edge
        g.addEdge(new Edge(new Node(2), new Node(3), 15));
        g.addEdge(new Edge(new Node(2), new Node(3), 15));

        // testing an edge with 0 or negative weight
        g.addEdge(new Edge(new Node(7), new Node(3), -11));
        g.addEdge(new Edge(new Node(7), new Node(1), 0));

        // testing adding edge for node that doesnt exist
        g.addEdge(new Edge(new Node(10), new Node(1), 11));

        // print graph to console
        GraphLogger logger = new GraphLogger(g.getAdjacencyList());
        logger.log();

    }
}
