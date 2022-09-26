package com.team01.scheduler.algorithm;

import com.team01.scheduler.graph.models.Edge;
import com.team01.scheduler.graph.models.EdgesLinkedList;
import com.team01.scheduler.graph.models.Graph;
import com.team01.scheduler.graph.models.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class BranchAndBound implements IRunnable {
    public BranchAndBound() {

    }

    @Override
    public String getTaskName() {
        return "Scheduler - DFS Branch and Bound";
    }

    class ScheduledTask {
        int startTime;
        int processorId;
        Node node;
        ScheduledTask parent;

        public ScheduledTask(ScheduledTask parent, int startTime, int processorId, Node node) {
            this.startTime = startTime;
            this.processorId = processorId;
            this.node = node;
            this.parent = parent;
        }

        @Override
        public String toString() {
            return processorId + " | " + startTime + " | " + node.getName();
        }
    }

    @Override
    public void run(Graph graph) {

        Map<Node, EdgesLinkedList> map = graph.getGraph();

        int numProcessors = 2;
        int shortestPathLength = Integer.MAX_VALUE;
        ScheduledTask shortestPathEndTask = null;

        var startNode = graph.getNodes().get(0);
        var stack = new Stack<ScheduledTask>();

        // Add children to DFS solution tree
        for (int processorId = 0; processorId < numProcessors; processorId++) {
            stack.push(new ScheduledTask(null, 0, processorId, startNode));
        }

        while (!stack.isEmpty()) {
            ScheduledTask task = stack.pop();
            Node iter = task.node;
            int currentProcessorId = task.processorId;

            // Visit the node
            int pathLength = task.startTime + task.node.getValue();
            System.out.println("Visiting node " + task.node.getName() + " with path length " + pathLength);

            // Update shortest path
            if (map.get(iter).size() == 0) {
                if (pathLength < shortestPathLength) {
                    shortestPathLength = pathLength;
                    shortestPathEndTask = task;
                }
            }

            // Generate N+1 solution

            // For each child node
            for (Edge edge : map.get(iter)) {
                Node child = edge.getTarget();
                int communicationWeight = edge.getWeight();

                // For each processor
                for (int processorId = 0; processorId < numProcessors; processorId++) {
                    // Account for communication weight if on different processor
                    if (processorId == currentProcessorId) {
                        stack.push(new ScheduledTask(task, pathLength, processorId, child));
                    } else {
                        stack.push(new ScheduledTask(task, pathLength + communicationWeight, processorId, child));
                    }
                }
            }
        }

        // Shortest path:
        System.out.println("Shortest Path Length: " + shortestPathLength);
        while (shortestPathEndTask != null) {
            System.out.println(shortestPathEndTask);
            shortestPathEndTask = shortestPathEndTask.parent;
        }
    }
}
