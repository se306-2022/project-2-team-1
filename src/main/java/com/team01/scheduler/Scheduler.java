package com.team01.scheduler;

import com.digraph.weighted.models.Edge;
import com.digraph.weighted.models.EdgesLinkedList;
import com.digraph.weighted.models.Graph;
import com.digraph.weighted.models.Node;

import java.util.Objects;

public class Scheduler {

    private Graph graph;

    public Graph getGraph() {
        return graph;
    }

    public Scheduler(Graph graph) {
        this.graph = graph;
    }

    public void scheduleNodes() {

        int iTime = 0;
        int jTime = 0;

        bool processor1 = true;

        for (Node node : graph.getNodes()) {

            if (processor1) {

                System.out.println("Node " + node.getName() + " being processed in processor 1 at time " + iTime);
                iTime >= jTime + node.getInteger();

            } else {

                System.out.println("Node " + node.getName() + " being processed in processor 2 at time " + jTime);
                jTime >= iTime + node.getInteger();
            }

        }

    }
}