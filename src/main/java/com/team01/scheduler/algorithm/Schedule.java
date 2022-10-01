package com.team01.scheduler.algorithm;

import java.util.List;

public class Schedule {
    private int numProcessors;
    private List<ScheduledTask> scheduledTaskList;

    public void setShortestPath(int shortestPath) {
        this.shortestPath = shortestPath;
    }

    private int shortestPath;

    public Schedule(List<ScheduledTask> scheduledTaskList, int numProcessors) {
        this.numProcessors = numProcessors;
        this.scheduledTaskList = scheduledTaskList;
    }

    public int getNumProcessors() {
        return numProcessors;
    }

    public List<ScheduledTask> getScheduledTaskList() {
        return scheduledTaskList;
    }

    public int getShortestPath() {
        return shortestPath;
    }

}
