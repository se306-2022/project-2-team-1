package com.team01.scheduler.algorithm;

import com.team01.scheduler.matrix.exception.NodeInvalidIDMapping;
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
     * @param visitedNodes
     * @param st
     * @param g
     * @return lowerBound
     */
    public Integer findCostFunction(ArrayList<Node> visitedNodes, ScheduledTask st, Graph g) {
        adjacencyMatrix = g;
        HashMap<Node,Integer> bottomLevels = new HashMap<>();
        HashMap<Node,Integer> startingTimes = new HashMap<>();

        for (Node n : visitedNodes){

            // calculate the bottom level for the node
            bottomLevels.put(n, calculateBottomLevel(n));

            // find the start time for node n
            while (st != null) {
                if (st.getNode() == n){
                    startingTimes.put(n,st.getStartTime());
                }
                st = st.parent;
            }
        }

        return findMaxLowerBound(bottomLevels,startingTimes,visitedNodes);
    }

    /**
     * Helper function which calculates the bottom level for a given node in the adjacency matrix
     * Bottom level is the longest path to leaf node
     * @param node
     * @return
     * @throws NonExistingNodeException
     * @throws NodeInvalidIDMapping
     */

    private Integer calculateBottomLevel(Node node) {

        return traverseChildrenNode(node);
    }

    public int traverseChildrenNode(Node startingNode){
        int bottomLevel = 0;
        ArrayList<Node> leafNodes = adjacencyMatrix.getExitNodes();
        ArrayList<Node> childrenNodes = adjacencyMatrix.getChildrenForNode(startingNode);

        // create hash map
        HashMap<Integer,Node> bottomLevelsPerNode = new HashMap<>();
        for (Node cn : childrenNodes){
            bottomLevelsPerNode.put(cn.getComputationCost(),cn);
        }


        int temp = startingNode.getComputationCost();
        for (Node cn : childrenNodes){
            temp += cn.getComputationCost();
            if (!leafNodes.contains(cn)){
                traverseChildrenNode(cn);
            } else {
                if (temp > bottomLevel){
                    bottomLevel = temp;
                    // reset temp
                    temp = startingNode.getComputationCost();
                }
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
