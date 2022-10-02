package com.team01.scheduler.gui.views;

import com.team01.scheduler.algorithm.Schedule;
import com.team01.scheduler.algorithm.ScheduledTask;
import javafx.concurrent.Task;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class ScheduleView extends Canvas {

    private Schedule schedule;
    private GraphicsContext gc;

    private static final int colWidth = 100;
    private static final int colHeight = 35;
    private static final int padding = 40;
    private static final float step = 1.0f;

    /**
     * Calculate necessary view width from schedule
     * @param schedule Schedule to display
     * @return Required minimum width
     */
    private static int calculateViewWidth(Schedule schedule) {
        return colWidth * schedule.getNumProcessors() + padding*2;
    }

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
    private static int calculateViewHeight(Schedule schedule) {
        var task = getLatestTask(schedule);
        var latestTime = task.getStartTime() + task.getWorkTime();

        return (int) ((latestTime / step) * colHeight) + padding*2;
    }

    public ScheduleView(Schedule schedule) {
        super(calculateViewWidth(schedule), calculateViewHeight(schedule));

        this.schedule = schedule;
        this.gc = getGraphicsContext2D();

        draw();
    }

    private void draw() {
        int numProcessors = schedule.getNumProcessors();
        var taskList = schedule.getScheduledTaskList();

        var lastTask = getLatestTask(schedule);
        int endTime = lastTask.getStartTime() + lastTask.getWorkTime();

        int offset = 15;
        endTime += step;

        gc.save();
        gc.setStroke(Color.BLACK);

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
        for (int i = 0; i < endTime; i += step) {
            gc.fillText(String.valueOf(i), 0, i*colHeight);
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
            gc.strokeLine(x, 0, x, getHeight() - 2*padding);
        }

        gc.restore();

        /*for (int i = 0; i < numProcessors; i++) {
            for (int j = 0; j < numCols; j++) {
                gc.strokeRect(i * colWidth, j * colHeight, colWidth, colHeight);
            }
        }*/
    }
}
