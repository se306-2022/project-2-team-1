package com.team01.scheduler.gui.views;

import com.team01.scheduler.visualizer.CumulativeTree;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.*;

import java.util.*;

public class RadialTree extends StackPane {

    private GraphicsContext gc;
    private CumulativeTree tree;

    private final double CIRCLE_DISTANCE = 40;
    private final double COLOR_MULTIPLIER = 1234;
    private final double DEPTH_LIMIT = 6;

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
        Color getColor(CumulativeTree.State state);
        Paint getBackdrop();
    }

    class BrightColorStrategy implements ColorStrategy {
        public Color getColor(CumulativeTree.State state)
        {
            // Ensure the generated hues are at least 20deg apart
            double hue = state.hashCode() * COLOR_MULTIPLIER % 360;
            double discrete_hue = Math.round(hue/20.0f)*20;

            // See: https://mdigi.tools/random-bright-color/
            return Color.hsb(discrete_hue, 0.8, 1.0);
        }

        public Paint getBackdrop() {
            return Color.WHITESMOKE;
        }
    }

    class PathLengthColorStrategy implements ColorStrategy {
        public Color getColor(CumulativeTree.State state)
        {
            // Ensure the generated hues are at least 20deg apart
            double hue = (state.pathLength * 2) % 360;
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
        gc.save();

        var backdrop = colorStrategy.getBackdrop();
        gc.setFill(backdrop);

        gc.fillRect(0, 0, getWidth(), getHeight());

        gc.restore();
    }

    private void drawRadialTree(List<CumulativeTree.State> startStates) {

        Queue<CumulativeTree.State> queue = new LinkedList<>(startStates);

        long startTime, endTime;

        startTime = System.nanoTime();

        System.out.println("Start Render");

        int getOutTimer = 0;
        int getOutTimerCmp = 100000;

        gc.save();
        gc.translate(getWidth()/2, getHeight()/2);

        // BFS
        while (!queue.isEmpty()) {
            var state = queue.poll();
            var depth = state.depth;

            if (depth > DEPTH_LIMIT)
                break;

            if (getOutTimer > getOutTimerCmp)
                break;

            if (!state.dirty && !state.dirtyChild)
                break;

            var children = tree.outwardRelation.get(state);

            if (children == null)
                break;

            double startAngle;
            double endAngle;
            double parentX;
            double parentY;

            if (state.parent == null) {
                startAngle = 0.0f;
                endAngle = 360.0f;
                parentX = 0.0f;
                parentY = 0.0f;
            } else {
                startAngle = state.startAngle;
                endAngle = state.endAngle;
                parentX = state.xPos;
                parentY = state.yPos;
            }

            double range = endAngle - startAngle;
            double step = range / children.size();

            for (int i = 0; i < children.size(); i++) {
                var childState = children.get(i);

                if (childState.dirty) {
                    double childStartAngle = step * i + startAngle;
                    double childEndAngle = childStartAngle + step;
                    double childCentreAngle = (childEndAngle + childStartAngle) / 2;

                    var startXY = getCoordsForAngle(childStartAngle, depth);
                    var endXY = getCoordsForAngle(childEndAngle, depth);
                    var centreXY = getCoordsForAngle(childCentreAngle, depth);

                    var color = colorStrategy.getColor(childState);
                    gc.setFill(color);

                    gc.fillPolygon(
                            new double[]{parentX, startXY.x, endXY.x},
                            new double[]{parentY, startXY.y, endXY.y}, 3);

                    childState.startAngle = childStartAngle;
                    childState.endAngle = childEndAngle;
                    childState.xPos = centreXY.x;
                    childState.yPos = centreXY.y;
                }

                queue.add(childState);
                getOutTimer++;
            }

            state.dirty = false;
            // TODO: Find way to mark subtree as not dirty
            // Can we even do it with BFS??
        }

        endTime = System.nanoTime();

        System.out.println("Finish Render");
        System.out.println(" - Elapsed time: " + (endTime - startTime) / (1000f*1000f) + "ms");

        if (getOutTimer > getOutTimerCmp)
            System.out.println(" - Partial render (" + getOutTimer + " items)");

        gc.restore();
    }

    private void draw() {

        gc.clearRect(0, 0, getWidth(), getHeight());
        drawGradientBackdrop();



        System.out.println("Start Render");

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                // If we only have one root state (i.e. single start node)
                // then consider the subnodes only.
                /*if (list.size() == 1) {
                    list = tree.depthMap.get(1);
                    // depth = 1;
                }*/

                // var step = 360.0f / list.size();

                var list = tree.getStartStates();
                drawRadialTree(list);
            }
        };

        timer.start();

        /*var startAngle = 0.0f;
        for (var stateId : list) {
            var state = tree.stateMap.get(stateId);
            drawRecursive(state, stateId, depth, 0, 0, startAngle, startAngle + step);
            startAngle += step;
        }*/

        if (tree.depthMap.size() > DEPTH_LIMIT) {
            var leaves = tree.depthMap.get(tree.depthMap.size()-1);
            System.out.println("Number of Leaves: " + leaves.size());
        }
    }
}
