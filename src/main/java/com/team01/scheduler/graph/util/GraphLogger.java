package com.team01.scheduler.graph.util;

import com.team01.scheduler.graph.models.Edge;
import com.team01.scheduler.graph.models.Node;

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
                System.out.print(" "+ edge.getSource().getValue()+"->"+edge.getTarget().getValue()+"("+edge.getWeight()+")");
            }
            System.out.println();
        }
    }

}
