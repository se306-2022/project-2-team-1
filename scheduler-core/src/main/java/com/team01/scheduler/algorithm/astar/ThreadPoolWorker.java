package com.team01.scheduler.algorithm.astar;

import com.team01.scheduler.algorithm.PartialSolution;
import com.team01.scheduler.algorithm.astar.AStarScheduler;

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
}
