package com.team01.scheduler;

import com.team01.scheduler.algorithm.IRunnable;

public class Invocation {
    public IRunnable runnable;
    public String inputFileName;
    public String outputFileName;
    public int numProcessors;
    public int numCores;
    public boolean useVisualization;
}
