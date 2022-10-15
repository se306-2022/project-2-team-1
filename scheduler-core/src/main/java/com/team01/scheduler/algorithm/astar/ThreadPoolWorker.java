package com.team01.scheduler.algorithm.astar;

public class ThreadPoolWorker implements Runnable {

    private AStarScheduler bs;
    private AStarScheduler.State state;
    private PartialSolution ps;

    public ThreadPoolWorker(AStarScheduler bs, PartialSolution ps) {
        this.bs = bs;
        this.state = bs.getState();
        this.ps = ps;
    }

    @Override
    public void run() {
        bs.doBranchAndBoundRecursive(state, ps);
    }

    public PartialSolution getPartialSolution() {
        return this.ps;
    }
}
