package com.team01.scheduler.algorithm.branchandbound;

import com.team01.scheduler.algorithm.*;
import com.team01.scheduler.graph.model.Node;
import com.team01.scheduler.graph.model.Graph;
import com.team01.scheduler.graph.exceptions.NodeInvalidIDMapping;
import com.team01.scheduler.visualizer.CumulativeTree;


import java.util.*;

public class BranchAndBoundSerial implements IRunnable {

    /**
     * Default Constructor
     */
    public BranchAndBoundSerial() {

    }
    private int shortestPath;

    State state;

    @Override
    public String getTaskName() {
        return "Scheduler - DFS Branch and Bound (Serial)";
    }

    /**
     * A state class that keeps track of the current shortest path in the algorithm.
     */
    private final class State {
        final int numProcessors;
        final int[][] map;
        int currentShortestPath;
        public int solutionsConsidered;
        final Graph graph;
        ScheduledTask currentShortestPathTask;
        CumulativeTree cumulativeTree;

        /**
         * Constructs a state object to keep track of the graph map along with the current shortest path
         * @param numProcessors     The number of processors that we declare for the task graph
         * @param map               The edge-node map object
         */
        private State(int numProcessors, int[][] map,Graph graph, CumulativeTree cumulativeTree) {
            this.numProcessors = numProcessors;
            this.map = map;
            this.currentShortestPath = Integer.MAX_VALUE;
            this.solutionsConsidered = 0;
            this.graph = graph;
            this.cumulativeTree = cumulativeTree;
        }
    }

    /**
     * The partialSolution class consists of a partialSolution constructor
     * which creates new instances whenever a new schedule is discovered.
     */
    private final class PartialSolution {
        // Nodes which have already been visited
        private final List<Node> visitedChildren;

        // contains tasks that have been discovered but not processed
        private final Map<Node, List<ScheduledTask>> queuedChildren;
        private final int[] processorBusyUntilTime;
        public int visualizerId;
        public int depth;

        public Map<Node, List<ScheduledTask>> getQueuedChildren() {
            return queuedChildren;
        }

        ScheduledTask task;

        /**
         * Creates a new root-level partial schedule (i.e. first node)
         * @param task Root task of the schedule
         * @param numProcessors Number of processors
         */
        private PartialSolution(ScheduledTask task, int numProcessors) {
            this.visitedChildren = new ArrayList<>();
            this.queuedChildren = new HashMap<>();
            this.processorBusyUntilTime = new int[numProcessors];
            this.task = task;

            // Set the initial 'busy' time for the first task
            processorBusyUntilTime[task.processorId] = task.getStartTime() + task.getWorkTime();

            // Add the current node to visited as an optimisation
            this.visitedChildren.add(task.getNode());

            this.depth = CumulativeTree.INITIAL_DEPTH;
        }

        /**
         * Creates a child partial schedule with newTask as the N+1 task
         * @param parent Parent partial schedule
         * @param newTask New task to queue
         */
        private PartialSolution(PartialSolution parent, ScheduledTask newTask) {
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

            this.depth = parent.depth + 1;
        }
    }

    /**
     * Checks if the node of interest in the current solution has completed dependencies
     *
     * @param state     The state of the algorithm that keeps track of the current shortest path
     * @param current   The current partial solution that is checked
     * @param node      The current node that requires dependency checks
     * @return
     */
    private boolean haveVisitedDependencies(State state, PartialSolution current, Node node) {

        for (Node dependencyNode : state.graph.getParentsForNode(node)){
            if (!current.visitedChildren.contains(dependencyNode))
                return false;
        }

        return true;
    }

    /**
     * Iterate over all scheduled tasks to get the path length. Note we have
     * to do this as the topmost task may start later but finish earlier and not
     * be the true path length.
     * @param iter Task to iterate on
     * @return Path length (i.e. finish time)
     */
    private int calculateFinishTime(ScheduledTask iter) {
        int latestTime = 0;

        // Iterate over scheduled tasks to get the latest task end time
        while (iter != null) {
            // Calculate end time of task
            var taskEndTime = iter.getStartTime() + iter.getWorkTime();

            // Update latest time
            if (taskEndTime > latestTime) {
                latestTime = taskEndTime;
            }

            iter = iter.parent;
        }

        return latestTime;
    }

    /**
     * Prints the schedule whenever a complete schedule is found and is the current shortest schedule
     *
     * @param task  The task. In this context, the important fields is the start time.
     */
    private void printPath(ScheduledTask task) {
        int pathLength = calculateFinishTime(task);

        System.out.println("New Shortest Path: " + pathLength);
        System.out.println("Processor ID | Start time | Node name:");
        while (task != null) {
            System.out.println(task);
            task = task.parent;
        }
        shortestPath = pathLength;
        System.out.println("End\n");
    }

