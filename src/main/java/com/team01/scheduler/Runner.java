package com.team01.scheduler;

import com.team01.scheduler.graph.models.Edge;
import com.team01.scheduler.graph.models.Node;
import com.team01.scheduler.algorithm.DepthFirstSearch;
import com.team01.scheduler.graph.models.Graph;

public class Runner {

    /**
     * Creates a sample graph using the node and edge
     * weights provided in the brief (refer to Figure 1).
     *
     * @return Graph instance
     */
    private Graph setupGraph() {
        Node nodeA = new Node(2, "a");
        Node nodeB = new Node(3, "b");
        Node nodeC = new Node(3, "c");
        Node nodeD = new Node(2, "d");

        Edge edgeAB = new Edge(nodeA, nodeB, 1);
        Edge edgeAC = new Edge(nodeA, nodeC, 2);
        Edge edgeBD = new Edge(nodeB, nodeD, 2);
        Edge edgeCD = new Edge(nodeC, nodeD, 1);

        Graph graph = new Graph();
        graph.addNodes(nodeA, nodeB, nodeC, nodeD);
        graph.addEdges(edgeAB, edgeAC, edgeBD, edgeCD);

        return graph;
    }

    public void runDepthFirstSearch() {

        Graph graph = setupGraph();

        var dfs = new DepthFirstSearch(graph);
        // dfs.run();
    }
}
