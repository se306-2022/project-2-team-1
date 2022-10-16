package com.team01.scheduler.graph.util;

import com.team01.scheduler.graph.models.Edge;
import com.team01.scheduler.graph.models.EdgesLinkedList;
import com.team01.scheduler.graph.models.Node;

import java.util.Map;

public class GraphLogger {
    private Map<Node, EdgesLinkedList> graph;

    public GraphLogger(Map<Node, EdgesLinkedList> graph) {
        this.graph = graph;
    }

    /**
     * Log the graph to console
     */
    public void log(){
        for (EdgesLinkedList currentList: graph.values()) {
            int listSize = currentList.size();

            Edge currentEdge;

            //Iterate through edges and print them to console
            for (int i = 0; i < listSize; i++) {
                currentEdge = currentList.get(i);

                System.out.println(currentEdge.toString());
            }
        }
    }

}
