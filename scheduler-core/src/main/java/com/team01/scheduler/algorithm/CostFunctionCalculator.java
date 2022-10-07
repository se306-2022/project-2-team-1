package com.team01.scheduler.algorithm;

import com.team01.scheduler.matrix.algorithm.PartialSchedule;
import com.team01.scheduler.matrix.algorithm.Processor;
import com.team01.scheduler.matrix.exception.NodeInvalidIDMapping;
import com.team01.scheduler.matrix.exception.NodeNotScheduledException;
import com.team01.scheduler.matrix.exception.NonExistingNodeException;
import com.team01.scheduler.algorithm.matrixModels.Node;
import com.team01.scheduler.algorithm.matrixModels.Graph;
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

    /**
     * Singleton design pattern so that only one instance is created of this class.
     * @return
     */
    public static CostFunctionCalculator getInstance(){
        return (instance == null) ? instance = new CostFunctionCalculator() : instance;
    }

    /**
     * Given a partial schedule this method is called to find the corresponding cost function,
     * which is used as the heuristic to determine whether we should continue exploring the given
     * partial schedule.
     * @param partialSchedule
     * @param processorsList
     * @param g
     * @return
     * @throws NonExistingNodeException
     * @throws NodeInvalidIDMapping
     * @throws NodeNotScheduledException
     */
    public Integer findCostFunction(PartialSchedule partialSchedule, ArrayList<Processor> processorsList, Graph g) throws NonExistingNodeException, NodeInvalidIDMapping, NodeNotScheduledException {
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

    /**
     * Helper function which calculates the bottom level for a given node in the adjacency matrix
     * @param node
     * @return
     * @throws NonExistingNodeException
     * @throws NodeInvalidIDMapping
     */
    private Integer calculateBottomLevel(Node node) {
        int bottomLevel = node.getComputationCost();
        ArrayList<Node> childrenNodes = adjacencyMatrix.getChildrenForNode(node);

        while (!childrenNodes.isEmpty()){
            for (Node n : childrenNodes){
                bottomLevel += n.getComputationCost();
            }
        }

        return bottomLevel;
    }

    /**
     * Helper function which given the bottom level and the starting time for each node/task
     * in a partial schedule, finds the node with the highest summation of the two.
     * The returned Lower bound summation is used as the heuristic for deciding whether to
     * continue with exploring that partial schedule or not.
     * @param bottomLevels
     * @param startingTimes
     * @param partialScheduleNodes
     * @return
     */
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
