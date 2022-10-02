package com.team01.optimal;

import com.team01.scheduler.TaskRunner;
import com.team01.scheduler.algorithm.BranchAndBound;
import com.team01.scheduler.algorithm.Schedule;
import com.team01.scheduler.graph.models.Graph;
import com.team01.scheduler.graph.models.GraphController;
import com.team01.scheduler.graph.util.ExportToDotFile;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

public class FindOptimalScheduleTest {
    Graph graph;

    /**
     *
     * @return results on whether an optimal test case was found
     * @throws IOException for invalid files
     */
    @TestFactory
    Stream<DynamicTest> providedTests() throws IOException {
        String[][] input = new String[][]{
                // Test params
                {"Nodes_7_OutTree.dot","2","28","output7.dot"},
                {"Nodes_8_Random.dot","2","581","output8.dot"},
                {"Nodes_9_SeriesParallel.dot","2","55","output9.dot"},
                {"Nodes_10_Random.dot","2","50","output10.dot"},
                {"Nodes_11_OutTree.dot","2","350","output11.dot"},
                {"Nodes_7_OutTree.dot","4","22","output7.dot"},
                {"Nodes_8_Random.dot","4","581","output8.dot"},
                {"Nodes_9_SeriesParallel.dot","4","55","output9.dot"},
                {"Nodes_10_Random.dot","4","50","output10.dot"},
                {"Nodes_11_OutTree.dot","4","227","output11.dot"},
        };
        TaskRunner taskRunner = new TaskRunner();

        return Arrays.stream(input).map(params -> {
            // Get params from array
            String fileName = params[0];
            String processorCount = params[1];
            int expected = Integer.parseInt(params[2]);
            String outFileName = params[3];

            try {
                File file = new File(fileName);
                GraphController gc = new GraphController(file);
                graph = gc.getGraph();
                Schedule schedule = taskRunner.safeRun(new BranchAndBound(), graph,Integer.parseInt(processorCount));
                ExportToDotFile export = new ExportToDotFile(graph, outFileName, schedule);
                export.writeDotWithSchedule();
                return DynamicTest.dynamicTest("File: "+fileName+" processors: "+processorCount, () -> {
                    Assertions.assertEquals(expected,schedule.getShortestPath());
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        });
    }


}
