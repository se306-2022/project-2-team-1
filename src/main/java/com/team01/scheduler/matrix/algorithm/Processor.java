package com.team01.scheduler.matrix.algorithm;

import com.team01.scheduler.matrix.exception.NodeNotScheduledException;
import com.team01.scheduler.matrix.exception.NotImplementedException;
import com.team01.scheduler.matrix.model.Node;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Processor {
    /**
     * the key is the node and the value is the start time scheduled for the node
     */
    private Map<Node,Integer> scheduledNodes;
    /**
     * The key is the order number of the node and the value is the actual node
     */
    private Map<Integer,Node> orderOfNodes;
    /**
     * current end time of the processor
     */
    private int currentEndTime;

    /**
     * Processor id
     */
    private int pid;

    public Processor(int id){
        this.pid = id;
        scheduledNodes = new LinkedHashMap<>();
        orderOfNodes = new LinkedHashMap<>();
        currentEndTime = 0;
    }

    /**
     * Returns a arraylist of the scheduled nodes. The nodes aren't ordered
     * and don't include the scheduling start times.
     * @return
     */
    public ArrayList<Node> getScheduledNodes(){
        return new ArrayList<Node>(scheduledNodes.keySet());
    }

    public Integer getStartTimeForNode(Node node) throws NodeNotScheduledException {
        if (scheduledNodes.containsKey(node)){
            return scheduledNodes.get(node);
        } else {
            throw new NodeNotScheduledException("This node has not been scheduled on the processor");
        }
    }

    public void scheduleNode(Node node, PartialSchedule ps) throws NotImplementedException {
        int order = scheduledNodes.size();
        orderOfNodes.put(order, node);
        scheduledNodes.put(node, calculateStartTime(node,ps));
        currentEndTime = scheduledNodes.get(node) + node.getComputationCost();
    }

    /**
     * Calculates the current total idle time of the processor
     * @return
     */

    public double calculateIdleTime() {
        int idleTime = 0;

        // If only one node is scheduled, get start time of the node
        if (scheduledNodes.size() == 1) {
            idleTime = scheduledNodes.get(orderOfNodes.get(0));
        } else {

            // Go through each node that is in the processor
            for (Integer i : orderOfNodes.keySet()) {
                if (i == 0) {
                    continue;
                }
                int startOfCurrentNode = scheduledNodes.get(orderOfNodes.get(i));
                int CostOfLastNode = orderOfNodes.get(i - 1).getComputationCost();
                int startOfLastNode = scheduledNodes.get(orderOfNodes.get(i - 1));

                // Calculate any idle times and add it to the total idle time
                if ((startOfCurrentNode) != (startOfLastNode + CostOfLastNode)) {
                    idleTime = idleTime + ((startOfCurrentNode) - (startOfLastNode + CostOfLastNode));
                }
            }
        }
        return idleTime;
    }

    private Integer calculateStartTime(Node node, PartialSchedule ps) throws NotImplementedException {
        //TODO: How would you calculate the start time of a node in the schedule
        throw new NotImplementedException("Method to implement");
    }

}
