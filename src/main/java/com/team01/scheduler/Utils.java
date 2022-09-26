package com.team01.scheduler;

import com.team01.scheduler.algorithm.IRunnable;
import com.team01.scheduler.graph.models.Edge;
import com.team01.scheduler.graph.models.EdgesLinkedList;
import com.team01.scheduler.graph.models.Graph;
import com.team01.scheduler.graph.models.Node;

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
        Node nodeA = new Node("a", 2);
        Node nodeB = new Node("b", 3);
        Node nodeC = new Node("c", 3);
        Node nodeD = new Node("d", 2);

        Edge edgeAB = new Edge(nodeA, nodeB, 1);
        Edge edgeAC = new Edge(nodeA, nodeC, 2);
        Edge edgeBD = new Edge(nodeB, nodeD, 2);
        Edge edgeCD = new Edge(nodeC, nodeD, 1);

        List<Node> nodes = Arrays.asList(nodeA, nodeB, nodeC, nodeD);
        List<Edge> edges = Arrays.asList(edgeAB, edgeAC, edgeBD, edgeCD);

        return new Graph(edges, nodes);
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
            public void run(Graph graph) {
                for (EdgesLinkedList currentList: graph.getGraph().values()) {
                    int listSize = currentList.size();

                    for (int i = 0; i < listSize; i++) {
                        Edge currentEdge = currentList.get(i);

                        System.out.println(currentEdge.toString());
                    }
                }
            }
        };
    }
}
