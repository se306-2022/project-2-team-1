package com.team01.scheduler.algorithm;

public class AStarThreadPoolWorker implements Runnable{
    private AStar as;
    private AStar.State state;
    private PartialSolution ps;

    public AStarThreadPoolWorker(AStar as, PartialSolution ps) {
        this.as = as;
        this.state = as.getState();
        this.ps = ps;
    }

    @Override
    public void run() {
        as.doBranchAndBoundRecursive(state, ps);
    }
}
