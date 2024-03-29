package com.team01.scheduler.algorithm.branchandbound;

import com.team01.scheduler.algorithm.branchandbound.PartialSolution;

public class ThreadPoolWorker implements Runnable {

    private BranchAndBound bs;
    private BranchAndBound.State state;
    private PartialSolution ps;

    /**
     * Constructor
     * @param bs
     * @param ps
     */
    public ThreadPoolWorker(BranchAndBound bs, PartialSolution ps) {
        this.bs = bs;
        this.state = bs.getState();
        this.ps = ps;
    }

    /**
     * Execute Algorithm for current partial solution
     */
    @Override
    public void run() {
        bs.doBranchAndBoundRecursive(state, ps);
    }
}
