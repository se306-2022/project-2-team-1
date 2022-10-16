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
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.io.IOException;

/**
 * A dashboard controller which outlines the functions for
 * display statistics when the algorithm is run
 */
public class DashboardController {
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
    public VBox visualizerContainer;

    // Instance state
    private final TaskRunner taskRunner;
    private RadialTree<PathLengthColorStrategy> radialTree;
    private IRunnable runnable;

    public DashboardController(){
        taskRunner = new TaskRunner();
    }

    /**
     * Run task when a graph and algorithm is selected
     *
     * @param runnable              Run program
     * @param graphDescription      Description of graph
     * @param numProcessors         The number of processors to display along with scheduled tasks
     * @param numCores              The number of threads used to find optimal schedule
     * @param useVisualization      Toggle visualisation
     * @param completionVisualizer  The visualisation object
     */
    public void runWithTask(IRunnable runnable, String graphDescription, int numProcessors, int numCores, boolean useVisualization, ICompletionVisualizer completionVisualizer) {
        performRotationAnimation();

        this.runnable = runnable;

        if (useVisualization) {
            radialTree = new RadialTree<>(new PathLengthColorStrategy());
            VBox.setVgrow(radialTree, Priority.ALWAYS);
            visualizerContainer.getChildren().add(radialTree);
        }

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

    /**
     * Update status checks of dashboard
     */
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

    /**
     * Parses the graphviz string safely
     *
     * @param graphviz The graphviz String
     * @return Gets the graph associated with the graphviz to process in this controller
     */
    private Graph safeParseGraph(String graphviz) {
        try {
            var graphController = new GraphController(graphviz);
            return graphController.getGraph();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Rotate display modules on right side of dashboard
     */
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

    /**
     * Display memory used during application
     */
    private void displayMemory() {
        //To stop display from jumping numbers when idle, set the lowest memory
        //identifier to be MB
        double info [] = computeMemory();
        switch ((int) info[1]) {
            case 0: // bytes
                memoryTypeLabel.setText("MB");
                info[0] /= (1024 ^ 2);
                break;

            case 1: // kilobytes
                memoryTypeLabel.setText("MB");
                info[0] /= 1024;
                break;

            case 2: // megabytes
                memoryTypeLabel.setText("MB");
                break;

            case 3: // gigabytes
                memoryTypeLabel.setText("GB");
                break;

            default:
                memoryTypeLabel.setText("TOO MUCH MEM");
        }
        info[0] = twoDecimalRounding(info[0]);
        memoryNumberLabel.setText(String.valueOf(info[0]));

        //updateRotationSpeed(info[0]);

    }

    /**
     * Computes the memory used up by the application
     * @return A double info array containing the number of bytes and the byte multiplier
     */
    private double [] computeMemory() {

        var info = new double[2];

        double usedMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());

        double byteMultiplier = 0; // i.e. bytes

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

    /**
     * Rotation animation for circles, creates mechanised effect
     *
     * @param c         The circle object from FXML
     * @param reverse   Boolean variable to reverse rotation after cycle ends
     * @param angle     THe angle in which the circle rotates
     * @param duration  The duration of the rotation per cycle
     */
    private void setRotate(Circle c, boolean reverse, int angle, double duration) {

        RotateTransition rt = new RotateTransition(Duration.seconds(duration), c);

        rt.setAutoReverse(reverse);

        rt.setByAngle(angle);

        rt.setDelay(Duration.seconds(0));

        rt.setRate(3);

        rt.setCycleCount(Animation.INDEFINITE);

        rt.play();
    }

    /**
     * Displays the current shortest path of the algorithm
     */
    private void displayShortestPath() {
        shortestPath.setText(String.valueOf(runnable.getShortestPath()));
    }

    /**
     * Displays the number of whole solutions currently found
     */
    private void displayNumberOfSolutionsFound() {
        numberOfSolutions.setText(String.valueOf(runnable.getNumberSolutions()));
    }

}
