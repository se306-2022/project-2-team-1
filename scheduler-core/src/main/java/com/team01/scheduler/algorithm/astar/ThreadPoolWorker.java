package com.team01.scheduler.algorithm.astar;

public class ThreadPoolWorker implements Runnable {

    private AStarScheduler bs;
    private AStarScheduler.State state;
    private PartialSolution ps;

    /**
     * Constructor
     * @param bs
     * @param ps
     */
    public ThreadPoolWorker(AStarScheduler bs, PartialSolution ps) {
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

    /**
     * Getter to get current partial solution
     * @return Partial Solution
     */
    public PartialSolution getPartialSolution() {
        return this.ps;
    }
}
