package com.team01.scheduler.algorithm;

import com.team01.scheduler.graph.models.Edge;
import com.team01.scheduler.graph.models.EdgesLinkedList;
import com.team01.scheduler.graph.models.Graph;
import com.team01.scheduler.graph.models.Node;
import javafx.util.Pair;

import java.util.*;

public class BranchAndBound implements IRunnable {
    public BranchAndBound() {

    }

    @Override
    public String getTaskName() {
        return "Scheduler - DFS Branch and Bound";
    }

    private final class State {
        final int numProcessors;
        final Map<Node, EdgesLinkedList> map;
        int currentShortestPath;
        ScheduledTask currentShortestPathTask;

        private State(int numProcessors, Map<Node, EdgesLinkedList> map) {
            this.numProcessors = numProcessors;
            this.map = map;
            this.currentShortestPath = Integer.MAX_VALUE;
        }
    }

    private final class PartialSolution {
        // Nodes which have already been visited
        // TODO: More efficient lookup structure (i.e. BST)
        private final List<Node> visitedChildren;

        // Map of Nodes and (Edge Weights, Earliest Start Times)
        private final Map<Node, List<ScheduledTask>> queuedChildren;
        private final int[] processorBusyUntilTime;

        ScheduledTask task;

        private PartialSolution(ScheduledTask task, int numProcessors) {
            this.visitedChildren = new ArrayList<>();
            this.queuedChildren = new HashMap<>();
            this.processorBusyUntilTime = new int[numProcessors];
            this.task = task;

            // Add the current node to visited as an optimisation
            this.visitedChildren.add(task.node);
        }

        private PartialSolution(PartialSolution clone, ScheduledTask newTask) {
            this.visitedChildren = new ArrayList<>();
            this.visitedChildren.addAll(clone.visitedChildren);

            this.queuedChildren = new HashMap<>();
            this.queuedChildren.putAll(clone.queuedChildren);

            this.processorBusyUntilTime = Arrays.copyOf(clone.processorBusyUntilTime, clone.processorBusyUntilTime.length);
            this.task = newTask;

            // Add the current node to visited as an optimisation
            this.visitedChildren.add(newTask.node);

            // Remove the current node from queued children to avoid infinite recursion
            this.queuedChildren.remove(newTask.node);
        }
    }

    private boolean haveVisitedDependencies(State state, PartialSolution current, Node node) {

        // This is awful - we'll probably need an adjacency matrix?
        for (EdgesLinkedList list : state.map.values()) {
            for (Edge edge : list) {
                if (edge.getTarget() == node) {
                    if (!current.visitedChildren.contains(edge.getSource()))
                        return false;
                }
            }
        }

        return true;
    }

    private void printPath(ScheduledTask iter) {
        int pathLength = iter.startTime + iter.node.getValue();
        System.out.println("New Shortest Path: " + pathLength);

        while (iter != null) {
            System.out.println(iter);
            iter = iter.parent;
        }

        System.out.println("End\n");
    }

    private int getEdgeWeight(State state, Node source, Node target) {
        EdgesLinkedList list = state.map.get(source);

        for (Edge edge : list) {
            if (edge.getTarget() == target)
                return edge.getWeight();
        }

        throw new RuntimeException("Edge not found");
    }

    private void doBranchAndBoundRecursive(State state, PartialSolution current) {
        // Consider current node
        var task = current.task;
        int nodeFinishTime = task.startTime + task.node.getValue();

        // Bound the algorithm by the currently determined shortest path
        if (nodeFinishTime >= state.currentShortestPath)
            return;

        // Add children of current node
        for (var edge : state.map.get(task.node)) {
            var child = edge.getTarget();

            // TODO: More efficient lookup
            // TODO: Branch and bound
            if (current.visitedChildren.contains(child))
                continue;

            // Add current task as a dependency
            if (current.queuedChildren.containsKey(child)) {
                current.queuedChildren.get(child).add(task);
            } else {
                var childList = new ArrayList<ScheduledTask>();
                childList.add(task);

                current.queuedChildren.put(child, childList);
            }
        }

        // If no more children are queued (i.e. done), then update
        // current shortestPath
        if (current.queuedChildren.size() == 0) {
            if (nodeFinishTime < state.currentShortestPath) {
                state.currentShortestPath = nodeFinishTime;
                state.currentShortestPathTask = task;

                // Notify success
                printPath(task);
            }
        }

        // Consider all queued children
        // TODO: Make sure to account for task being scheduled after earliest start time
        for (var childToQueue : current.queuedChildren.entrySet()) {

            // Consider all processors the child can be queued on
            for (int processorId = 0; processorId < state.numProcessors; processorId++) {

                int earliestStartTime = 0;

                // Get child information
                var child = childToQueue.getKey();
                var dependencies = childToQueue.getValue();

                // Ensure all dependency tasks have been completed
                if (!haveVisitedDependencies(state, current, child))
                    continue;

                // The task can start (at the earliest, assuming no communication time) once
                // all dependency tasks have been completed
                for (var dependency : dependencies) {
                    int finishTime = dependency.startTime + dependency.node.getValue();

                    // Account for communication time
                    if (dependency.processorId != processorId)
                        finishTime += getEdgeWeight(state, dependency.node, child);

                    // Update earliest start time
                    earliestStartTime = Math.max(earliestStartTime, finishTime);
                }

                // Ensure earliestStartTime is not before the time when the processor is free
                int realStartTime = Math.max(earliestStartTime, current.processorBusyUntilTime[processorId]);

                // Create new scheduled task
                var newTask = new ScheduledTask(current.task, realStartTime, processorId, child);

                // Add children to DFS solution tree
                var nextSolution = new PartialSolution(current, newTask);
                nextSolution.processorBusyUntilTime[processorId] = realStartTime + child.getValue();
                doBranchAndBoundRecursive(state, nextSolution);
            }
        }
    }

    @Override
    public void run(Graph graph, INotifyCompletion notifyCompletion) {

        var startNode = graph.getNodes().get(0);

        int numProcessors = 2;
        Map<Node, EdgesLinkedList> map = graph.getGraph();
        State state = new State(numProcessors, map);

        // Add children to DFS solution tree
        ScheduledTask newTask = new ScheduledTask(null, 0, 0, startNode);
        doBranchAndBoundRecursive(state, new PartialSolution(newTask, numProcessors));

        // Report results
        List<ScheduledTask> taskList = new ArrayList<>();

        ScheduledTask iter = state.currentShortestPathTask;
        while (iter != null) {
            taskList.add(0, iter);
            iter = iter.parent;
        }

        var schedule = new Schedule(taskList, numProcessors);
        notifyCompletion.notifyComplete(schedule);

        /*Map<Node, EdgesLinkedList> map = graph.getGraph();

        int numProcessors = 2;
        int shortestPathLength = Integer.MAX_VALUE;
        ScheduledTask shortestPathEndTask = null;

        var startNode = graph.getNodes().get(0);
        var stack = new Stack<ScheduledTask>();

        // var queuedChildrenBinaryTree;

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
        }*/
    }
}
