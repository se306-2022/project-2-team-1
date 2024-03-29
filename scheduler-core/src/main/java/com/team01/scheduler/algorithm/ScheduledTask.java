package com.team01.scheduler.algorithm;


import com.team01.scheduler.graph.model.Node;

/**
 * Each task in this class has a processor ID associated with it as well as the node in question. Each
 * task also has the earliest start time.
 */
public class ScheduledTask {
    int startTime;
    public int processorId;
    Node node;
    public ScheduledTask parent;

    /**
     * The constructor to create ScheduledTask. Multiple instances are created
     * when looking at all child nodes in the queue.
     *
     * @param parent        The parent scheduled task preceding the current node
     * @param startTime     The available starting time of the current node
     * @param processorId   The processor associated with the current node
     * @param node          The current node
     */
    public ScheduledTask(ScheduledTask parent, int startTime, int processorId, Node node) {
        this.startTime = startTime;
        this.processorId = processorId;
        this.node = node;
        this.parent = parent;
    }

    /**
     * Formatter function to display progress of algorithm
     *
     * @return  The formatted output
     */
    @Override
    public String toString() {
        return "processorId: " + getProcessorId() + " | startTime: " + getStartTime() + " | node: " + getNode().getName();
    }

    /**
     * Getter for task start time
     * @return int: task time
     */
    public int getStartTime() {
        return startTime;
    }

    /**
     * Getter for processor ID
     * @return int: processor ID
     */
    public int getProcessorId() {
        return processorId;
    }

    /**
     * Getter for the node associated with the task
     * @return
     */
    public Node getNode() {
        return node;
    }

    /**
     * Getter for task work time
     * @return int: work time
     */
    public int getWorkTime() {
        return node.getComputationCost();
    }
}
