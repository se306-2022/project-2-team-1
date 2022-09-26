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
        return processorId + " | " + startTime + " | " + node.getName();
    }
}
