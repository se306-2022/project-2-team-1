package com.team01.scheduler.algorithm;

import com.team01.scheduler.graph.models.Graph;

public interface IRunnable {
    String getTaskName();
    Schedule run(Graph graph);

}
