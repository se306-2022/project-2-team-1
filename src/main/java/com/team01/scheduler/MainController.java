package com.team01.scheduler;

import com.team01.scheduler.algorithm.BranchAndBound;
import com.team01.scheduler.algorithm.Schedule;
import com.team01.scheduler.gui.ScheduleView;
import com.team01.scheduler.prototype.DepthFirstSearch;
import com.team01.scheduler.algorithm.IRunnable;
import com.team01.scheduler.gui.Console;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.OutputStream;
import java.io.PrintStream;
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
    public void onRunTask(ActionEvent actionEvent) {
        IRunnable runnable = listView.getSelectionModel().getSelectedItem();

        if (runnable == null) {
            System.err.println("No task selected");
            return;
        }

        taskRunner.safeRun(runnable, Utils.createSampleGraph(), schedule -> {
            if (schedule instanceof Schedule) {
                showResults(schedule);
            }
        });
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
    }

    private void addTab(String title, Node content) {
        Tab newTab = new Tab(title, content);
        newTab.setClosable(true);
        tabPane.getTabs().add(newTab);
    }

    private void showResults(Schedule schedule) {
        var schedulerView = new ScheduleView(schedule);
        addTab("Job Results", schedulerView);
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