package com.team01.scheduler.gui.views;

import com.team01.scheduler.visualizer.CumulativeTree;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.Random;

public class RadialTree extends StackPane {

    private GraphicsContext gc;
    private CumulativeTree tree;

    private final double CIRCLE_DISTANCE = 40;

    public RadialTree(CumulativeTree tree) {
        var canvas = new Canvas();
        var pane = new CanvasPane(canvas, this::draw);
        getChildren().add(pane);

        this.gc = canvas.getGraphicsContext2D();
        this.tree = tree;
    }

    static Random random = new Random();

    private Color getColor(int pathLength)
    {
        // See: https://mdigi.tools/random-bright-color/
        int hue = random.nextInt(360);
        return Color.hsb(hue, 0.8, 1.0);
    }

    static class Point {
        double x;
        double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    private Point getCoordsForAngle(double angle, int depth) {
        double radians = Math.toRadians(angle);
        double x = Math.sin(radians) * CIRCLE_DISTANCE * depth;
        double y = Math.cos(radians) * CIRCLE_DISTANCE * depth;

        return new Point(x, y);
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
            double childStartAngle = step * i + startRangeAngle;
            double childEndAngle = childStartAngle + step;
            double childCentreAngle = (childEndAngle + childStartAngle) / 2;

            var startXY = getCoordsForAngle(childStartAngle, depth);
            var endXY = getCoordsForAngle(childEndAngle, depth);
            var centreXY = getCoordsForAngle(childCentreAngle, depth);

            gc.setFill(getColor(0));
            // gc.fillOval(x, y, 8, 8);
            gc.fillPolygon(
                    new double[] {parentX, startXY.x, endXY.x },
                    new double[] {parentY, startXY.y, endXY.y}, 3);

            drawRecursive(children.get(i), depth + 1, centreXY.x, centreXY.y, childStartAngle, childEndAngle);
        }
    }

    private void draw() {
        gc.setFill(Color.BLANCHEDALMOND);
        gc.clearRect(0, 0, getWidth(), getHeight());
        // gc.fillRect(0, 0, getWidth(), getHeight());
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

    }

    private void drawOld() {
        gc.setFill(Color.BLANCHEDALMOND);
        gc.clearRect(0, 0, getWidth(), getHeight());
        gc.translate(getWidth()/2, getHeight()/2);

        // System.out.println(tree.stateMap);
        System.out.println(tree.numSolutions);

        double circle_distance = 40;
        for (var entry : tree.depthMap.entrySet()) {
            var depth = entry.getKey();
            var list = entry.getValue();

            double step = 360.0f / list.size();
            for (double i = 0; i < list.size(); i++) {
                double radians = Math.toRadians(step * i);
                double x = Math.sin(radians) * circle_distance * depth;
                double y = Math.cos(radians) * circle_distance * depth;

                gc.setFill(Color.BLUE);
                gc.fillOval(x, y, 10, 10);
            }
        }
    }
}
