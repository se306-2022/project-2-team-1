package com.team01.scheduler;

import com.team01.scheduler.algorithm.INotifyCompletion;
import com.team01.scheduler.algorithm.IRunnable;
import com.team01.scheduler.algorithm.Schedule;
import com.team01.scheduler.graph.models.Graph;

public class TaskRunner {

    /**
     * Run a task (in the form of IRunnable) safely and report on
     * whether it was successful.
     *
     * @param runnable The task to run
     * @param graph The input graph
     * @param numProcessors
     * @return Status code of task
     */
    public Schedule safeRun(IRunnable runnable, Graph graph, int numProcessors) {

        System.out.println("Running task: " + runnable.getTaskName());

        System.out.println("\nTASK START\n");
        Schedule schedule;

        try {
            schedule = runnable.run(graph, numProcessors);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        System.out.println("\nTASK END\n\n");

        return schedule;
    }
    public void safeRunAsync(IRunnable runnable, Graph graph, INotifyCompletion notifyCompletion) {

        System.out.println("Running task: " + runnable.getTaskName());

        System.out.println("\nTASK START\n");

        try {
            notifyCompletion.notifyComplete(runnable.run(graph, 2));
        }
        catch (Exception e) {
            e.printStackTrace();

        }

        System.out.println("\nTASK END\n\n");

    }
}
