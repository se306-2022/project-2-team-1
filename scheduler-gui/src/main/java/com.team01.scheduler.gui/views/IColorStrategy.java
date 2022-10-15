package com.team01.scheduler.gui.views;

import com.team01.scheduler.visualizer.CumulativeTree;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

interface IColorStrategy {
    Color getColor(int stateId, CumulativeTree.State state);

    Paint getBackdrop();
}
