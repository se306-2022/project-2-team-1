package com.team01.scheduler.gui.views;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class RadialTree extends Pane {

    private GraphicsContext gc;

    public RadialTree() {
        var canvas = new Canvas();
        var pane = new CanvasPane(canvas, this::draw);
        getChildren().add(pane);

        this.gc = canvas.getGraphicsContext2D();
    }

    private void draw() {
        gc.setFill(Color.BURLYWOOD);
        gc.clearRect(0, 0, getWidth(), getHeight());
    }
}
