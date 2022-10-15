package com.team01.scheduler.gui;

import com.team01.scheduler.TaskRunner;
import com.team01.scheduler.Utils;
import com.team01.scheduler.algorithm.BranchAndBound;
import com.team01.scheduler.algorithm.ICompletionVisualizer;
import com.team01.scheduler.algorithm.Schedule;
import com.team01.scheduler.algorithm.matrixModels.Graph;
import com.team01.scheduler.graph.models.GraphController;
import com.team01.scheduler.gui.views.RadialTree;
import com.team01.scheduler.gui.views.ScheduleView;
import com.team01.scheduler.prototype.DepthFirstSearch;
import com.team01.scheduler.algorithm.IRunnable;
import com.team01.scheduler.gui.views.Console;
import com.team01.scheduler.visualizer.CumulativeTree;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MainController {
    private TaskRunner taskRunner;
    private ObservableList<IRunnable> taskList;
    private PrintStream stdOutput;
    private PrintStream stdError;
    private PrintStream consoleOutput;

    // State Variables
    private boolean useInternalConsole = false;

    @FXML
    private ListView<IRunnable> listView;

    @FXML
    private Console console;

    @FXML
    private SplitPane splitPane;

    @FXML
    private TabPane tabPane;

    @FXML
    public TextArea graphEditor;

    @FXML
    public Spinner<Integer> numProcessors;

    @FXML
    public Spinner<Integer> cpuCores;

    /**
     * Run a task using TaskRunner
     *
     * @param actionEvent ignored
     */
    @FXML
    public void onRunTask(ActionEvent actionEvent) {
        // Get runnable from list
        IRunnable runnable = listView.getSelectionModel().getSelectedItem();

        if (runnable == null) {
            System.err.println("No task selected");
            return;
        }

        // Get parameters from controls
        int processorCount = numProcessors.getValue();
        int coreCount = cpuCores.getValue();
        String inputGraph = graphEditor.getText();

        // Attempt to parse the graph
        var graph = safeParseGraph(inputGraph);

        if (graph == null) {
            System.err.println("Could not parse graph");
            return;
        }

        // Run the task (currently synchronous, but later in async)
        var updateVisualizer = addProgressView();
        /*var completionVisualizer = new ICompletionVisualizer() {

            @Override
            public void setSchedule(Schedule schedule) {
                var scheduleView = addResultsView();
                scheduleView.setSchedule(schedule);
            }
        };*/
        var completionVisualizer = addResultsView();

        taskRunner.safeRunAsync(runnable, graph, processorCount, coreCount, updateVisualizer, completionVisualizer);
    }

    /**
     * Parse the provided string and return a new instance of its graph model.
     * @param graphviz Input string in graphviz dot format
     * @return The graph model or NULL if a failure occurred
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
     * Redirect standard output and error to internal console
     * @param shouldRedirect whether to redirect
     */
    private void redirectOutput(boolean shouldRedirect) {
        if (shouldRedirect) {
            // Set new standard output and error
            System.setOut(consoleOutput);
            System.setErr(consoleOutput);
        }
        else {
            // Set new standard output and error
            System.setOut(stdOutput);
            System.setErr(stdError);
        }
    }

    /**
     * Set the visibility of the internal console
     * @param showConsole whether to show
     */
    private void setConsoleState(boolean showConsole) {
        if (showConsole) {
            redirectOutput(true);
            useInternalConsole = true;

            // Enable split pane
            splitPane.setDividerPosition(0, 0.7);
            console.setVisible(true);
            console.setManaged(true);
        }
        else {
            redirectOutput(false);
            useInternalConsole = false;

            // Disable split pane
            splitPane.setDividerPosition(0, 1.0);
            console.setVisible(false);
            console.setManaged(false);
        }
    }

    /**
     * Toggle the console visibility
     * @param actionEvent ignored
     */
    public void onToggleConsole(ActionEvent actionEvent) {
        setConsoleState(!useInternalConsole);
    }

    /**
     * Setup relevant streams for output redirection
     */
    private void setupRedirection() {
        // Create console output stream
        OutputStream consoleStream = console.getConsoleOutputStream();
        consoleOutput = new PrintStream(consoleStream);

        // Store reference to system print streams
        stdOutput = System.out;
        stdError = System.err;
    }

    /**
     * Called once JavaFX has initialised. Do setup involving controls
     * in this function.
     */
    public void initialize() {
        // Setup console for redirection but do not redirect
        // until the user opts in.
        setupRedirection();
        setConsoleState(false);

        // Setup List View
        listView.setItems(taskList);
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Cell Factory
        // Refer to: https://stackoverflow.com/questions/36657299/how-can-i-populate-a-listview-in-javafx-using-custom-objects
        listView.setCellFactory(task -> new ListCell<>() {
            @Override
            protected void updateItem(IRunnable item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null)
                    setText(null);
                else
                    setText(item.getTaskName());
            }
        });

        // Setup Defaults

        // Populate Graph Editor
        var graphDotFile = Utils.loadResource(MainController.class, "Nodes_7_OutTree.dot");

        if (graphDotFile != null) {
            graphEditor.setText(graphDotFile);
        }

        // Setup Num Processors Spinner
        var factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE);
        factory.setValue(2);

        numProcessors.setValueFactory(factory);

        // Setup Core Count Spinner
        factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE);
        factory.setValue(1);

        cpuCores.setValueFactory(factory);

    }

    /**
     * Add a new tab
     * @param title Title of tab
     * @param content Content of tab (JavaFX control)
     * @param makeCurrent Steal focus from current tab
     */
    private void addTab(String title, Node content, boolean makeCurrent) {
        Tab newTab = new Tab(title, content);
        newTab.setClosable(true);
        tabPane.getTabs().add(newTab);

        if (makeCurrent)
            tabPane.getSelectionModel().select(newTab);
    }

    /**
     * Add a new tab in the background
     * @param title Title of tab
     * @param content Content of tab (JavaFX control)
     */
    private void addTab(String title, Node content) {
        addTab(title, content, false);
    }

    private RadialTree addProgressView() {
        var radialTree = new RadialTree();
        addTab("Progress", radialTree, true);
        return radialTree;
    }

    /**
     * Show schedule in new tab
     */
    private ScheduleView addResultsView() {
        // Scheduler View is a custom control which displays a schedule
        var schedulerView = new ScheduleView();
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
        addTab("Job Results", vbox);

        return schedulerView;
    }

    /**
     * Controller associated with main-view.fxml
     */
    public MainController() {

        // Create runner
        taskRunner = new TaskRunner();

        // Add tasks
        List<IRunnable> tasks = new ArrayList<>();

        tasks.add(Utils.createPrintGraphTask());
        //tasks.add(new DepthFirstSearch());
        tasks.add(new BranchAndBound());

        taskList = FXCollections.observableList(tasks);
    }
}