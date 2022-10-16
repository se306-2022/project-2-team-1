package com.team01.scheduler.cli.io;

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

    /**
     * Getters for the command line arguments, so that the InputController class
     * can access these values
     * @return
     */
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
            System.exit(0);
        }

        // check that the input file name and the numProcessors are supplied
        if (args == null || args.length < 2) {
            throw new InvalidInputException("InputFileName or numProcessors args not supplied");
        }

        // check input file name is valid
        if (args[0] != null){
            if (!args[0].endsWith(".dot")) {
                System.out.println("Warning: Filename does not end with '.dot' - is this a valid graphviz file?");
            }
            inputFileName = args[0];
        } else {
            throw new InvalidInputException("Input file name is invalid");
        }

        // check that the number of processors is valid
        if (args[1] == null || !isParseableInteger(args[1])){
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
        //ensure dot file is sent
            if (!outputFileName.endsWith(".dot")){
                outputFileName = outputFileName + ".dot";
            }
        }

        isVisualize = options.isVisualize;
    }

    /**
     * Helper to check if string can be parsed to int
     * @param s string to be parsed
     * @return
     */
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

    /**
     *  Debugging function: lists the available parameters/options that can be supplied in the command
     *  line alongside the two mandatory parameters. This method is invoked when the --help optional
     *  paramter is used.
     * @param parser
     */
    private void helpUser(OptionsParser parser){
        System.out.println(parser.describeOptions(Collections.<String, String>emptyMap(), OptionsParser.HelpVerbosity.LONG));
    }


}
