package com.team01.graphparser;

import com.team01.scheduler.Utils;
import com.team01.scheduler.graph.exceptions.CycleException;
import com.team01.scheduler.graph.exceptions.InvalidEdgeWeightException;
import com.team01.scheduler.graph.exceptions.InvalidInputException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class GraphParserTest {


    @Test
    void testInvalidName() {
        String name = "Does_Not_Exist.dot";

        String graph = Utils.loadResource(Utils.class, name);

        Assertions.assertNull(graph);
    }

    @Test
    void testCyclicGraphException() {

        CycleException exception = assertThrows(CycleException.class, () -> {
            String name = "cyclic_graph.dot";
            String graph = Utils.loadResource(Utils.class, name);
        });

        Assertions.assertEquals(exception, new CycleException("Graph is cyclic."));
    }

    @Test
    void testEdgeWeightException() {

        InvalidEdgeWeightException exception = assertThrows(InvalidEdgeWeightException.class, () -> {
            String name = "invalid_edge_weight_exception.dot";
            String graph = Utils.loadResource(Utils.class, name);
        });

        Assertions.assertEquals(exception, new InvalidEdgeWeightException("Graph has invalid edge weight."));
    }




}