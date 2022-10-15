package com.team01.scheduler.gui.views;

import com.team01.scheduler.algorithm.IUpdateVisualizer;
import com.team01.scheduler.visualizer.CumulativeTree;
import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.*;
import javafx.util.Pair;

import java.util.*;

public class RadialTree extends StackPane implements IUpdateVisualizer {

    private GraphicsContext gc;
    private CumulativeTree tree;

    private final double CIRCLE_DISTANCE = 40;
    private final double COLOR_MULTIPLIER = 1234;
    private final double DEPTH_LIMIT = 6;
    private final int FRAME_BUDGET = 10000;
    private int frameCounter = 0;
    private boolean algorithmTerminated = false;

    public RadialTree() {
        var canvas = new Canvas();
        var pane = new CanvasPane(canvas, this::draw);
        getChildren().add(pane);

        this.gc = canvas.getGraphicsContext2D();
    }

    @Override
    public void setCumulativeTree(CumulativeTree tree) {
        this.tree = tree;
        draw();
    }

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
            // System.out.println("Path Length: " + state.getPathLength() + " (hue: " + hue + "deg)");

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

    enum RecursiveResult {
        DONE,
        GET_OUT,
        NO_DRAW,
    }

    private RecursiveResult drawRecursive(CumulativeTree.State state, int stateId, int depth, double parentX, double parentY, double startRangeAngle, double endRangeAngle) {

        // Get Out Clause
        if (frameCounter > FRAME_BUDGET)
            return RecursiveResult.GET_OUT;

        if (depth > DEPTH_LIMIT)
            return RecursiveResult.NO_DRAW;

        if (!state.dirty)
            return RecursiveResult.NO_DRAW;

        List<Pair<Integer, CumulativeTree.State>> children = new ArrayList<>();

        // Ugly synchronised access
        // I miss opengl :(
        synchronized (tree) {
            var childrenIds = tree.outwardRelation.get(stateId);
            for (Integer childStateId : childrenIds) {
                var childState = tree.stateMap.get(childStateId);
                children.add(new Pair<>(childStateId, childState));
            }
        }

        // TODO: Clear dirty sectors before redrawing (to avoid artifacts)

        if (!children.isEmpty()) {
            double range = endRangeAngle - startRangeAngle;
            double step = range / children.size();

            for (int i = 0; i < children.size(); i++) {
                var childStateId = children.get(i).getKey();
                var childState = children.get(i).getValue();

                double childStartAngle = step * i + startRangeAngle;
                double childEndAngle = childStartAngle + step;
                double childCentreAngle = (childEndAngle + childStartAngle) / 2;
                var centreXY = getCoordsForAngle(childCentreAngle, depth);

                if (childState.dirty) {
                    frameCounter++;

                    var startXY = getCoordsForAngle(childStartAngle, depth);
                    var endXY = getCoordsForAngle(childEndAngle, depth);

                    var color = colorStrategy.getColor(childStateId, childState);
                    gc.setFill(color);

                    // gc.fillOval(x, y, 8, 8);
                    gc.fillPolygon(
                            new double[] {parentX, startXY.x, endXY.x },
                            new double[] {parentY, startXY.y, endXY.y}, 3);
                }

                var result = drawRecursive(childState, childStateId, depth + 1, centreXY.x, centreXY.y, childStartAngle, childEndAngle);

                if (result == RecursiveResult.GET_OUT)
                    return RecursiveResult.GET_OUT;
            }
        }

        state.dirty = false;

        return RecursiveResult.DONE;
    }

    @Override
    public void notifyFinished() {
        this.algorithmTerminated = true;
    }

    private void draw() {

        if (this.tree == null)
            return;

        gc.save();
        gc.clearRect(0, 0, getWidth(), getHeight());
        drawGradientBackdrop();
        gc.restore();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                long startTime, endTime;

                gc.save();

                startTime = System.nanoTime();

                gc.translate(getWidth()/2, getHeight()/2);

                System.out.println("Start Render");

                var depth = 0;
                var list = tree.getStartStates();

                frameCounter = 0;

                // If we only have one root state (i.e. single start node)
                // then consider the subnodes only.
                if (list.size() == 1) {
                    list = tree.depthMap.get(1);
                    depth = 1;
                }

                var step = 360.0f / list.size();

                boolean done = true;

                var startAngle = 0.0f;
                for (var stateId : list) {
                    var state = tree.stateMap.get(stateId);
                    var result = drawRecursive(state, stateId, depth, 0, 0, startAngle, startAngle + step);
                    startAngle += step;

                    if (result == RecursiveResult.GET_OUT)
                        done = false;
                }

                endTime = System.nanoTime();

                gc.restore();

                System.out.println("Finish Render");
                System.out.println(" - Elapsed time: " + (endTime - startTime) / (1000f*1000f) + "ms");

                if (frameCounter > FRAME_BUDGET)
                    System.out.println(" - Partial render (" + frameCounter + " items)");

                if (done && algorithmTerminated) {
                    System.out.println("\nDone");
                    this.stop();
                }
            }
        };

        timer.start();

        if (tree.depthMap.size() > DEPTH_LIMIT) {
            var leaves = tree.depthMap.get(tree.depthMap.size()-1);
            System.out.println("Number of Leaves: " + leaves.size());
        }
    }
}
