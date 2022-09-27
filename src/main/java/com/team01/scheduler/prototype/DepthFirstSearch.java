package com.team01.scheduler.prototype;

import com.team01.scheduler.algorithm.IRunnable;
import com.team01.scheduler.graph.exceptions.InvalidInputException;
import com.team01.scheduler.graph.models.Edge;
import com.team01.scheduler.graph.models.EdgesLinkedList;
import com.team01.scheduler.graph.models.Graph;
import com.team01.scheduler.graph.models.Node;

import java.util.List;
import java.util.Map;
import java.util.Stack;

public class DepthFirstSearch implements IRunnable {

    public DepthFirstSearch() {
    }

    @Override
    public String getTaskName() {
        return "Depth First Search";
    }

    @Override
    public void run(Graph graph) {

        Map<Node, EdgesLinkedList> map = graph.getGraph();
        Node startNode = graph.getNodes().get(0);

        if (!map.containsKey(startNode))
            throw new InvalidInputException("Starting node must be part of graph");

        var stack = new Stack<Node>();
        stack.push(startNode);

        while (!stack.isEmpty()) {
            Node iter = stack.pop();

            // Visit the node (preorder)
            System.out.println(iter);

            // Loop over children
            for (Edge edge : map.get(iter)) {
                // Add destination nodes to stack
                stack.push(edge.getTarget());
            }
        }
    }
}
