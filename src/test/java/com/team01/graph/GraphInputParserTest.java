package com.team01.graph;

import com.team01.scheduler.graph.models.Edge;
import com.team01.scheduler.graph.models.Graph;
import com.team01.scheduler.graph.models.GraphController;
import com.team01.scheduler.graph.models.Node;
import com.team01.scheduler.graph.util.GraphLogger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GraphInputParserTest {
    private Graph graph = null;


    @Test
    void testGetGraphFromFileName() throws IOException {
        // TODO CHECK EACH EDGE NODE
        GraphController graphController = new GraphController("graph.dot");
        graph = graphController.getGraph();
        Assertions.assertNotNull(graph);
    }


}
