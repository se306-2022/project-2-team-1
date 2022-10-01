package com.team01.scheduler.matrix.algorithm;

import com.team01.scheduler.matrix.exception.NodeInvalidIDMapping;
import com.team01.scheduler.matrix.exception.NodeNotScheduledException;
import com.team01.scheduler.matrix.exception.NonExistingNodeException;
import com.team01.scheduler.matrix.model.Graph;
import com.team01.scheduler.matrix.model.Node;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * lowerbound/costfunction = max(bottom level + starttime for every node in partial schedule)
 */
public class CostFunctionCalculator {
    private static CostFunctionCalculator instance;
    private HashMap<Node,Integer> bottomLevels = new HashMap<>();
    private Graph adjacencyMatrix;

    private CostFunctionCalculator(){
    }

    public static CostFunctionCalculator getInstance(){
        return (instance == null) ? instance = new CostFunctionCalculator() : instance;
    }

    public Integer findCostFunction(PartialSchedule partialSchedule, ArrayList<Processor> processorsList,Graph g) throws NonExistingNodeException, NodeInvalidIDMapping, NodeNotScheduledException {
        adjacencyMatrix = g;
        HashMap<Node,Integer> bottomLevels = new HashMap<>();
        HashMap<Node,Integer> startingTimes = new HashMap<>();

        for (Node n : partialSchedule.getNodesInPartialSchedule()){

            bottomLevels.put(n, calculateBottomLevel(n));

            for (Processor processor : processorsList){
                if (processor.getScheduledNodes().contains(n)){
                    startingTimes.put(n,processor.getStartTimeForNode(n));
                }
            }
        }

        return findMaxLowerBound(bottomLevels,startingTimes,partialSchedule.getNodesInPartialSchedule());
    }

    private Integer calculateBottomLevel(Node node) throws NonExistingNodeException, NodeInvalidIDMapping {
        int bottomLevel = node.getComputationCost();
        ArrayList<Node> childrenNodes = adjacencyMatrix.getChildrenForNode(node);

        while (!childrenNodes.isEmpty()){
            for (Node n : childrenNodes){
                bottomLevel += n.getComputationCost();
            }
        }

        return bottomLevel;
    }

    private Integer findMaxLowerBound( HashMap<Node,Integer> bottomLevels, HashMap<Node,Integer> startingTimes, ArrayList<Node> partialScheduleNodes){
        HashMap<Node, Integer> lowerBounds = new HashMap<>();

        for (Node node : partialScheduleNodes){
            int lowerBound = bottomLevels.get(node) + startingTimes.get(node);
            lowerBounds.put(node, lowerBound);
        }

        int maxLowerBound = 0;
        for (Integer lb : lowerBounds.values()){
            if (lb > maxLowerBound){
                maxLowerBound = lb;
            }
        }

        return maxLowerBound;
    }
}
