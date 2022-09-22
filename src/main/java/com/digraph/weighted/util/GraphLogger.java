package com.digraph.weighted.util;

import com.digraph.weighted.models.Edge;

import java.util.ArrayList;
import java.util.Map;

public class GraphLogger {
    private Map<Integer, ArrayList<Edge>> graph;

    public GraphLogger(Map<Integer, ArrayList<Edge>> graph) {
        this.graph = graph;
    }

    /**
     * Log the graph to console
     */
    public void log(){
        for (Map.Entry<Integer,ArrayList<Edge>> entry : graph.entrySet()){
            System.out.print(entry.getKey() + ": ");

            for (Edge edge : entry.getValue()){
                System.out.print(" "+ edge.getSrc().getValue()+"->"+edge.getDest().getValue()+"("+edge.getWeight()+")");
            }
            System.out.println();
        }
    }

}
