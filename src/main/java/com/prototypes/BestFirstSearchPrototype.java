package com.prototypes;
import java.util.PriorityQueue;


/**
 * A prototype class designed to simulate Best First Search which contains pseudo-graph construction
 * as well as the algorithm.
 */
public class BestFirstSearchPrototype {

    //Local Cost
    private static int cost = 0;

    //Driver code
    public static void main(String[] args) {

        int vertices = 4;

        WeightedGraph.Graph graph = new WeightedGraph.Graph(vertices);

        graph.addNode(0, 2);
        graph.addNode(1, 3);
        graph.addNode(2, 1);
        graph.addNode(3, 1);

        graph.addEdge(0, 1, 1);
        graph.addEdge(0, 2, 2);
        graph.addEdge(1, 3, 2);
        graph.addEdge(2, 3, 1);

        graph.printGraph();

        BestFirstSearch(graph, 0, 3, 4);
    }

    /**
     * Simulate Prototype of Best First Search with default priority queue logic (i.e.) Add everything to queue
     * with no prejudice.
     *
     * The Best First Search algorithm starts with considering the head of the graph (the very beginning node)
     * and find adjacent unvisited nodes. These unvisited nodes are added to the priority queue, to be traversed
     * in the next subsequent loops. The algorithm ends when either all nodes are traversed, or the current leaf
     * node value plus current path is lower than any one of the values in the priority queue plus the current path.
     *
     * @param graph     The input graph
     * @param start     The starting task of the graph
     * @param end       The end task of the graph, although it is important to note there can be multiple ends
     * @param vertices  The number of tasks in the graph
     */
    public static void BestFirstSearch(WeightedGraph.Graph graph, int start, int end, int vertices) {

        //Instantiate new priority queue
        PriorityQueue<WeightedGraph.Node> pq = new PriorityQueue<>();

        System.out.println("Traversing through the graph...");

        //Initial Cost
        cost += graph.nodeList[start].getWeight();

        //Initiated all visited arrays to false by default
        boolean visited[] = new boolean[vertices];

        pq.add(graph.getNode(start));
        while (!pq.isEmpty()) {
            int currentNodeID = pq.poll().getId();
            System.out.println(currentNodeID + " ");

            //Get all adjacent edges in current node ID.
            for (int adjacentNodeId : graph.getAdjacentNodes(currentNodeID)) {

                //If the current node visited is false
                if (!visited[adjacentNodeId]) {
                    visited[adjacentNodeId] = true;
                    cost += graph.nodeList[adjacentNodeId].getWeight();

                    //Default priority queue implementation by adding all adjacent nodes to queue
                    pq.add(graph.getNode(adjacentNodeId));

                }
            }
        }

        //Print out linear cost
        System.out.println("----------------------------");
        System.out.println("The total cost is: " + cost);
        System.out.println("----------------------------");
        System.out.println("Search finished.");

    }
}