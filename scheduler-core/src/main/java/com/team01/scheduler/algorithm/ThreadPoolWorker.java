package com.team01.scheduler.algorithm;

public class ThreadPoolWorker implements Runnable {

    private BranchAndBound bs;
    private BranchAndBound.State state;
    private PartialSolution ps;

    public ThreadPoolWorker(BranchAndBound bs, PartialSolution ps) {
        this.bs = bs;
        this.state = bs.getState();
        this.ps = ps;

        // Add a lock on the phaser
        this.state.phaser.register();
    }

    @Override
    public void run() {
        bs.doBranchAndBoundRecursive(state, ps);

        // Remove lock for this task
        // Note child phasers will already have registered by this point
        this.state.phaser.arriveAndDeregister();
    }
}
