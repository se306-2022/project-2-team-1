package com.team01.scheduler.cli.io;

import com.team01.scheduler.graph.models.GraphController;
import com.team01.scheduler.TaskRunner;
import com.team01.scheduler.algorithm.BranchAndBound;
import com.team01.scheduler.algorithm.Schedule;
import com.team01.scheduler.graph.models.Graph;
import com.team01.scheduler.graph.util.ExportToDotFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
            GraphController gc = new GraphController(commandLineParser.getInputFileName());
            graph = gc.getGraph();
            schedule = taskRunner.safeRun(new BranchAndBound(),graph);
            try{
                ExportToDotFile export = new ExportToDotFile(graph,commandLineParser.getOutputFileName(),schedule);
                export.writeDotWithSchedule();
            } catch (Exception e){
                e.printStackTrace();
            }
            //RunnableOptions.getInstance().setGraph(graph);


        } catch (IOException e){
            LOGGER.info("<<<PROBLEM WITH GRAPH PARSING>>> " + e.getMessage());
        }
    }

}
