package com.team01.scheduler;

import com.team01.scheduler.algorithm.INotifyCompletion;
import com.team01.scheduler.algorithm.IRunnable;
import com.team01.scheduler.graph.models.Edge;
import com.team01.scheduler.graph.models.Node;
import com.team01.scheduler.graph.models.Graph;

import java.util.Arrays;
import java.util.List;

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

        System.out.println("\nTASK START\n");

        try {
            runnable.run(graph, notifyCompletion);
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        System.out.println("\nTASK END\n\n");

        return true;
    }
}
