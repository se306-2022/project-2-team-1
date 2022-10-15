package com.team01.scheduler.algorithm;

import com.team01.scheduler.visualizer.CumulativeTree;

public interface IUpdateVisualizer {
    void setCumulativeTree(CumulativeTree tree);
    void notifyFinished();
}
