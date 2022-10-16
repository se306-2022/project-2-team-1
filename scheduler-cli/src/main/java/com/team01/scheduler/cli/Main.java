package com.team01.scheduler.cli;

import com.team01.scheduler.Invocation;
import com.team01.scheduler.TaskRunner;
import com.team01.scheduler.algorithm.Schedule;
import com.team01.scheduler.algorithm.branchandbound.BranchAndBound;
import com.team01.scheduler.cli.io.InputController;
import com.team01.scheduler.graph.models.GraphController;
import com.team01.scheduler.graph.util.ExportToDotFile;
import com.team01.scheduler.gui.MainApplication;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void runSchedulerWithVisualization(Invocation invocation) {

        // See: https://stackoverflow.com/questions/25873769/launch-javafx-application-from-another-class

        // Set the invocation for the JavaFX Application
        MainApplication.setInvocation(invocation);
        javafx.application.Application.launch(MainApplication.class);
    }

    public static void runScheduler(Invocation invocation) {
        Schedule schedule;
        TaskRunner taskRunner = new TaskRunner();
        try {
            var graphController = new GraphController(new File(invocation.inputFileName));
            var graph = graphController.getGraph(); // parse dot file to graph model

            schedule = taskRunner.safeRun(new BranchAndBound(), graph, invocation.numProcessors, invocation.numCores); // run branch and bound

            try{
                // export to dot file
                var outputFileName = invocation.outputFileName;
                ExportToDotFile export = new ExportToDotFile(graph, outputFileName, schedule);
                export.writeDotWithSchedule();

                System.out.println("Wrote to file: " + outputFileName);
            } catch (Exception e){
                e.printStackTrace();
            }
        } catch (IOException e){
            System.out.println("Error with graph parsing: " + e.getMessage());
            System.out.println("Ensure the dot file is valid.");
        }
    }

    /**
     * Different run configuration depending on how the application was called.
     * @param args command line input
     */
    public static void main(String[] args) {
        // initialise input controller to handle input arguments
        try {
            var inputController = new InputController(args);
            var invocation = inputController.getInvocation();

            if (invocation.useVisualization) {
                runSchedulerWithVisualization(invocation);
            } else {
                runScheduler(invocation);
            }
        }
        catch (Exception e) {
            System.err.println("Unable to run scheduler: " + e.getMessage());
        }
    }
}