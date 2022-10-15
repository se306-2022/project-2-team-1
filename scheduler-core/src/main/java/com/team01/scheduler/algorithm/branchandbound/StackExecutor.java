package com.team01.scheduler.algorithm.branchandbound;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Stack;
import java.util.concurrent.*;

public class StackExecutor {

    private final BlockingDeque<Runnable> stack = new LinkedBlockingDeque<>();

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
                            var runnable = stack.takeLast();
                            runnable.run();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }
                    }
                });

                threads.add(thread);
                thread.start();
            }

            System.out.println("Running Stack Exectutor with " + threads.size() + " threads (expected: " + nThreads + ")");

            for (var thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void execute(Runnable runnable) {
        try {
            stack.putLast(runnable);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
