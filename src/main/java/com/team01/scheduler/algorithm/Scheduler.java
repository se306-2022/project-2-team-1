package com.team01.scheduler.algorithm;

import com.digraph.weighted.models.Edge;
import com.digraph.weighted.models.EdgesLinkedList;
import com.digraph.weighted.models.Graph;
import com.digraph.weighted.models.Node;

import java.util.Objects;

public class Scheduler implements IRunnable {

    public Scheduler() {

    }

    @Override
    public String getTaskName() {
        return "Scheduling Algorithm";
    }

    @Override
    public void run(Graph graph) {

        Map<Node, EdgesLinkedList> map = graph.getGraph();
        Node startNode = graph.getNodes().get(0);

        if (!map.containsKey(startNode))
            throw new InvalidInputException("Starting node must be part of graph");

        int iTime = 0;
        int jTime = 0;

        bool processor1 = true;

        for (Node node : graph.getNodes()) {

            if (processor1) {

                System.out.println("Node " + node.getName() + " being processed in processor 1 at time " + iTime);
                iTime = jTime + node.getInteger();

            } else {

                System.out.println("Node " + node.getName() + " being processed in processor 2 at time " + jTime);
                jTime = iTime + node.getInteger();
            }

        }

    }
}