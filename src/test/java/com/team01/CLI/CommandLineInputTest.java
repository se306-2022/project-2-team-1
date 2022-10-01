package com.team01.CLI;

import com.team01.scheduler.io.InputController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class CommandLineInputTest {

    /**
     * Ensure command line input are parsed/identified correctly
     * **/
    @Test
    void inputTest() {
        String fileName = "graph.dot";
        String processorCount = "3";
        String option1 = "-o";
        String outFileName = "outputNew1.dot";
        String[] args = new String[] {fileName, processorCount, option1, outFileName};

        InputController ic = new InputController(args);
        String[] expected = new String[] {"HEADLESS", processorCount, outFileName, fileName};
        String invocationType = ic.getInvocationType().toString();
        int numProcessors = ic.commandLineParser.getNumProcessors();
        String outputFileName = ic.commandLineParser.getOutputFileName();
        String inputFileName = ic.commandLineParser.getInputFileName();

        String[] actual = new String[] {invocationType, String.valueOf(numProcessors), outputFileName, inputFileName};
        Assertions.assertArrayEquals(expected,actual);

    }
}
