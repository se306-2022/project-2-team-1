package com.team01.scheduler.gui.views;

import com.team01.scheduler.visualizer.CumulativeTree;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class BrightColorStrategy implements IColorStrategy {

    private static final double COLOR_MULTIPLIER = 1234;

    public Color getColor(int stateId, CumulativeTree.State state) {
        // Ensure the generated hues are at least 20deg apart
        double hue = stateId * COLOR_MULTIPLIER % 360;
        double discrete_hue = Math.round(hue / 20.0f) * 20;

        // See: https://mdigi.tools/random-bright-color/
        return Color.hsb(discrete_hue, 0.8, 1.0);
    }

    /**
     * Get the backdrop colour of the dashboard
     *
     * @return Color.WHITESMOKE
     */
    public Paint getBackdrop() {
        return Color.WHITESMOKE;
    }
}
