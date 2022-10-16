package com.team01.scheduler;

import com.team01.scheduler.algorithm.ICompletionVisualizer;
import com.team01.scheduler.algorithm.IRunnable;
import com.team01.scheduler.algorithm.IUpdateVisualizer;
import com.team01.scheduler.algorithm.Schedule;
import com.team01.scheduler.graph.model.Graph;

import java.util.concurrent.CompletableFuture;

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
    public Schedule safeRun(IRunnable runnable, Graph graph, int numProcessors, int numCores) {

        System.out.println("Running task: " + runnable.getTaskName());

        System.out.println("\nTASK START\n");
        Schedule schedule;

        //Try running the scheduler and catch exception if one is thrown
        try {
            schedule = runnable.run(graph, numProcessors, numCores, null, null);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        System.out.println("\nTASK END\n\n");

        return schedule;
    }
    public void safeRunAsync(IRunnable runnable, Graph graph, int numProcessors, int numCores, IUpdateVisualizer updateVisualizer, ICompletionVisualizer completionVisualizer) {

        System.out.println("Running task: " + runnable.getTaskName());

        //Try running the scheduler asynchronously and catch exception if one is thrown

        CompletableFuture.runAsync(() -> {
            System.out.println("\nTASK START\n");

            try {
                runnable.run(graph, numProcessors, numCores, updateVisualizer, completionVisualizer);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("\nTASK END\n\n");
        });

    }
}
