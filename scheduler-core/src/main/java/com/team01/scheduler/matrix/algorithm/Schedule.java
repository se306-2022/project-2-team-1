
package com.team01.scheduler.matrix.algorithm;

import java.util.List;

/**
 * The class Schedule is responsible for keeping track of all tasks within a scheduled task list. The list adds
 * all tasks when the current shortest path is updated
 */
public class Schedule {
    private int numProcessors;
    private List<ScheduledTask> scheduledTaskList;

    /**
     * A constructor to create a new schedule whenever the shortest path is updated
     *
     * @param scheduledTaskList     The list containing all tasks
     * @param numProcessors         The number of processors used for the algorithm run
     */
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

    /**
     * Gets the path length by comparing each task's available start time in the list.
     *
     * @return  The path length of the schedule
     */
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
