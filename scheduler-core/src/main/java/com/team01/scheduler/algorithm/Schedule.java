
package com.team01.scheduler.algorithm;

import java.util.List;

public class Schedule {
    private int numProcessors;
    private List<ScheduledTask> scheduledTaskList;

    public Schedule(List<ScheduledTask> scheduledTaskList, int numProcessors) {
        this.numProcessors = numProcessors;
        this.scheduledTaskList = scheduledTaskList;
    }

    public void setShortestPath(int shortestPath) {
        this.shortestPath = shortestPath;
    }

    private int shortestPath;

    public int getShortestPath() {
        return shortestPath;
    }


    public int getPathLength() {

        int latestTime = 0;

        for (var task : scheduledTaskList) {
            var taskEndTime = task.getStartTime() + task.getWorkTime();

            if (taskEndTime > latestTime) {
                latestTime = taskEndTime;
            }
        }

        return latestTime;

    }

    public int getNumProcessors() {
        return numProcessors;
    }

    public List<ScheduledTask> getScheduledTaskList() {
        return scheduledTaskList;
    }
}
