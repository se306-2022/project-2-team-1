package com.team01.scheduler.cli.io;

import com.team01.scheduler.graph.models.GraphController;
import com.team01.scheduler.TaskRunner;
import com.team01.scheduler.algorithm.BranchAndBound;
import com.team01.scheduler.algorithm.Schedule;
import com.team01.scheduler.graph.models.Graph;
import com.team01.scheduler.graph.util.ExportToDotFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class InputController {

    private static final Logger LOGGER = LoggerFactory.getLogger(InputController.class);
    public CommandLineParser commandLineParser;
    private Graph graph;

    private ExportToDotFile export;

    public enum InvocationType {
        VISUALIZATION,
        HEADLESS,
    }

    /**
     * Create a new InputController for the given arguments
     * @param args Args to parse
     */
    public InputController(String args[]) {
        commandLineParser = CommandLineParser.getInstance();
        commandLineParser.parseInputArguments(args);
    }

    public InvocationType getInvocationType() {
        if (commandLineParser.isVisualize())
            return InvocationType.VISUALIZATION;

        return InvocationType.HEADLESS;
    }

    public void runScheduler() {

        Schedule schedule;
        TaskRunner taskRunner = new TaskRunner();
        try {
            var file = new File(commandLineParser.getInputFileName());
            var graphController = new GraphController(file);
            graph = graphController.getGraph();

            schedule = taskRunner.safeRun(new BranchAndBound(), graph, commandLineParser.getNumProcessors());

            try{
                System.out.println("  export");
                ExportToDotFile export = new ExportToDotFile(graph,commandLineParser.getOutputFileName(),schedule);
                export.writeDotWithSchedule();
            } catch (Exception e){
                e.printStackTrace();
            }


        } catch (IOException e){
            System.out.println("Please enter a valid file name with a valid dot file.");
            LOGGER.info("<<<PROBLEM WITH GRAPH PARSING>>> " + e.getMessage());
        }
    }

}
