package com.team01.scheduler;

import com.team01.scheduler.algorithm.INotifyCompletion;
import com.team01.scheduler.algorithm.IRunnable;
import com.team01.scheduler.graph.models.Edge;
import com.team01.scheduler.graph.models.Node;
import com.team01.scheduler.graph.models.Graph;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;

public class TaskRunner {

    /**
     * Run a task (in the form of IRunnable) safely and report on
     * whether it was successful.
     *
     * @param runnable The task to run
     * @param graph The input graph
     * @return Status code of task
     */
    public boolean safeRun(IRunnable runnable, Graph graph, INotifyCompletion notifyCompletion) {

        System.out.println("Running task: " + runnable.getTaskName());

        long startTime, endTime;

        System.out.println("\nTASK START\n");

        try {
            startTime = System.nanoTime();
            runnable.run(graph, notifyCompletion);
            endTime = System.nanoTime();
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        System.out.println("\nTASK END\n");

        System.out.println("Task Execution Summary: ");
        System.out.println(" - Elapsed time: " + (endTime - startTime) / (1000f*1000f) + "ms");

        return true;
    }
}
