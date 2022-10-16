package com.team01.scheduler.algorithm.astar;

import com.team01.scheduler.algorithm.ScheduledTask;
import com.team01.scheduler.algorithm.matrixModels.Node;
import com.team01.scheduler.visualizer.CumulativeTree;

import java.util.*;

/**
 * The partialSolution class consists of a partialSolution constructor
 * which creates new instances whenever a new schedule is discovered.
 */
public class PartialSolution {

    // Nodes which have already been visited
    public ArrayList<Node> visitedChildren;

    private int costFunctionValue;
    private CostFunctionCalculator functionCalculator;
    // contains tasks that have been discovered but not processed
    public Map<Node, List<ScheduledTask>> queuedChildren;
    public int[] processorBusyUntilTime;

    public Map<Node, List<ScheduledTask>> getQueuedChildren() {
        return queuedChildren;
    }

    ScheduledTask task;

    int depth;
    int visualizerId;

    /**
     * Creates a new root-level partial schedule (i.e. first node)
     * @param task Root task of the schedule
     * @param numProcessors Number of processors
     */
    public PartialSolution(ScheduledTask task, int numProcessors) {
        this.visitedChildren = new ArrayList<>();
        this.queuedChildren = new HashMap<>();
        this.processorBusyUntilTime = new int[numProcessors];
        this.task = task;

        // Set the initial 'busy' time for the first task
        processorBusyUntilTime[task.processorId] = task.getStartTime() + task.getWorkTime();

        // Add the current node to visited as an optimisation
        this.visitedChildren.add(task.getNode());

        this.depth = CumulativeTree.INITIAL_DEPTH;

        functionCalculator = CostFunctionCalculator.getInstance();
        this.costFunctionValue = functionCalculator.findCostFunction(visitedChildren, task);

    }

    /**
     * Getter to get cost function value for the partial solution
     * @return int: cost function value
     */
    public int getCostFunction(){
        return this.costFunctionValue;
    }

    /**
     * Creates a child partial schedule with newTask as the N+1 task
     * @param parent Parent partial schedule
     * @param newTask New task to queue
     */
    public PartialSolution(PartialSolution parent, ScheduledTask newTask) {
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

    /**
     * Function to check if a partial solution object equals another
     * @param o: partial solution object
     * @return boolean: True if objects are the same, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PartialSolution that = (PartialSolution) o;
        return depth == that.depth && Objects.equals(visitedChildren, that.visitedChildren) && Objects.equals(queuedChildren, that.queuedChildren) && Arrays.equals(processorBusyUntilTime, that.processorBusyUntilTime) && Objects.equals(task, that.task);
    }

    /**
     * Function to generate the hashcode for a partial solution
     * @return hash code
     */
    @Override
    public int hashCode() {
        int result = Objects.hash(visitedChildren, queuedChildren, task, depth);
        result = 31 * result + Arrays.hashCode(processorBusyUntilTime);
        return result;
    }

    /**
     * Getter to get number of nodes in the partial solution
     * @return int: number of nodes
     */
    public int getNumberOfNodes() {
        return this.visitedChildren.size();
    }
}