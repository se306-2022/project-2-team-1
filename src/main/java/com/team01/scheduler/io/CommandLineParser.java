package com.team01.scheduler.io;

import com.team01.scheduler.graph.exceptions.InvalidInputException;
import com.google.devtools.common.options.OptionsParser;

import java.util.Collections;

public class CommandLineParser{

    private static final CommandLineParser instance = new CommandLineParser();

    private OptionsParser parser;
    private String inputFileName;
    private String outputFileName;
    private boolean isVisualize;
    private int numProcessors;
    private int numCores;
    private boolean debugGui;

    public String getInputFileName() {
        return inputFileName;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public boolean isVisualize() {
        return isVisualize;
    }

    public int getNumProcessors() {
        return numProcessors;
    }

    public int getNumCores() {
        return numCores;
    }

    public boolean isDebugGui() { return debugGui; }

    /**
     * private constructor for singleton design pattern
     */
    private CommandLineParser(){
        parser = OptionsParser.newOptionsParser(CommandLineOptions.class);
    }

    public static CommandLineParser getInstance(){
        return instance;
    }

    public void parseInputArguments(String[] args) throws InvalidInputException {
        // pass in the command line options
        parser.parseAndExitUponError(args);
        checkValidityOfArguments(args);
    }

    /**
     * Helper method to check that the input arguments supplied are valid
     * @param args
     */
    private void checkValidityOfArguments(String[] args) throws InvalidInputException{
        CommandLineOptions options = parser.getOptions(CommandLineOptions.class);

        if (options.help) {
            helpUser(parser);
        }

        if (options.debugGui) {
            debugGui = true;
            return;
        }

        // check that the input file name and the numProcessors are supplied
        if (args == null || args.length == 0) {
            throw new InvalidInputException("InputFileName or numProcessors args not supplied");
        }

        // check input file name is valid
        if (args[0] != null && args[0].length() >= 5 && args[0].endsWith(".dot")){
            inputFileName = args[0];
        } else {
            throw new InvalidInputException("input file name is invalid");
        }

        // check that the number of processors is valid
        if (args[1] != null && !isParseableInteger(args[1])){
            throw new InvalidInputException("numProcessors arg is invalid");
        }

        numProcessors = Integer.parseInt(args[1]);
        if(numProcessors < 1){
            throw new InvalidInputException("numProcessors arg is invalid");
        }

        // check validity of number of cores
        if (options.numCores < 1){
            throw new InvalidInputException("numCores arg is invalid");
        }
        numCores = options.numCores;

        // check the validity of the output file name
        if(options.outputFileName.equals("")){
            outputFileName = inputFileName.replace(".dot", "") + "-output.dot";
        } else {
            outputFileName = options.outputFileName;

            if (!outputFileName.endsWith(".dot")){
                outputFileName = outputFileName + ".dot";
            }
        }

        isVisualize = options.isVisualize;
    }

    private boolean isParseableInteger(String s){
        if (s == null) {
            return false;
        }

        try {
            int i = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
    private void helpUser(OptionsParser parser){
        System.out.println(parser.describeOptions(Collections.<String, String>emptyMap(), OptionsParser.HelpVerbosity.LONG));
    }


}
