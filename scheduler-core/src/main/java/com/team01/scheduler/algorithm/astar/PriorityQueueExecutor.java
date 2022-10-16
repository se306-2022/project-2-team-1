package com.team01.scheduler.algorithm.astar;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

public class PriorityQueueExecutor {

    private final PriorityBlockingQueue<ThreadPoolWorker> queue;

    private final int nThreads;

    public PriorityQueueExecutor(int nThreads) {
        this.nThreads = nThreads;
        this.queue = new PriorityBlockingQueue<>(1024, new CostFunctionComparator());
    }

    public void runAndWait() {
        try {
            var threads = new ArrayList<Thread>();

            for (int i = 0; i < nThreads; i++) {
                var thread = new Thread(() -> {
                    while (!queue.isEmpty()) {
                        try {
                            var runnable = queue.poll(1, TimeUnit.MILLISECONDS);

                            if (runnable == null)
                                continue;

                            runnable.run();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

                threads.add(thread);
                thread.start();
            }

            System.out.println("Running Priority Queue Executor with " + threads.size() + " threads (expected: " + nThreads + ")");

            for (var thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void execute(ThreadPoolWorker runnable) {
        queue.put(runnable);
    }
}
