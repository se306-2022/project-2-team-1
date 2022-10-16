package com.team01.scheduler.gui.views;

import com.team01.scheduler.TaskRunner;
import com.team01.scheduler.algorithm.ICompletionVisualizer;
import com.team01.scheduler.algorithm.IRunnable;
import com.team01.scheduler.algorithm.IUpdateVisualizer;
import com.team01.scheduler.algorithm.matrixModels.Graph;
import com.team01.scheduler.graph.models.GraphController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.io.IOException;

public class DashboardController {
    private TaskRunner taskRunner;

    @FXML
    private Circle c1;
    @FXML
    private Circle c2;
    @FXML
    private Circle c3;
    @FXML
    private Circle c4;
    @FXML
    private Circle c5;
    @FXML
    private Circle c6;
    @FXML
    private Circle c7;
    @FXML
    private Circle c8;
    @FXML
    private Circle c9;
    @FXML
    private Label memoryNumberLabel;
    @FXML
    private Label memoryTypeLabel;
    @FXML
    private Label shortestPath;
    @FXML
    private Label shortestPathLabel;
    @FXML
    private Label numberOfSolutions;
    @FXML
    private Label numberOfSolutionsLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private ProgressIndicator progressCircle1;

    @FXML
    private RadialTree<PathLengthColorStrategy> radialTree;
    private IRunnable runnable;

    public DashboardController(){
        taskRunner = new TaskRunner();
    }

    public void runWithTask(IRunnable runnable, String graphDescription, int numProcessors, int numCores, boolean useVisualization, ICompletionVisualizer completionVisualizer) {
        performRotationAnimation();

        this.runnable = runnable;

        //Updates display every time period stated in Keyframe(DURATION)
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), ev -> {
            displayMemory();
            displayNumberOfSolutionsFound();
            displayShortestPath();
        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        try {
            runTaskInternal(graphDescription, numProcessors, numCores, useVisualization, completionVisualizer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void runTaskInternal(String graphDescription, int numProcessors, int numCores, boolean useVisualization, ICompletionVisualizer externalCompletion) throws IOException {

        var task = new Task<>() {
            @Override
            public Void call() throws IOException, InterruptedException {
                updateMessage("Status");

                updateProgress(20, 100);
                updateMessage("Started Algorithm");

                if (runnable == null) {
                    System.err.println("No task selected");
                    return null;
                }

                var graph = safeParseGraph(graphDescription);

                if (graph == null) {
                    System.err.println("Could not parse graph");
                    return null;
                }

                updateProgress(40, 100);
                updateMessage("Algorithm in progress");

                // Run on completion
                ICompletionVisualizer internalCompletion = schedule -> {
                    updateMessage("Algorithm Completed");
                    updateProgress(85, 100);

                    externalCompletion.setSchedule(schedule);

                    updateMessage("Results Printed");
                    updateProgress(100, 100);
                };

                // Run the task
                IUpdateVisualizer updateVisualizer = useVisualization ? radialTree : null;
                taskRunner.safeRunAsync(runnable, graph, numProcessors, numCores, updateVisualizer, internalCompletion);

                return null;
            }
        };

        statusLabel.textProperty().bind(task.messageProperty());
        progressCircle1.progressProperty().bind(task.progressProperty());
        new Thread(task).start();
    }

    private Graph safeParseGraph(String graphviz) {
        try {
            var graphController = new GraphController(graphviz);
            return graphController.getGraph();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private void performRotationAnimation() {
        setRotate(c1, true, 360, 20);
        setRotate(c2, false, 360, 15);
        setRotate(c3, true, 360, 10);

        setRotate(c4, false, 360, 20);
        setRotate(c5, true, 360, 15);
        setRotate(c6, false, 360, 10);

        setRotate(c7, true, 360, 20);
        setRotate(c8, false, 360, 15);
        setRotate(c9, true, 360, 10);
    }

    private void displayMemory() {
        //To stop display from jumping numbers when idle, set the lowest memory
        //identifier to be MB
        double info [] = computeMemory();
        switch ((int) info[1]) {
            case 0:
                memoryTypeLabel.setText("MB");
                info[0] /= (1024 ^ 2);
                break;

            case 1:
                memoryTypeLabel.setText("MB");
                info[0] /= 1024;
                break;

            case 2:
                memoryTypeLabel.setText("MB");
                break;

            case 3:
                memoryTypeLabel.setText("GB");
                break;

            default:
                memoryTypeLabel.setText("TOO MUCH MEM");
        }
        info[0] = twoDecimalRounding(info[0]);
        memoryNumberLabel.setText(String.valueOf(info[0]));

        //updateRotationSpeed(info[0]);

    }

    private double [] computeMemory() {

        double info[] = new double[2];

        double usedMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024;

        double byteMultiplier = 0;

        while (usedMemory > 999) {
            usedMemory /= 1024;
            byteMultiplier++;
        }

        info[0] = usedMemory;
        info[1] = byteMultiplier;

        return info;
    }

    private double twoDecimalRounding(double number) {
        return Math.round(number * 100.0) / 100.0;
    }


    private void setRotate(Circle c, boolean reverse, int angle, double duration) {

        RotateTransition rt = new RotateTransition(Duration.seconds(duration), c);

        rt.setAutoReverse(reverse);

        rt.setByAngle(angle);

        rt.setDelay(Duration.seconds(0));

        rt.setRate(3);

        rt.setCycleCount(Animation.INDEFINITE);

        rt.play();
    }

    private void displayShortestPath() {
        shortestPath.setText(String.valueOf(runnable.getShortestPath()));
    }

    private void displayNumberOfSolutionsFound() {
        numberOfSolutions.setText(String.valueOf(runnable.getNumberSolutions()));
    }

}
