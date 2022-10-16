package com.team01.scheduler.cli.io;

import com.team01.scheduler.Invocation;
import com.team01.scheduler.algorithm.astar.AStarScheduler;
import com.team01.scheduler.algorithm.branchandbound.BranchAndBoundSerial;
import com.team01.scheduler.algorithm.matrixModels.Graph;
import com.team01.scheduler.graph.models.GraphController;
import com.team01.scheduler.TaskRunner;
import com.team01.scheduler.algorithm.branchandbound.BranchAndBound;
import com.team01.scheduler.algorithm.Schedule;

import com.team01.scheduler.graph.util.ExportToDotFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class InputController {

    private static final Logger LOGGER = LoggerFactory.getLogger(InputController.class);
    public CommandLineParser commandLineParser;
    private Graph graph;

    private ExportToDotFile export;

    public enum Algorithm {
        BRANCH_AND_BOUND,
        A_STAR
    }

    /**
     * Create a new InputController for the given arguments
     * @param args Args to parse
     */
    public InputController(String args[]) {
        commandLineParser = CommandLineParser.getInstance();
        commandLineParser.parseInputArguments(args);
    }

    /**
     * Get an Invocation from the command line args
     * @return Invocation to execute
     */
    public Invocation getInvocation() {

        var invocation = new Invocation();
        invocation.numCores = commandLineParser.getNumCores();
        invocation.numProcessors = commandLineParser.getNumProcessors();
        invocation.useVisualization = commandLineParser.isVisualize();
        invocation.outputFileName = commandLineParser.getOutputFileName();
        invocation.inputFileName = commandLineParser.getInputFileName();

        if (commandLineParser.getAlgorithm() == Algorithm.A_STAR) {
            invocation.runnable = new AStarScheduler();
        } else {
            invocation.runnable = new BranchAndBound();
        }

        return invocation;
    }

}
