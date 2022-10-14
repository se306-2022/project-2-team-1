package com.team01.scheduler.gui.views;

import com.team01.scheduler.visualizer.CumulativeTree;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.*;

import java.util.Random;

public class RadialTree extends StackPane {

    private GraphicsContext gc;
    private CumulativeTree tree;

    private final double CIRCLE_DISTANCE = 40;
    private final double COLOR_MULTIPLIER = 1234;

    public RadialTree(CumulativeTree tree) {
        var canvas = new Canvas();
        var pane = new CanvasPane(canvas, this::draw);
        getChildren().add(pane);

        this.gc = canvas.getGraphicsContext2D();
        this.tree = tree;
    }

    static Random random = new Random();

    static class Point {
        double x;
        double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    interface ColorStrategy {
        Color getColor(int stateId, CumulativeTree.State state);
        Paint getBackdrop();
    }

    class BrightColorStrategy implements ColorStrategy {
        public Color getColor(int stateId, CumulativeTree.State state)
        {
            // Ensure the generated hues are at least 20deg apart
            double hue = stateId * COLOR_MULTIPLIER % 360;
            double discrete_hue = Math.round(hue/20.0f)*20;

            // See: https://mdigi.tools/random-bright-color/
            return Color.hsb(discrete_hue, 0.8, 1.0);
        }

        public Paint getBackdrop() {
            return Color.WHITESMOKE;
        }
    }

    class PathLengthColorStrategy implements ColorStrategy {
        public Color getColor(int stateId, CumulativeTree.State state)
        {
            // Ensure the generated hues are at least 20deg apart
            double hue = (state.getPathLength() * 3) % 360;
            System.out.println("Path Length: " + state.getPathLength() + " (hue: " + hue + "deg)");

            // See: https://mdigi.tools/random-bright-color/
            return Color.hsb(hue, 0.8, 1.0);
        }

        public Paint getBackdrop() {
            var stops = new Stop[] {
                    new Stop(0, Color.rgb(15, 32, 39)),
                    new Stop(0.5, Color.rgb(32, 58, 67)),
                    new Stop(1, Color.rgb(44, 83, 100))
            };

            return new LinearGradient(0, 1, 0, 0, true, CycleMethod.NO_CYCLE, stops);
        }
    }

    ColorStrategy colorStrategy = new PathLengthColorStrategy();

    private Point getCoordsForAngle(double angle, int depth) {
        double radians = Math.toRadians(angle);
        double x = Math.sin(radians) * CIRCLE_DISTANCE * depth;
        double y = Math.cos(radians) * CIRCLE_DISTANCE * depth;

        return new Point(x, y);
    }

    private void drawGradientBackdrop() {
        var backdrop = colorStrategy.getBackdrop();
        gc.setFill(backdrop);

        gc.fillRect(0, 0, getWidth(), getHeight());
    }

    private void drawRecursive(int stateId, int depth, double parentX, double parentY, double startRangeAngle, double endRangeAngle) {

        if (!tree.depthMap.containsKey(depth))
            return;

        var children = tree.outwardRelation.get(stateId);

        if (children == null)
            return;

        double range = endRangeAngle - startRangeAngle;
        double step = range / children.size();

        for (int i = 0; i < children.size(); i++) {
            var childStateId = children.get(i);
            var state = tree.stateMap.get(childStateId);

            double childStartAngle = step * i + startRangeAngle;
            double childEndAngle = childStartAngle + step;
            double childCentreAngle = (childEndAngle + childStartAngle) / 2;

            var startXY = getCoordsForAngle(childStartAngle, depth);
            var endXY = getCoordsForAngle(childEndAngle, depth);
            var centreXY = getCoordsForAngle(childCentreAngle, depth);

            var color = colorStrategy.getColor(childStateId, state);
            gc.setFill(color);

            // gc.fillOval(x, y, 8, 8);
            gc.fillPolygon(
                    new double[] {parentX, startXY.x, endXY.x },
                    new double[] {parentY, startXY.y, endXY.y}, 3);

            drawRecursive(childStateId, depth + 1, centreXY.x, centreXY.y, childStartAngle, childEndAngle);
        }
    }

    private void draw() {

        gc.save();

        gc.clearRect(0, 0, getWidth(), getHeight());
        drawGradientBackdrop();

        gc.translate(getWidth()/2, getHeight()/2);

        // System.out.println(tree.stateMap);
        System.out.println(tree.numSolutions);

        var depth = 0;
        var list = tree.getStartStates();

        // If we only have one root state (i.e. single start node)
        // then consider the subnodes only.
        if (list.size() == 1) {
            list = tree.depthMap.get(1);
            depth = 1;
        }

        var step = 360.0f / list.size();

        var startAngle = 0.0f;
        for (var stateId : list) {
            drawRecursive(stateId, depth, 0, 0, startAngle, startAngle + step);
            startAngle += step;
        }

        gc.restore();
    }
}
