package com.team01.scheduler.gui.views;

import com.team01.scheduler.visualizer.CumulativeTree;
import javafx.scene.paint.*;

public class PathLengthColorStrategy implements IColorStrategy {
    public Color getColor(int stateId, CumulativeTree.State state) {
        // Ensure the generated hues are at least 20deg apart
        double hue = (state.pathLength * 3) % 360;
        // System.out.println("Path Length: " + state.getPathLength() + " (hue: " + hue + "deg)");

        // See: https://mdigi.tools/random-bright-color/
        return Color.hsb(hue, 0.8, 1.0);
    }

    public Paint getBackdrop() {
        var stops = new Stop[]{
                new Stop(0, Color.rgb(15, 32, 39)),
                new Stop(0.5, Color.rgb(32, 58, 67)),
                new Stop(1, Color.rgb(44, 83, 100))
        };

        return new LinearGradient(0, 1, 0, 0, true, CycleMethod.NO_CYCLE, stops);
    }
}
