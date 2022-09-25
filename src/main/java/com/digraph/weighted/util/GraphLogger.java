package com.digraph.weighted.util;

import com.digraph.weighted.models.Edge;
import com.digraph.weighted.models.EdgesLinkedList;
import com.digraph.weighted.models.Node;

import java.util.ArrayList;
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
            Edge currentEdge = currentList.get(0);

            for (int i = 0; i < listSize; i++) {
                currentEdge = currentList.get(i);

                System.out.println(currentEdge.toString());
            }
        }
    }

}
