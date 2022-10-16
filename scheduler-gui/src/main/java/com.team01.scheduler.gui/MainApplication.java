package com.team01.scheduler.gui;

import com.team01.scheduler.Invocation;
import com.team01.scheduler.algorithm.ICompletionVisualizer;
import com.team01.scheduler.algorithm.IRunnable;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class MainApplication extends Application {

    private static Invocation invocation;

    /**
     * JavaFX Start method
     *
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     */
    @Override
    public void start(Stage stage) {
        try {
            if (invocation != null) {
                // Attach
                runVisualizer(stage, invocation);
            } else {
                // Run Debug GUI
                runDebugGui(stage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void runVisualizer(Stage stage, Invocation invocation) {

        String graphDescription;

        try {
            graphDescription = Files.readString(Path.of(invocation.inputFileName));
        } catch (Exception e) {
            throw new RuntimeException("Unable to read graph file: " + e.getMessage());
        }

        var node = runTaskWithDashboard(
                invocation.runnable,
                graphDescription,
                invocation.numProcessors,
                invocation.numCores,
                invocation.useVisualization,
                invocation.outputFileName,
                null);

        Scene scene = new Scene(node, 1250, 800);

        stage.setResizable(false);
        stage.setTitle("Team 01 - Visualiser [" + invocation.runnable.getTaskName() + "]");
        stage.setScene(scene);
        stage.show();
    }

    private void runDebugGui(Stage stage) throws IOException {
        // Get main view FXML file
        URL uiPath = MainApplication.class.getClassLoader().getResource("main-view.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(uiPath);

        // Create scene and display
        Scene scene = new Scene(fxmlLoader.load(), 1250, 840);

        stage.setResizable(false);
        stage.setTitle("Team 01 - Scheduler");
        stage.setScene(scene);
        stage.show();
    }

    public static Parent runTaskWithDashboard(IRunnable runnable, String inputGraph, int processorCount, int numCores, boolean useVisualization, String outputFileName, ICompletionVisualizer completionVisualizer) {
        URL uiPath = MainApplication.class.getClassLoader().getResource("dashboard.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(uiPath);

        // Create scene and display
        GridPane gridPane;
        try {
            gridPane = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Setup Controller Properties
        DashboardController controller = fxmlLoader.getController();
        controller.runWithTask(runnable, inputGraph, processorCount, numCores, useVisualization, completionVisualizer, outputFileName);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(gridPane);

        return borderPane;
    }

    public static void setInvocation(Invocation args) {
        invocation = args;
    }

    /**
     * Entry point for Scheduler Debug Console
     * @param args program arguments
     */
    public static void main(String[] args) {
        invocation = null;
        launch(args);
    }
}