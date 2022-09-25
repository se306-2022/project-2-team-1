package com.digraph.weighted.io;

import com.digraph.weighted.exceptions.InvalidInputException;
import com.digraph.weighted.models.Graph;
import com.digraph.weighted.models.GraphController;
import com.digraph.weighted.util.ExportToDotFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.digraph.weighted.io.*;
import java.io.IOException;
import com.team01.scheduler.*;

import static javafx.application.Application.launch;

public class InputController {

    private static final Logger LOGGER = LoggerFactory.getLogger(InputController.class);
    private static final InputController instance = new InputController();
    private CommandLineParser commandLineParser;
    private Graph graph;

    private ExportToDotFile export;
    /**
     * private constructor for singleton design pattern
     */
    private InputController(){
        commandLineParser = CommandLineParser.getInstance();
    }

    /**
     * Use this getter to use static object of class, as the constructor
     * is private.
     * @return InputController
     */

    public static InputController getInstance(){
        return instance;
    }

    public void run(String[] args) throws InvalidInputException {
        commandLineParser.parseInputArguments(args);

        // check if visualization is enabled or disabled
        if (commandLineParser.isVisualize()){
            initiateScheduler();
            launch();
        } else{
            // run just the scheduler without no visualization
            initiateScheduler();
        }
    }



    private void initiateScheduler(){
        try {
            GraphController gc = new GraphController(commandLineParser.getInputFileName());
            graph = gc.getGraph();

            export = new ExportToDotFile(graph,commandLineParser.getOutputFileName());
            export.writeDot();

        } catch (IOException e){
            LOGGER.info("<<<PROBLEM WITH GRAPH PARSING>>> " + e.getMessage());
        }
    }

}