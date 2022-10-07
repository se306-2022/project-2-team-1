package com.team01.scheduler.algorithm;


import com.team01.scheduler.algorithm.matrixModels.Graph;

public interface IRunnable {
    String getTaskName();
    Schedule run(Graph graph, int numProcessors);

}
