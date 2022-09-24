package com.team01.scheduler.algorithm;

import com.team01.scheduler.graph.exceptions.InvalidInputException;
import com.team01.scheduler.graph.models.Graph;
import com.team01.scheduler.graph.models.Node;

import java.util.Stack;

public class DepthFirstSearch implements IRunnable {

    private Graph graph;

    public DepthFirstSearch(Graph graph) {
        this.graph = graph;
    }

    @Override
    public void run(Node startNode) {

        if (!graph.isNodeValid(startNode))
            throw new InvalidInputException("Starting node must be part of graph");

        var stack = new Stack<Node>();
        stack.push(startNode);

        while (!stack.isEmpty()) {
            Node iter = stack.pop();

            // Visit the node (preorder)
            // System.out.println(n);
        }
    }
}
