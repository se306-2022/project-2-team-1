package com.team01.scheduler.matrix.algorithm;

import com.team01.scheduler.algorithm.ScheduledTask;
import com.team01.scheduler.graph.models.EdgesLinkedList;
import com.team01.scheduler.graph.models.Node;
import com.team01.scheduler.matrix.model.Graph;

import java.util.*;

public class AStarScheduler {
    /**
     * Adjacency Matrix based implementation for the A* star exhaustive search algorithm.
     * Iterates through each possible partial schedule for the given number of processors and
     * prunes ones which aren't in contention to be the shortest path based on the cost function
     * heuristic.
     */
    private Graph adjacencyMatrix;
    private PriorityQueue<PartialSchedule> priorityQueue = new PriorityQueue<>(new CostFunctionComparator());

    public AStarScheduler(Graph adjacencyMatrix){
        this.adjacencyMatrix = adjacencyMatrix;
    }

    // minheap priority queue which returns partial schedules with the lowest cost function/ lower bound value
    // how would we determine if it is a complete solution
        // if the last node in partial solution is a leaf
        // or if partial solution contains all the nodes in the adjacency matrix graph
    // what would be added initially into the pq, since there can be multiple entry nodes into the graph

    // we will need way to determine cost function for each partial schedule >> DONE
    // need to take into account nodes in a partial schedule are scheduled on different processors

    private final class State {
        final int numProcessors;
        final Map<Node, EdgesLinkedList> map;
        int currentShortestPath;
        ScheduledTask currentShortestPathTask;

        /**
         * Constructs a state object to keep track of the graph map along with the current shortest path
         * @param numProcessors     The number of processors that we declare for the task graph
         * @param map               The edge-node map object
         */
        private State(int numProcessors, Map<Node, EdgesLinkedList> map) {
            this.numProcessors = numProcessors;
            this.map = map;
            this.currentShortestPath = Integer.MAX_VALUE;
        }
    }


    /**
     * The partialSolution class consists of a partialSolution constructor
     * which creates new instances whenever a new schedule is discovered.
     */
    private final class PartialSolution {
        // Nodes which have already been visited
        private final List<com.team01.scheduler.matrix.model.Node> visitedChildren;

        // Map of Nodes and (Edge Weights, Earliest Start Times)
        private final Map<com.team01.scheduler.matrix.model.Node, List<com.team01.scheduler.matrix.algorithm.ScheduledTask>> queuedChildren;
        private final int[] processorBusyUntilTime;

        public Map<com.team01.scheduler.matrix.model.Node, List<com.team01.scheduler.matrix.algorithm.ScheduledTask>> getQueuedChildren() {
            return queuedChildren;
        }

        com.team01.scheduler.matrix.algorithm.ScheduledTask task;

        /**
         * Creates a new root-level partial schedule (i.e. first node)
         * @param task Root task of the schedule
         * @param numProcessors Number of processors
         */
        private PartialSolution(com.team01.scheduler.matrix.algorithm.ScheduledTask task, int numProcessors) {
            this.visitedChildren = new ArrayList<>();
            this.queuedChildren = new HashMap<>();
            this.processorBusyUntilTime = new int[numProcessors];
            this.task = task;

            // Set the initial 'busy' time for the first task
            processorBusyUntilTime[task.getProcessorId()] = task.getStartTime() + task.getWorkTime();

            // Add the current node to visited as an optimisation
            this.visitedChildren.add(task.getNode());
        }

        /**
         * Creates a child partial schedule with newTask as the N+1 task
         * @param parent Parent partial schedule
         * @param newTask New task to queue
         */
        private PartialSolution(PartialSolution parent, com.team01.scheduler.matrix.algorithm.ScheduledTask newTask) {
            this.visitedChildren = new ArrayList<>();
            this.visitedChildren.addAll(parent.visitedChildren);

            this.queuedChildren = new HashMap<>();
            for (var nodeDependencyPair : parent.queuedChildren.entrySet()) {
                var node = nodeDependencyPair.getKey();
                var dependencyList = nodeDependencyPair.getValue();

                // Clone list so it doesn't interfere between recursions
                var clonedList = new ArrayList<>(dependencyList);
                queuedChildren.put(node, clonedList);
            }

            this.processorBusyUntilTime = Arrays.copyOf(parent.processorBusyUntilTime, parent.processorBusyUntilTime.length);
            this.task = newTask;

            // Add the current node to visited as an optimisation
            this.visitedChildren.add(newTask.getNode());

            // Remove the current node from queued children to avoid infinite recursion
            this.queuedChildren.remove(newTask.getNode());
        }
    }

}
