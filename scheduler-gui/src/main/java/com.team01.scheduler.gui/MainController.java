package com.team01.scheduler.gui;

import com.team01.scheduler.TaskRunner;
import com.team01.scheduler.Utils;
import com.team01.scheduler.algorithm.BranchAndBound;
import com.team01.scheduler.algorithm.Schedule;
import com.team01.scheduler.graph.models.Graph;
import com.team01.scheduler.graph.models.GraphController;
import com.team01.scheduler.gui.views.ScheduleView;
import com.team01.scheduler.prototype.DepthFirstSearch;
import com.team01.scheduler.algorithm.IRunnable;
import com.team01.scheduler.gui.views.Console;
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
    public void onRunTask(ActionEvent actionEvent) {
        // Get runnable from list
        IRunnable runnable = listView.getSelectionModel().getSelectedItem();

        if (runnable == null) {
            System.err.println("No task selected");
            return;
        }

        // Get parameters from controls
        int processorCount = numProcessors.getValue();
        String inputGraph = graphEditor.getText();

        // Attempt to parse the graph
        var graph = safeParseGraph(inputGraph);

        if (graph == null) {
            System.err.println("Could not parse graph");
            return;
        }

        // Run the task (currently synchronous, but later in async)
        taskRunner.safeRunAsync(runnable, graph, processorCount, schedule -> {
            if (schedule != null) {
                showResults(schedule);
            }
        });
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

    public void onToggleConsole(ActionEvent actionEvent) {
        setConsoleState(!useInternalConsole);
    }

    private void setupRedirection() {
        // Create console output stream
        OutputStream consoleStream = console.getConsoleOutputStream();
        consoleOutput = new PrintStream(consoleStream);

        // Store reference to system print streams
        stdOutput = System.out;
        stdError = System.err;
    }

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

        // Setup Core Count Spinner
        var factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE);
        factory.setValue(2);

        numProcessors.setValueFactory(factory);

    }

    private void addTab(String title, Node content) {
        Tab newTab = new Tab(title, content);
        newTab.setClosable(true);
        tabPane.getTabs().add(newTab);
    }

    private void showResults(Schedule schedule) {
        var schedulerView = new ScheduleView(schedule);
        VBox.setVgrow(schedulerView, Priority.ALWAYS);

        var zoomInButton = new Button("Zoom In");
        zoomInButton.setOnMouseClicked(x -> schedulerView.zoomIn());

        var zoomOutButton = new Button("Zoom Out");
        zoomOutButton.setOnMouseClicked(x -> schedulerView.zoomOut());

        var toolbar = new ToolBar(zoomInButton, zoomOutButton);

        var vbox = new VBox();
        vbox.getChildren().addAll(toolbar, schedulerView);

        addTab("Job Results", vbox);
    }

    public MainController() {

        // Create runner
        taskRunner = new TaskRunner();

        // Add tasks
        List<IRunnable> tasks = new ArrayList<>();

        tasks.add(Utils.createPrintGraphTask());
        tasks.add(new DepthFirstSearch());
        tasks.add(new BranchAndBound());

        taskList = FXCollections.observableList(tasks);
    }
}