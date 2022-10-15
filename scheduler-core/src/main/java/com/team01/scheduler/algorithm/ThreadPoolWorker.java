<<<<<<< Updated upstream:scheduler-core/src/main/java/com/team01/scheduler/algorithm/ThreadPoolWorker.java
package com.team01.scheduler.algorithm;
=======
package com.team01.scheduler.algorithm.branchandbound;

import com.team01.scheduler.algorithm.branchandbound.PartialSolution;
>>>>>>> Stashed changes:scheduler-core/src/main/java/com/team01/scheduler/algorithm/branchandbound/ThreadPoolWorker.java

public class ThreadPoolWorker implements Runnable {

    private BranchAndBound bs;
    private BranchAndBound.State state;
    private PartialSolution ps;

    public ThreadPoolWorker(BranchAndBound bs, PartialSolution ps) {
        this.bs = bs;
        this.state = bs.getState();
        this.ps = ps;
    }

    @Override
    public void run() {
        bs.doBranchAndBoundRecursive(state, ps);
    }
}
