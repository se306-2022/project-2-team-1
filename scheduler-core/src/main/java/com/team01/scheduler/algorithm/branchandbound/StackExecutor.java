package com.team01.scheduler.algorithm.branchandbound;

import java.util.ArrayList;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class StackExecutor {

    private final BlockingDeque<ThreadPoolWorker> stack = new LinkedBlockingDeque<>();

    private final int nThreads;

    public StackExecutor(int nThreads) {
        this.nThreads = nThreads;
    }

    public void runAndWait() {
        try {
            var threads = new ArrayList<Thread>();

            for (int i = 0; i < nThreads; i++) {
                var thread = new Thread(() -> {
                    while (!stack.isEmpty()) {
                        try {
                            var runnable = stack.pollLast(1, TimeUnit.MILLISECONDS);

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

            System.out.println("Running Stack Executor with " + threads.size() + " threads (expected: " + nThreads + ")");

            for (var thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void execute(ThreadPoolWorker runnable) {
        try {
            stack.putLast(runnable);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
