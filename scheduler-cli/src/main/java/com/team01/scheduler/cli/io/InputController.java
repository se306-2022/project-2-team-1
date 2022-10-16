package com.team01.scheduler.cli.io;

import com.team01.scheduler.Invocation;
import com.team01.scheduler.algorithm.astar.AStarScheduler;
import com.team01.scheduler.graph.model.Graph;
import com.team01.scheduler.algorithm.branchandbound.BranchAndBound;

import com.team01.scheduler.graph.ExportToDotFile;

public class InputController {

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
