package com.team01.scheduler.matrix.algorithm;

import java.util.Comparator;

public class CostFunctionComparator implements Comparator<PartialSchedule> {

    /**
     * Comparator is used to sort the partial schedules and return the one
     * with the lowest cost function value from the priority queue in the
     * A* star algorithm implementation.
     *
     * @param s1 the first object to be compared.
     * @param s2 the second object to be compared.
     * @return
     */
    public int compare(PartialSchedule s1, PartialSchedule s2) {
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
