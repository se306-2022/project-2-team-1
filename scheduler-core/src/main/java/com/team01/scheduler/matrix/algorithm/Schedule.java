
package com.team01.scheduler.matrix.algorithm;

import com.team01.scheduler.matrix.exception.NodeInvalidIDMapping;
import com.team01.scheduler.matrix.exception.NonExistingNodeException;
import com.team01.scheduler.matrix.model.Edge;
import com.team01.scheduler.matrix.model.Graph;
import com.team01.scheduler.matrix.model.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * The class Schedule is responsible for keeping track of all tasks within a scheduled task list. The list adds
 * all tasks when the current shortest path is updated
 */
public class Schedule {

    public int getCostFunction() {
        return lowerBound;
    }

    public void setCostFunction(int lowerBound) {
        this.lowerBound = lowerBound;
    }

    /**
     * Stores the lower bound value for the partial schedule,
     * calculated by the CostFunctionCalculator.class
     */
    private int lowerBound;
    private int numProcessors;

    private Processor[] processors;
    private int workingProcessor = 0;

    private Graph graph;
    private List<Node> scheduledTaskList = new ArrayList<Node>();
    private List<Node> entryNodes = new ArrayList<Node>();
    private List<Node> unscheduledTaskList = new ArrayList<Node>();
    private Node lastAddedNode;


    public int getNumberOfNodes(){

        int nodeCount=0;
        for (Processor p : processors){
            nodeCount += p.getScheduledNodes().size();
        }

        return nodeCount;
    }

    /**
     * A constructor to create a new schedule whenever the shortest path is updated
     *
     * @param graph     Input graph
     * @param numProcessors         The number of processors used for the algorithm run
     */
    public Schedule(Graph graph, int numProcessors) throws NodeInvalidIDMapping {
        this.numProcessors = numProcessors;
        this.graph = graph;
        processors = new Processor[numProcessors];

        for(int i = 0; i < numProcessors; i++) {
            processors[i] = new Processor(i);
        }
        entryNodes = graph.getEntryNodes();
        unscheduledTaskList.addAll(graph.getNodes());

    }

    public boolean addTask(Node n, int targetProcessorID) throws NonExistingNodeException, NodeInvalidIDMapping {
        // edge dependencies
        List<Edge> dependencyEdges = new ArrayList<Edge>();
        // if target node is the destination node of an edge, then that edge is a
        for(int i = 0; i < graph.getInputEdges().size();i++){
            if(graph.getInputEdges().get(i).getDestNode().equals(n)){
                dependencyEdges.add(graph.getInputEdges().get(i));
            }
        }
        // node dependencies
        List<Node> dependencyNodes = graph.getParentsForNode(n);


        // All dependencies accounted for
        if(scheduledTaskList.containsAll(dependencyNodes)) {
            // Find latest dependency end time
            int latestTime = 0;
            for(int i = 0; i < processors.length; i++) {
                for(Edge edge: dependencyEdges) {
                    int potentialTime = 0;
                    if(processors[i].getScheduledNodes().get(edge.getSrcNode())!=null) {
                        int nodeStartTime = processors[i].getScheduledNodes().get(edge.getSrcNode());
                        int nodeCost = edge.getSrcNode().getComputationCost();
                        potentialTime = nodeStartTime + nodeCost;
                        // Dependency on another processor
                        if(targetProcessorID != i){
                            potentialTime += edge.getWeight();
                        }
                        if(potentialTime > latestTime) {
                            latestTime = potentialTime;
                        }
                    }
                }
            }

            processors[targetProcessorID].scheduleNodeAtTime(n, (int) Math.max(latestTime, processors[targetProcessorID].getCurrentEndTime()));
            scheduledTaskList.add(n);
            unscheduledTaskList.remove(n);
            workingProcessor = targetProcessorID;
            lastAddedNode = n;
            return true;
        } else {
            return false;
        }

    }

    public void setShortestPath(int shortestPath) {
        this.shortestPath = shortestPath;
    }

    private int shortestPath;

    public int getShortestPath() {
        return shortestPath;
    }


    public int getNumProcessors() {
        return numProcessors;
    }

    public List<Node> getScheduledTaskList() {
        return scheduledTaskList;
    }
}
