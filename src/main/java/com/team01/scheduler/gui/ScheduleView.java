package com.team01.scheduler.gui;

import com.team01.scheduler.algorithm.Schedule;
import com.team01.scheduler.algorithm.ScheduledTask;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.util.List;

public class ScheduleView extends Canvas {

    private Schedule schedule;
    private GraphicsContext gc;

    private static final int viewWidth = 400;
    private static final int viewHeight = 400;

    private static final int colWidth = 100;
    private static final int colHeight = 35;
    private static final int padding = 40;

    public ScheduleView(Schedule schedule) {
        super(viewWidth, viewHeight);

        this.schedule = schedule;
        this.gc = getGraphicsContext2D();

        draw();
    }

    private void draw() {
        int numProcessors = schedule.getNumProcessors();
        var taskList = schedule.getScheduledTaskList();

        var lastTask = taskList.get(taskList.size()-1);
        int endTime = lastTask.getStartTime() + lastTask.getWorkTime();

        int offset = 15;

        float step = 1.0f;
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
            gc.strokeLine(x, 0, x, viewHeight - 2*padding);
        }

        gc.restore();

        /*for (int i = 0; i < numProcessors; i++) {
            for (int j = 0; j < numCols; j++) {
                gc.strokeRect(i * colWidth, j * colHeight, colWidth, colHeight);
            }
        }*/
    }
}
