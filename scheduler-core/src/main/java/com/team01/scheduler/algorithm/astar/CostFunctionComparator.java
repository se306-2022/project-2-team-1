package com.team01.scheduler.algorithm.astar;

import com.team01.scheduler.matrix.algorithm.PartialSchedule;

import java.util.Comparator;

public class CostFunctionComparator implements Comparator<ThreadPoolWorker> {

    /**
     * Comparator is used to sort the partial schedules and return the one
     * with the lowest cost function value from the priority queue in the
     * A* star algorithm implementation.
     *
     * @param worker the first object to be compared.
     * @param other the second object to be compared.
     * @return
     */
    public int compare(ThreadPoolWorker worker, ThreadPoolWorker other) {
        var s1 = worker.getPartialSolution();
        var s2 = other.getPartialSolution();

        if (s1.getCostFunction() < s2.getCostFunction()) {
            return -1;
        } else if (s1.getCostFunction() > s2.getCostFunction()) {
            return 1;
        } else {
            if (s1.getNumberOfNodes() >= s2.getNumberOfNodes()) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}
