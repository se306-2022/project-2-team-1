package com.team01.scheduler.matrix.algorithm;

import java.util.Comparator;

public class CostFunctionComparator implements Comparator<PartialSchedule> {

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
