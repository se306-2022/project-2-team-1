package com.team01.scheduler.gui;

import com.team01.scheduler.TaskRunner;
import com.team01.scheduler.algorithm.ICompletionVisualizer;
import com.team01.scheduler.algorithm.IRunnable;
import com.team01.scheduler.algorithm.IUpdateVisualizer;
import com.team01.scheduler.algorithm.matrixModels.Graph;
import com.team01.scheduler.graph.models.GraphController;
import com.team01.scheduler.gui.views.PathLengthColorStrategy;
import com.team01.scheduler.gui.views.RadialTree;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.io.IOException;

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
    public VBox visualizerContainer;
    @FXML
    public VBox stagesVbox;

    // Instance state
    private final TaskRunner taskRunner;
    private RadialTree<PathLengthColorStrategy> radialTree;
    private IRunnable runnable;

    // Stages
    private final DashboardStage started = new DashboardStage("Started");
    private final DashboardStage parsing = new DashboardStage("Graph Parsing");
    private final DashboardProgressStage algorithm = new DashboardProgressStage("Algorithm");
    private final DashboardStage finished = new DashboardStage("Finished");
    private final DashboardStage printed = new DashboardStage("Printed");

    private final DashboardStage[] stages = {
            started,
            parsing,
            algorithm,
            finished,
            printed
    };

    public DashboardController(){
        taskRunner = new TaskRunner();
    }

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

    private void createViewForStage(DashboardStage stage) {

        var vbox = new VBox();
        vbox.setSpacing(5);
        vbox.getStyleClass().add("stageVbox");
        vbox.setOpacity(0.5);

        var label = new Label(stage.getTitle());
        label.getStyleClass().add("stageLabel");
        vbox.getChildren().add(label);

        // Adjust label state depending on finished property
        stage.finishedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                vbox.setOpacity(1.0);
            } else {
                vbox.setOpacity(0.5);
            }
        });

        // Add progress indicator only if needed
        if (stage instanceof DashboardProgressStage) {
            var progressStage = (DashboardProgressStage) stage;

            var indicator = new ProgressBar();
            indicator.prefWidthProperty().bind(vbox.widthProperty());
            indicator.progressProperty().bind(progressStage.progressProperty());
            vbox.getChildren().add(indicator);
        }

        stagesVbox.getChildren().add(vbox);
    }

    private void fail(String error) {
        System.err.println(error);
    }

    private void runTaskInternal(String graphDescription, int numProcessors, int numCores, boolean useVisualization, ICompletionVisualizer externalCompletion) throws IOException {

        for (var stage : stages)
            createViewForStage(stage);

        var thread = new Thread(() -> {
            Platform.runLater(() -> started.setFinished(true));

            var graph = safeParseGraph(graphDescription);

            if (graph == null) {
                fail("Could not parse graph");
                return;
            }

            Platform.runLater(() -> parsing.setFinished(true));
            Platform.runLater(() -> algorithm.setFinished(true));
            Platform.runLater(() -> algorithm.setProgress(-1));

            // Run on completion
            ICompletionVisualizer internalCompletion = schedule -> {
                Platform.runLater(() -> algorithm.setProgress(1));
                Platform.runLater(() -> finished.setFinished(true));

                externalCompletion.setSchedule(schedule);

                Platform.runLater(() -> printed.setFinished(true));
            };

            // Run the task
            IUpdateVisualizer updateVisualizer = useVisualization ? radialTree : null;
            taskRunner.safeRunAsync(runnable, graph, numProcessors, numCores, updateVisualizer, internalCompletion);
        });

        thread.start();
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
