package com.team01.scheduler.gui.views;

import com.team01.scheduler.algorithm.Schedule;
import com.team01.scheduler.algorithm.ScheduledTask;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Task;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.util.function.Consumer;

public class ScheduleView extends GridPane {

    private Schedule schedule;
    private GraphicsContext gc;
    private ScrollBar horizontalScroll;
    private ScrollBar verticalScroll;
    private int totalWidth;
    private int totalHeight;
    private float step = 1.0f;

    private static final int colWidth = 100;
    private static final int colHeight = 35;
    private static final int padding = 40;


    /**
     * Calculate necessary view width from schedule
     * @param schedule Schedule to display
     * @return Required minimum width
     */
    private static int calculateViewWidth(Schedule schedule) {
        return colWidth * schedule.getNumProcessors() + padding*2;
    }

    /**
     * Calculates the most recent task from a given schedule
     * @param schedule Schedule to consider
     * @return ScheduledTask in the schedule
     */
    private static ScheduledTask getLatestTask(Schedule schedule) {
        int latestTime = 0;
        ScheduledTask latestTask = null;

        // Iterate over scheduled tasks to get the latest task end time
        for (var task : schedule.getScheduledTaskList()) {
            // Calculate end time of task
            var taskEndTime = task.getStartTime() + task.getWorkTime();

            // Update latest time
            if (taskEndTime > latestTime) {
                latestTime = taskEndTime;
                latestTask = task;
            }
        }

        return latestTask;
    }

    /**
     * Calculate necessary view height from schedule
     * @param schedule Schedule to display
     * @return Required minimum height
     */
    private static int calculateViewHeight(Schedule schedule, float step) {
        var task = getLatestTask(schedule);
        var latestTime = task.getStartTime() + task.getWorkTime();

        return (int) ((latestTime / step) * colHeight) + padding*2;
    }

    /**
     * Update the horizontal scroll according to available space
     * @param observable not used
     */
    private void updateHorizontalScroll(Observable observable) {
        totalWidth = calculateViewWidth(schedule);
        horizontalScroll.setMax(totalWidth);
        horizontalScroll.setVisibleAmount(getWidth());
    }

    /**
     * Update the vertical scroll according to available space
     * @param observable not used
     */
    private void updateVerticalScroll(Observable observable) {
        totalHeight = calculateViewHeight(schedule, step);
        verticalScroll.setMax(totalHeight);
        verticalScroll.setVisibleAmount(getHeight());
    }

    // See: https://stackoverflow.com/questions/68011270/how-to-make-canvas-fill-up-the-center-of-borderpane-in-javafx
    private class CanvasPane extends Region {
        private Canvas canvas;

        public CanvasPane(Canvas canvas) {
            this.canvas = canvas;
            getChildren().add(canvas);
        }

        @Override
        protected void layoutChildren() {
            super.layoutChildren();

            canvas.setWidth(getWidth());
            canvas.setHeight(getHeight());
            draw();
        }
    }

    public void zoomIn() {
        step = Math.max(step / 2, 0.125f);
        System.out.println(step);
        updateVerticalScroll(null);
        updateHorizontalScroll(null);
        draw();
    }

    public void zoomOut() {
        step = Math.min(step * 2, 512.0f);

        System.out.println(step);
        updateVerticalScroll(null);
        updateHorizontalScroll(null);
        draw();
    }

    public ScheduleView(Schedule schedule) {

        if (schedule == null)
            throw new IllegalArgumentException("Must provide a valid schedule");

        this.schedule = schedule;

        totalWidth = calculateViewWidth(schedule);
        totalHeight = calculateViewHeight(schedule, step);

        // See: https://stackoverflow.com/questions/35327437/javafx-navigating-through-a-canvas-within-a-scrollpane-of-equal-dimensions
        horizontalScroll = new ScrollBar();
        verticalScroll = new ScrollBar();
        horizontalScroll.setOrientation(Orientation.HORIZONTAL);
        verticalScroll.setOrientation(Orientation.VERTICAL);

        widthProperty().addListener(this::updateHorizontalScroll);
        heightProperty().addListener(this::updateVerticalScroll);
        updateHorizontalScroll(null);
        updateVerticalScroll(null);

        // Add to Grid Pane
        var canvas = new Canvas();
        var canvasPane = new CanvasPane(canvas);
        addColumn(0, canvasPane, horizontalScroll);
        add(verticalScroll, 1, 0);

        this.gc = canvas.getGraphicsContext2D();

        // See: https://docs.oracle.com/javase/8/javafx/api/javafx/scene/layout/GridPane.html
        ColumnConstraints column1 = new ColumnConstraints(100,100,Double.MAX_VALUE);
        column1.setHgrow(Priority.ALWAYS);
        getColumnConstraints().addAll(column1);

        RowConstraints row1 = new RowConstraints(100, 100, Double.MAX_VALUE);
        row1.setVgrow(Priority.ALWAYS);
        getRowConstraints().addAll(row1);

        horizontalScroll.valueProperty().addListener(x -> draw());
        verticalScroll.valueProperty().addListener(x -> draw());

        canvas.setHeight(400);
        canvas.setWidth(400);
        setVgrow(canvas, Priority.ALWAYS);
        setHgrow(canvas, Priority.ALWAYS);

        draw();
    }

    private void draw() {
        int numProcessors = schedule.getNumProcessors();
        var taskList = schedule.getScheduledTaskList();

        var lastTask = getLatestTask(schedule);
        int endTime = lastTask.getStartTime() + lastTask.getWorkTime();

        int offset = 15;
        endTime += step;

        var canvas = gc.getCanvas();

        gc.save();
        gc.setStroke(Color.BLACK);

        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.translate(0, -verticalScroll.getValue());

        gc.translate(padding, padding);
        gc.setTextBaseline(VPos.CENTER);

        // Draw Headers
        gc.translate(offset, 0);
        gc.setTextAlign(TextAlignment.CENTER);
        for (int i = 0; i < numProcessors; i++) {
            gc.fillText("Processor " + i, i * colWidth + colWidth/2, 0);
        }
        gc.translate(-offset, 0);

        // Draw Time Scale
        gc.translate(0, offset);
        gc.setTextAlign(TextAlignment.RIGHT);
        for (float i = 0; i < endTime; i += step) {
            gc.fillText(String.valueOf(i), 0, (i/step)*colHeight);
        }
        gc.translate(0, -offset);

        gc.translate(offset, offset);

        // Draw Blocks
        gc.setTextAlign(TextAlignment.CENTER);
        for (var task : taskList) {
            float scaledStartTime = task.getStartTime() / step;
            float scaledWorkTime = task.getWorkTime() / step;

            float startX = task.getProcessorId() * colWidth;
            float startY = scaledStartTime * colHeight;
            float height = scaledWorkTime * colHeight;

            gc.setFill(Color.LIGHTBLUE);
            gc.setStroke(Color.WHITE);
            gc.fillRect(startX, startY, colWidth, height);
            gc.strokeRect(startX, startY, colWidth, height);

            gc.setFill(Color.BLACK);
            gc.fillText(task.getNode().getName(),
                    startX + colWidth/2f,
                    startY + height/2f);
        }

        // Draw Columns
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        for (int i = 0; i <= numProcessors; i++) {
            int x = i * colWidth;
            gc.strokeLine(x, 0, x, Math.max(totalHeight, getHeight()) - 2*padding);
        }

        gc.restore();
    }
}
