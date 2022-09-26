package com.team01.scheduler.algorithm;

import java.util.List;

public class Schedule {
    int numProcessors;
    List<ScheduledTask> scheduledTaskList;

    public Schedule(List<ScheduledTask> scheduledTaskList, int numProcessors) {
        this.numProcessors = numProcessors;
        this.scheduledTaskList = scheduledTaskList;
    }
}
