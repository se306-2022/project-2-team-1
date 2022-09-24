package com.team01.scheduler;

import com.team01.scheduler.algorithm.DepthFirstSearch;
import com.team01.scheduler.algorithm.IRunnable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;

public class MainController {

    private TaskRunner taskRunner;
    private ObservableList<IRunnable> taskList;

    @FXML
    private ListView<IRunnable> listView;

    @FXML
    private TextArea textArea;

    @FXML
    public void onRunTask(ActionEvent actionEvent) {
        IRunnable runnable = listView.getSelectionModel().getSelectedItem();
        taskRunner.safeRun(runnable, Utils.createSampleGraph());
    }

    public void initialize()
    {
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

    public MainController() {

        // Create runner
        taskRunner = new TaskRunner();

        // Add tasks
        List<IRunnable> tasks = new ArrayList<>();

        tasks.add(Utils.createPrintGraphTask());
        tasks.add(new DepthFirstSearch());

        taskList = FXCollections.observableList(tasks);
    }
}