    /**
     * Gets the weight of the edge
     *
     * @param state     The state of the algorithm
     * @param source    The source node
     * @param target    The target node
     * @return
     */
    private int getEdgeWeight(State state, Node source, Node target) {
        return state.map[source.getId()][target.getId()];
    }

    /**
     * Recursively solve for branch and bound, by splitting the search space
     * into smaller spaces.
     *
     * @param state     The state of the algorithm
     * @param current   The current partial solution
     */
    private void doBranchAndBoundRecursive(State state, PartialSolution current) {
        // Consider current node
        var task = current.task;
        int pathLength = calculateFinishTime(task);

        // Bound the algorithm by the currently determined shortest path
        if (pathLength >= state.currentShortestPath)
            return;

        // Add children of current node
        for (Node child : state.graph.getChildrenForNode(task.getNode())) {

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

            if (pathLength < state.currentShortestPath) {
                state.currentShortestPath = pathLength;
                state.currentShortestPathTask = task;

                // Notify success
                printPath(task);

                // Mark solution found
                state.solutionsConsidered++;
            }
        }

        // Consider all queued children
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
                    int finishTime = dependency.getStartTime() + dependency.getNode().getComputationCost();

                    // Account for communication time
                    if (dependency.getProcessorId() != processorId)
                        finishTime += getEdgeWeight(state, dependency.getNode(), child);

                    // Update earliest start time
                    earliestStartTime = Math.max(earliestStartTime, finishTime);
                }

                // Ensure earliestStartTime is not before the time when the processor is free
                int realStartTime = Math.max(earliestStartTime, current.processorBusyUntilTime[processorId]);

                // Create new scheduled task
                var newTask = new ScheduledTask(current.task, realStartTime, processorId, child);

                // Add children to DFS solution tree
                var nextSolution = new PartialSolution(current, newTask);
                if (state.cumulativeTree != null) {
                    nextSolution.visualizerId = state.cumulativeTree.pushState(nextSolution.depth, pathLength + child.getComputationCost(), current.visualizerId);
                }
                nextSolution.processorBusyUntilTime[processorId] = realStartTime + child.getComputationCost();
                // add to queue

                // fetch from queue add submit thread pool thread
                // thread calls branch and bound recursive
                doBranchAndBoundRecursive(state, nextSolution);
            }
        }
    }

    /**
     * Run the schedule using abstract IRunnable interface
     *
     * @param graph             The complete graph which has been deciphered from
     *                          the dot file
     * @param numProcessors     The number of processors that the algorithm will
     *                          allocate to
     *
     * @return                  Return the optimal schedule
     */
    @Override
    public Schedule run(Graph graph, int numProcessors, int numCores, IUpdateVisualizer updateVisualizer, ICompletionVisualizer completionVisualizer) {

        int[][] map = graph.getAdjacencyMatrix();

        var cumulativeTree = (updateVisualizer != null)
                ? new CumulativeTree()
                : null;

        State state = new State(numProcessors, map, graph, cumulativeTree);
        this.state = state;

        if (updateVisualizer != null)
            updateVisualizer.setCumulativeTree(state.cumulativeTree);

        try {
            for (Node n : graph.getEntryNodes()) {
                Map<Node, List<ScheduledTask>> queuedChildren = new HashMap<>();

                for (Node s : graph.getEntryNodes()) {
                    if (s != n){
                        queuedChildren.put(s, new ArrayList<>());
                    }
                }

                // Add children to DFS solution tree
                ScheduledTask newTask = new ScheduledTask(null, 0, 0, n);
                PartialSolution ps = new PartialSolution(newTask, numProcessors);
                if (state.cumulativeTree != null) {
                    ps.visualizerId = state.cumulativeTree.pushState(ps.depth, n.getComputationCost(), CumulativeTree.ROOT_ID);
                }
                ps.getQueuedChildren().putAll(queuedChildren);
                doBranchAndBoundRecursive(state, ps);
            }


            // Report results
            List<ScheduledTask> taskList = new ArrayList<>();

            ScheduledTask iter = state.currentShortestPathTask;
            while (iter != null) {
                taskList.add(0, iter);
                iter = iter.parent;
            }

            var schedule = new Schedule(taskList, numProcessors);
            schedule.setShortestPath(shortestPath);

            if (updateVisualizer != null)
                updateVisualizer.notifyFinished();

            if (completionVisualizer != null)
                completionVisualizer.setSchedule(schedule);

            return schedule;
        } catch (NodeInvalidIDMapping e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Getter to get the current shortest path
     * @return int: current shortest path
     */
    @Override
    public int getShortestPath() {
        return state.currentShortestPath;
    }

    /**
     * Getter to get the number of partial solutions considered
     * @return int: number of partial solutions considered
     */
    @Override
    public int getNumberSolutions() {
        return state.solutionsConsidered;
    }
}
