package com.digraph.weighted.util;

import com.digraph.weighted.models.Edge;
import com.digraph.weighted.models.Node;

import java.util.ArrayList;
import java.util.Map;

public class GraphLogger {
    private Map<Node, ArrayList<Edge>> graph;

    public GraphLogger(Map<Node, ArrayList<Edge>> graph) {
        this.graph = graph;
    }

    /**
     * Log the graph to console
     */
    public void log(){
        for (Map.Entry<Node,ArrayList<Edge>> entry : graph.entrySet()){
            System.out.print(entry.getKey() + ": ");

            for (Edge edge : entry.getValue()){
                System.out.print(" "+ edge.getSrc().getValue()+"->"+edge.getDest().getValue()+"("+edge.getWeight()+")");
            }
            System.out.println();
        }
    }

}
