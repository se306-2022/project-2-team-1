package com.digraph.weighted.models;

import com.digraph.weighted.exceptions.CycleException;
import com.digraph.weighted.exceptions.InvalidEdgeWeightException;
import com.digraph.weighted.exceptions.NonExistingNodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Graph {

    private static final Logger LOGGER = LoggerFactory.getLogger(Graph.class);

    // Adjacency List
    private Map<Integer,ArrayList<Edge>> adjacencyList;

    public Graph(){
        adjacencyList = new HashMap<>();
    }

    public Map<Integer, ArrayList<Edge>> getAdjacencyList() {
        return adjacencyList;
    }

    /**
     * Add nodes to adjacency list
     */
    public void addNodes(Node ...nodes){
        for (Node node : nodes){
            if(isExistNode(node)){
                continue;
            } else {
                adjacencyList.put(node.getValue(),new ArrayList<Edge>());
            }
        }
    }

    /**
     * Add single node to the adjacency list
     * @param node
     */
    public void addNode(Node node){
        if(!isExistNode(node)){
            adjacencyList.put(node.getValue(),new ArrayList<Edge>());
        }
    }

    /**
     * check that node doesn't already exist
     */
    private boolean isExistNode(Node node){
        if (adjacencyList.containsKey(node)){
            return true;
        }
        return false;
    }

    /**
     * Add edge to the adjacencyList
     */
    public void addEdge(Edge edge){
        try {
            if (!isNodeValid(edge.getSrc()) || !isNodeValid(edge.getDest())){
                throw new NonExistingNodeException("The node does not exist");
            }

            if (!isExistEdge(edge)) {
                if (edge.getWeight() < 1) {
                    throw new InvalidEdgeWeightException("The edge weight cannot be negative or zero.");
                }

                if (edge.getSrc().equals(edge.getDest())){
                    throw new CycleException("The source and destination node of the edge are the same.");
                }
                adjacencyList.get(edge.getSrc().getValue()).add(edge);
            }
        } catch (InvalidEdgeWeightException e){
            LOGGER.info("<<<<INVALID>>> invalid edge entry: " + edge);
        } catch (NonExistingNodeException e) {
            LOGGER.info("<<<<INVALID>>> src or dest node does not exist for: " + edge);
        } catch (CycleException e) {
            LOGGER.info("<<<<INVALID>>> self-loop edge: src and dest the same: " + edge);
        }
    }

    /**
     * Add collection of edges to the adjacency list
     * @param edges
     */
    public void addEdges(Edge ...edges){
        for (Edge e : edges){
            addEdge(e);
        }
    }

    private boolean isExistEdge(Edge edge){
        ArrayList<Edge> edges = adjacencyList.get(edge.getSrc().getValue());
        if (edges.contains(edge)){
            LOGGER.info("<<<DUPLICATE>>> graph already contains: " + edge);
            return true;
        } else {
            //LOGGER.info("<<<VALID ENTRY>>> graph doesnt contain: " + edge);
            return false;
        }
    }

    public boolean isNodeValid(Node node){
        if (adjacencyList.containsKey(node.getValue())){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Graph{" +
                "adjacencyList=" + adjacencyList +
                '}';
    }
}
