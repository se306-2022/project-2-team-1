
package com.team01.scheduler.algorithm;

import com.team01.scheduler.visualizer.CumulativeTree;

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

    /**
     * Setter for shortest path
     * @param shortestPath
     */
    public void setShortestPath(int shortestPath) {
        this.shortestPath = shortestPath;
    }

    private int shortestPath;

    /**
     * Getter for shortest path
     * @return
     */
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

    /**
     * Getter for number of processors
     * @return number of processors
     */
    public int getNumProcessors() {
        return numProcessors;
    }

    /**
     * Getter for schedule task list
     * @return schedule task list
     */
    public List<ScheduledTask> getScheduledTaskList() {
        return scheduledTaskList;
    }
}
