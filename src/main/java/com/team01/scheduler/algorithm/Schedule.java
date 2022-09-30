package com.team01.scheduler.algorithm;

import java.util.List;

public class Schedule {
    private int numProcessors;
    private List<ScheduledTask> scheduledTaskList;

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
}
