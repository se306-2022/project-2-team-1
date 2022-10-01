package com.team01.scheduler.algorithm;

import com.team01.scheduler.graph.models.Node;

public class ScheduledTask {
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
        return getProcessorId() + " | " + getStartTime() + " | " + getNode().getName();
    }

    public int getStartTime() {
        return startTime;
    }

    public int getProcessorId() {
        return processorId;
    }

    public Node getNode() {
        return node;
    }

    public int getWorkTime() {
        return node.getValue();
    }
}
