package com.team01.scheduler.gui.views;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Region;

import java.util.function.Function;

/**
 * Container for a Canvas control which handles resizing and painting
 * whenever the available space changes.
 * <p>
 * See: https://stackoverflow.com/questions/68011270/how-to-make-canvas-fill-up-the-center-of-borderpane-in-javafx
 */
class CanvasPane extends Region {
    private final Canvas canvas;
    private final DrawFunction drawFunc;

    interface DrawFunction {
        void draw();
    }

    public CanvasPane(Canvas canvas, DrawFunction drawFunc) {
        this.canvas = canvas;
        this.drawFunc = drawFunc;
        getChildren().add(canvas);
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();

        canvas.setWidth(getWidth());
        canvas.setHeight(getHeight());
        drawFunc.draw();
    }
}
