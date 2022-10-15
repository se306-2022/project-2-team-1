package com.team01.scheduler;

import com.team01.scheduler.algorithm.ICompletionVisualizer;
import com.team01.scheduler.algorithm.IRunnable;
import com.team01.scheduler.algorithm.IUpdateVisualizer;
import com.team01.scheduler.algorithm.Schedule;
import com.team01.scheduler.algorithm.matrixModels.Graph;
import com.team01.scheduler.algorithm.matrixModels.Node;

import com.team01.scheduler.algorithm.matrixModels.Edge;
import com.team01.scheduler.graph.models.EdgesLinkedList;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class Utils {
    /**
     * Creates a sample graph using the node and edge
     * weights provided in the brief (refer to Figure 1).
     *
     * @return Graph instance
     */
    public static Graph createSampleGraph() {
        Node nodeA = new Node(0,"a", 2);
        Node nodeB = new Node(1,"b", 3);
        Node nodeC = new Node(2,"c", 3);
        Node nodeD = new Node(3,"d", 2);

        Edge edgeAB = new Edge(nodeA, nodeB, 1);
        Edge edgeAC = new Edge(nodeA, nodeC, 2);
        Edge edgeBD = new Edge(nodeB, nodeD, 2);
        Edge edgeCD = new Edge(nodeC, nodeD, 1);

        List<Node> nodes = Arrays.asList(nodeA, nodeB, nodeC, nodeD);
        List<Edge> edges = Arrays.asList(edgeAB, edgeAC, edgeBD, edgeCD);

//        return new Graph(nodes,edges);
        return null;
    }

    /**
     * Creates a new task to print out all the edges in the graph.
     *
     * @return IRunnable instance
     */
    public static IRunnable createPrintGraphTask() {
        return new IRunnable() {
            @Override
            public String getTaskName() {
                return "Print Graph";
            }

            @Override
            public Schedule run(Graph graph, int numProcessors, int numCores, IUpdateVisualizer updateVisualizer, ICompletionVisualizer completionVisualizer) {
                /*
                for (EdgesLinkedList currentList: graph.getGraph().values()) {
                    int listSize = currentList.size();

                    for (int i = 0; i < listSize; i++) {
                        Edge currentEdge = currentList.get(i);

                        System.out.println(currentEdge.toString());
                    }
                }
                 */
                for (int i=0; i< graph.getNumberofNodes(); i++){
                    for (int j=0; j < graph.getNumberofNodes(); j++){
                        System.out.println(graph.getNodeById(i) + " - " + graph.getNodeById(j) + " = " + graph.getAdjacencyMatrix()[i][j]);
                    }
                }
                return null;
            }
        };
    }

    /**
     * Helper function for reading a resource file as a string.
     *
     * @param klass Class to load the resource from
     * @param name Filename of the resource (relative to the module)
     * @return String containing the resource contents
     * @param <T> Type of the class to load the resource from
     */
    public static <T> String loadResource(Class<T> klass, String name) {
        // Get input stream for resource
        InputStream inputStream = klass.getClassLoader().getResourceAsStream(name);

        if (inputStream != null) {
            // Try read the resource input stream
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                StringBuilder contents = new StringBuilder();
                String append;

                // Build a string by appending each line
                while ((append = reader.readLine()) != null)
                    contents.append(append).append("\n");

                System.out.println(contents);
                return contents.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Otherwise return null
        return null;
    }
}
