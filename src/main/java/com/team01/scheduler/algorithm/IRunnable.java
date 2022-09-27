package com.team01.scheduler.algorithm;

import com.team01.scheduler.graph.models.Graph;

public interface IRunnable {
    String getTaskName();
    void run(Graph graph, INotifyCompletion notifyCompletion);
}
