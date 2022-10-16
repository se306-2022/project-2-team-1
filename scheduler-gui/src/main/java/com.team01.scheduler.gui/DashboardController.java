package com.team01.scheduler.gui;

import com.team01.scheduler.TaskRunner;
import com.team01.scheduler.algorithm.ICompletionVisualizer;
import com.team01.scheduler.algorithm.IRunnable;
import com.team01.scheduler.algorithm.IUpdateVisualizer;
import com.team01.scheduler.algorithm.Schedule;
import com.team01.scheduler.graph.model.Graph;
import com.team01.scheduler.graph.GraphController;
import com.team01.scheduler.graph.ExportToDotFile;
import com.team01.scheduler.gui.views.PathLengthColorStrategy;
import com.team01.scheduler.gui.views.RadialTree;
import com.team01.scheduler.gui.views.ScheduleView;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
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
    public VBox visualizerContainer;
    @FXML
    public VBox stagesVbox;
    @FXML
    public Label timeElapsed;
    @FXML
    public Button showScheduleBtn;

    // Instance state
    private final TaskRunner taskRunner;
    private RadialTree<PathLengthColorStrategy> radialTree;
    private IRunnable runnable;
    private long startingElapsedTime;
    private Schedule schedule = null;

    // Stages
    private final DashboardStage started = new DashboardStage("Started");
    private final DashboardStage parsing = new DashboardStage("Graph Parsing");
    private final DashboardProgressStage algorithm = new DashboardProgressStage("Algorithm");
    private final DashboardStage finished = new DashboardStage("Finished");
    private final DashboardStage printed = new DashboardStage("Presented");
    private final DashboardStage exported = new DashboardStage("Exported");

    private final DashboardStage[] requiredStages = {
            started,
            parsing,
            algorithm,
            finished,
            printed
    };

    public DashboardController(){
        taskRunner = new TaskRunner();
    }

    /**
     * Run task when a graph and algorithm is selected. Do not output a file
     *
     * @param runnable              Run program
     * @param graphDescription      Description of graph
     * @param numProcessors         The number of processors to display along with scheduled tasks
     * @param numCores              The number of threads used to find optimal schedule
     * @param useVisualization      Toggle visualisation
     * @param completionVisualizer  The visualisation object
     */
    public void runWithTask(IRunnable runnable, String graphDescription, int numProcessors, int numCores, boolean useVisualization, ICompletionVisualizer completionVisualizer) {
        runWithTask(runnable, graphDescription, numProcessors, numCores, useVisualization, completionVisualizer, null);
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
     * @param outputFileName        File to output
     */
    public void runWithTask(IRunnable runnable, String graphDescription, int numProcessors, int numCores, boolean useVisualization, ICompletionVisualizer completionVisualizer, String outputFileName) {
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
            displayElapsedTime();
        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        try {
            runTaskInternal(graphDescription, numProcessors, numCores, useVisualization, completionVisualizer, outputFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a new view for a given dashboard stage
     * @param stage stage to bind to
     */
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

    /**
     * Print an error to the console
     * @param error error to print
     */
    private void fail(String error) {
        System.err.println(error);
    }

    /**
     * Update status checks of dashboard
     */
    private void runTaskInternal(String graphDescription, int numProcessors, int numCores, boolean useVisualization, ICompletionVisualizer externalCompletion, String outputFileName) throws IOException {

        for (var stage : requiredStages)
            createViewForStage(stage);

        if (outputFileName != null)
            createViewForStage(exported);

        startingElapsedTime = System.currentTimeMillis();

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
                Platform.runLater(() -> {
                    algorithm.setProgress(1);
                    finished.setFinished(true);
                    showScheduleBtn.setDisable(false);
                });

                if (externalCompletion != null)
                    externalCompletion.setSchedule(schedule);
                this.schedule = schedule;

                Platform.runLater(() -> printed.setFinished(true));

                // export to dot file
                if (outputFileName != null) {
                    ExportToDotFile export = new ExportToDotFile(graph, outputFileName, schedule);
                    try {
                        export.writeDotWithSchedule();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                Platform.runLater(() -> exported.setFinished(true));

                Platform.runLater(() -> timeElapsed.setText("Time Taken: " + (System.currentTimeMillis() - startingElapsedTime) + "ms"));
            };

            // Run the task
            IUpdateVisualizer updateVisualizer = useVisualization ? radialTree : null;
            taskRunner.safeRunAsync(runnable, graph, numProcessors, numCores, updateVisualizer, internalCompletion);
        });

        thread.start();
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

    /**
     * Displays the current elapsed time
     */
    private void displayElapsedTime() {
        if (!printed.getFinished())
            timeElapsed.setText("Elapsed Time: " + (System.currentTimeMillis() - startingElapsedTime) + "ms");
    }

    public void showSchedule(MouseEvent mouseEvent) {
        if (schedule == null)
            return;

        // Scheduler View is a custom control which displays a schedule
        var schedulerView = new ScheduleView();
        schedulerView.setSchedule(schedule);
        VBox.setVgrow(schedulerView, Priority.ALWAYS);

        // Add zoom controls
        var zoomInButton = new Button("Zoom In");
        var zoomOutButton = new Button("Zoom Out");
        zoomInButton.setOnMouseClicked(x -> schedulerView.zoomIn());
        zoomOutButton.setOnMouseClicked(x -> schedulerView.zoomOut());

        // Add to toolbar
        var toolbar = new ToolBar(zoomInButton, zoomOutButton);

        var vbox = new VBox();
        vbox.getChildren().addAll(toolbar, schedulerView);

        // Create new tab
        var scene = new Scene(vbox, 600, 600);

        var stage = new Stage();
        stage.setTitle("Schedule");
        stage.setScene(scene);
        stage.show();
    }
}
