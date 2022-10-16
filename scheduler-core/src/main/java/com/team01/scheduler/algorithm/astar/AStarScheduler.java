package com.team01.scheduler.algorithm.astar;

import com.team01.scheduler.algorithm.*;
import com.team01.scheduler.algorithm.matrixModels.Node;
import com.team01.scheduler.algorithm.matrixModels.Graph;
import com.team01.scheduler.algorithm.matrixModels.exception.NodeInvalidIDMapping;
import com.team01.scheduler.matrix.algorithm.CostFunctionComparator;
import com.team01.scheduler.visualizer.CumulativeTree;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AStarScheduler implements IRunnable {
    /**
     * Default Constructor
     */
    public AStarScheduler() {
    }

    private int shortestPath;

    private AStarScheduler.State state;

    private PriorityQueueExecutor executor;

    @Override
    public String getTaskName() {
        return "Scheduler - BFS A-Star";
    }

    public AStarScheduler.State getState(){
        return state;
    }


    /**
     * A state class that keeps track of the current shortest path in the algorithm.
     */
    public static final class State {
        AtomicInteger numProcessors;
        final int[][] map;
        AtomicInteger currentShortestPath;
        final Graph graph;
        ScheduledTask currentShortestPathTask;
        CumulativeTree cumulativeTree;
        AtomicInteger solutionsConsidered;

        /**
         * Constructs a state object to keep track of the graph map along with the current shortest path
         * @param numProcessors     The number of processors that we declare for the task graph
         * @param map               The edge-node map object
         */
        public State(int numProcessors, int[][] map, Graph graph, CumulativeTree tree) {
            this.numProcessors = new AtomicInteger(numProcessors);
            this.map = map;
            this.currentShortestPath = new AtomicInteger(Integer.MAX_VALUE);
            this.solutionsConsidered = new AtomicInteger(0);
            this.graph = graph;
            this.cumulativeTree = tree;
        }
    }

    /**
     * Checks if the node of interest in the current solution has completed dependencies
     *
     * @param state     The state of the algorithm that keeps track of the current shortest path
     * @param current   The current partial solution that is checked
     * @param node      The current node that requires dependency checks
     * @return Returns true if dependencies have been visited
     */
    private boolean haveVisitedDependencies(AStarScheduler.State state, PartialSolution current, Node node) {

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
     * @return Returns the edge weight
     */
    private int getEdgeWeight(AStarScheduler.State state, Node source, Node target) {
        return state.map[source.getId()][target.getId()];
    }

    /**
     * Recursively solve for branch and bound, by splitting the search space
     * into smaller spaces.
     *
     * @param state     The state of the algorithm
     * @param current   The current partial solution
     */
    public void doBranchAndBoundRecursive(AStarScheduler.State state, PartialSolution current) {
        // Consider current node
        var task = current.task;
        int pathLength = calculateFinishTime(task);

        // Bound the algorithm by the currently determined shortest path
        /*CostFunctionCalculator functionCalculator = CostFunctionCalculator.getInstance();
        int projectedPathLength = functionCalculator.findCostFunction(current.visitedChildren,current.task,state.graph);
        if (projectedPathLength >= state.currentShortestPath.get())
            return;*/

        // Bound the algorithm by the currently determined shortest path
        if (pathLength >= state.currentShortestPath.get())
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

            if (pathLength < state.currentShortestPath.get()) {
                state.currentShortestPath.set(pathLength);
                state.currentShortestPathTask = task;

                // Notify success
                printPath(task);

                // Mark solution found
                state.solutionsConsidered.incrementAndGet();
            }
        }

        // Consider all queued children
        for (var childToQueue : current.queuedChildren.entrySet()) {

            // Consider all processors the child can be queued on
            for (int processorId = 0; processorId < state.numProcessors.get(); processorId++) {

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

                if (Math.max(pathLength, realStartTime + child.getComputationCost()) >= state.currentShortestPath.get())
                    continue;

                // Create new scheduled task
                var newTask = new ScheduledTask(current.task, realStartTime, processorId, child);

                // Add children to DFS solution tree
                var nextSolution = new PartialSolution(current, newTask);
                if (state.cumulativeTree != null) {
                    nextSolution.visualizerId = state.cumulativeTree.pushState(nextSolution.depth, pathLength + child.getComputationCost(), current.visualizerId);
                }
                nextSolution.processorBusyUntilTime[processorId] = realStartTime + child.getComputationCost();

                ThreadPoolWorker tw = new ThreadPoolWorker(this, nextSolution);

                // add to thread pool
                executor.execute(tw);
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

        // Start Timer
        long startTime = System.nanoTime();
        try{
            for (var startNode : graph.getEntryNodes()) {
                CostFunctionCalculator functionCalculator = CostFunctionCalculator.getInstance();
                functionCalculator.setGraph(graph);
                functionCalculator.setBottomLevel(startNode);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }


        // Obtain adjacency matrix
        int[][] map = graph.getAdjacencyMatrix();

        // Create thread pool
        executor = new PriorityQueueExecutor(numCores);

        // Setup state
        var cumulativeTree = (updateVisualizer != null)
                ? new CumulativeTree()
                : null;

        state = new AStarScheduler.State(numProcessors, map, graph, cumulativeTree);

        if (updateVisualizer != null)
            updateVisualizer.setCumulativeTree(state.cumulativeTree);

        // Queue a thread worker for each starting node
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

                var worker = new ThreadPoolWorker(this, ps);
                executor.execute(worker);
            }
        } catch (NodeInvalidIDMapping e) {
            throw new RuntimeException(e);
        }

        executor.runAndWait();

        // Report results
        List<ScheduledTask> taskList = new ArrayList<>();

        ScheduledTask iter = state.currentShortestPathTask;
        while (iter != null) {
            taskList.add(0, iter);
            iter = iter.parent;
        }

        var schedule = new Schedule(taskList, numProcessors);
        schedule.setShortestPath(shortestPath);

        // End Timer
        long endTime = System.nanoTime();

        // Print Time Taken
        long duration = (endTime - startTime);

        System.out.println("The algorithm took " + Duration.ofNanos(duration).toMillis() + " milliseconds");

        if (updateVisualizer != null)
            updateVisualizer.notifyFinished();

        if (completionVisualizer != null)
            completionVisualizer.setSchedule(schedule);

        return schedule;
    }

    /**
     * Getter to get the current shortest path
     * @return int: shortest path
     */
    @Override
    public int getShortestPath() {
        return state.currentShortestPath.get();
    }

    /**
     * Getter to get the number of solutions considered by the algorithm
     * @return int: number of solutions considered
     */
    @Override
    public int getNumberSolutions() {
        return state.solutionsConsidered.get();
    }
}
