package com.team01.scheduler.gui.views;

import com.team01.scheduler.algorithm.Schedule;
import com.team01.scheduler.algorithm.ScheduledTask;
import javafx.beans.Observable;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

/**
 * ScheduleView displays a schedule in a table-like format. It handles sizing,
 * scrolling, painting, and other display features.
 */
public class ScheduleView extends GridPane {

    public static final int OFFSET = 15;
    private Schedule schedule;
    private GraphicsContext gc;
    private ScrollBar horizontalScroll;
    private ScrollBar verticalScroll;
    private int totalWidth;
    private int totalHeight;
    private float step = 1.0f;

    // Constants
    private static final int COL_WIDTH = 100;
    private static final int COL_HEIGHT = 35;
    private static final int PADDING = 40;
    public static final float MIN_STEP_SIZE = 0.125f;
    public static final float MAX_STEP_SIZE = 512.0f;


    /**
     * Calculate necessary view width from schedule
     * @param schedule Schedule to display
     * @return Required minimum width
     */
    private static int calculateViewWidth(Schedule schedule) {
        return COL_WIDTH * schedule.getNumProcessors() + PADDING *2;
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

        return (int) ((latestTime / step) * COL_HEIGHT) + PADDING *2;
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

    /**
     * Container for a Canvas control which handles resizing and painting
     * whenever the available space changes.
     *
     * See: https://stackoverflow.com/questions/68011270/how-to-make-canvas-fill-up-the-center-of-borderpane-in-javafx
     */
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

    /**
     * Decrease the step size i.e. zoom out
     */
    public void zoomIn() {
        step = Math.max(step / 2, MIN_STEP_SIZE);

        // Update scrollbars and draw
        updateVerticalScroll(null);
        updateHorizontalScroll(null);
        draw();
    }

    /**
     * Increase the step size i.e. zoom in
     */
    public void zoomOut() {
        step = Math.min(step * 2, MAX_STEP_SIZE);

        // Update scrollbars and draw
        updateVerticalScroll(null);
        updateHorizontalScroll(null);
        draw();
    }

    /**
     * Create a new schedule view control and display the provided schedule
     * @param schedule Schedule to display
     */
    public ScheduleView(Schedule schedule) {

        if (schedule == null)
            throw new IllegalArgumentException("Must provide a valid schedule");

        this.schedule = schedule;

        // Calculate content width based on schedule length (seconds) and num processors
        totalWidth = calculateViewWidth(schedule);
        totalHeight = calculateViewHeight(schedule, step);

        // Setup scrollbars
        // See: https://stackoverflow.com/questions/35327437/javafx-navigating-through-a-canvas-within-a-scrollpane-of-equal-dimensions
        horizontalScroll = new ScrollBar();
        verticalScroll = new ScrollBar();
        horizontalScroll.setOrientation(Orientation.HORIZONTAL);
        verticalScroll.setOrientation(Orientation.VERTICAL);

        // Resize scrollbars whenever the control resizes
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

        // Make the CanvasPane grow horizontally
        // See: https://docs.oracle.com/javase/8/javafx/api/javafx/scene/layout/GridPane.html
        ColumnConstraints column1 = new ColumnConstraints(100,100, Double.MAX_VALUE);
        column1.setHgrow(Priority.ALWAYS);
        getColumnConstraints().addAll(column1);

        // Make the CanvasPane grow vertically
        RowConstraints row1 = new RowConstraints(100, 100, Double.MAX_VALUE);
        row1.setVgrow(Priority.ALWAYS);
        getRowConstraints().addAll(row1);

        // Redraw whenever the scrollbars are used
        horizontalScroll.valueProperty().addListener(x -> draw());
        verticalScroll.valueProperty().addListener(x -> draw());

        // Set default size and grow properties
        canvas.setHeight(400);
        canvas.setWidth(400);
        setVgrow(canvas, Priority.ALWAYS);
        setHgrow(canvas, Priority.ALWAYS);

        // Draw the schedule
        draw();
    }

    /**
     * Drawing function. Draws a table for the schedule.
     */
    private void draw() {
        // Calculate information about the schedule
        int numProcessors = schedule.getNumProcessors();
        var taskList = schedule.getScheduledTaskList();

        var lastTask = getLatestTask(schedule);
        int endTime = lastTask.getStartTime() + lastTask.getWorkTime();

        // If the end time is not perfectly divisible by the time step, we
        // want to 'round up' so that the final task is within range.
        endTime += step;

        var canvas = gc.getCanvas();

        // Setup graphics context
        gc.save();
        gc.setStroke(Color.BLACK);

        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.translate(0, -verticalScroll.getValue());

        gc.translate(PADDING, PADDING);
        gc.setTextBaseline(VPos.CENTER);

        // Draw Headers
        gc.translate(OFFSET, 0);
        gc.setTextAlign(TextAlignment.CENTER);
        for (int i = 0; i < numProcessors; i++) {
            gc.fillText("Processor " + i, i * COL_WIDTH + COL_WIDTH /2, 0);
        }
        gc.translate(-OFFSET, 0);

        // Draw Time Scale
        gc.translate(0, OFFSET);
        gc.setTextAlign(TextAlignment.RIGHT);
        for (float i = 0; i < endTime; i += step) {
            gc.fillText(String.valueOf(i), 0, (i/step)* COL_HEIGHT);
        }
        gc.translate(0, -OFFSET);

        gc.translate(OFFSET, OFFSET);

        // Draw Blocks
        gc.setTextAlign(TextAlignment.CENTER);
        for (var task : taskList) {
            float scaledStartTime = task.getStartTime() / step;
            float scaledWorkTime = task.getWorkTime() / step;

            float startX = task.getProcessorId() * COL_WIDTH;
            float startY = scaledStartTime * COL_HEIGHT;
            float height = scaledWorkTime * COL_HEIGHT;

            gc.setFill(Color.LIGHTBLUE);
            gc.setStroke(Color.WHITE);
            gc.fillRect(startX, startY, COL_WIDTH, height);
            gc.strokeRect(startX, startY, COL_WIDTH, height);

            gc.setFill(Color.BLACK);
            gc.fillText(task.getNode().getName(),
                    startX + COL_WIDTH /2f,
                    startY + height/2f);
        }

        // Draw Columns
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        for (int i = 0; i <= numProcessors; i++) {
            int x = i * COL_WIDTH;
            gc.strokeLine(x, 0, x, Math.max(totalHeight, getHeight()) - 2* PADDING);
        }

        // Reset any transformations for next time
        gc.restore();
    }
}
