package com.team01.scheduler.algorithm.astar;

import com.team01.scheduler.algorithm.ScheduledTask;
import com.team01.scheduler.graph.exceptions.NodeInvalidIDMapping;
import com.team01.scheduler.graph.exceptions.NonExistingNodeException;
import com.team01.scheduler.graph.model.Node;
import com.team01.scheduler.graph.model.Graph;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * lowerbound/costfunction = max(bottom level + starttime for every node in partial schedule)
 */
public class CostFunctionCalculator {
    private static CostFunctionCalculator instance;
    public static HashMap<Node,Integer> bottomLevels = new HashMap<>();
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
     * @return lowerBound
     */
    public Integer findCostFunction(ArrayList<Node> visitedNodes, ScheduledTask st ) {
        HashMap<Node,Integer> startingTimes = new HashMap<>();

        // find the start time for node n
        while (st != null) {
            startingTimes.put(st.getNode(),st.getStartTime());
            st = st.parent;
        }

        return findMaxLowerBound(startingTimes, visitedNodes);
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
        return bottomLevels.get(node);
    }


    public void setGraph(Graph g){
        this.adjacencyMatrix = g;
    }

    /**
     * Function to calculate the bottom level of a node
     * @param node
     * @return int: bottom level value
     */
    public int setBottomLevel(Node node) {
        for (Node s : adjacencyMatrix.getChildrenForNode(node)) {
            int childBottomLevel = setBottomLevel(s);
            bottomLevels.put(s, childBottomLevel);
        }
        ArrayList<Integer> childrenBottomLevels = new ArrayList<>();

        for(Node n : adjacencyMatrix.getChildrenForNode(node)){
            childrenBottomLevels.add(bottomLevels.get(n));
        }

        int bottomLevel;
        if(childrenBottomLevels.isEmpty()){
            bottomLevel = node.getComputationCost();
        } else{
            bottomLevel = Collections.max(childrenBottomLevels) + node.getComputationCost();
        }
        bottomLevels.put(node,bottomLevel);
        return bottomLevel;
    }


    /**
     * Helper function which given the bottom level and the starting time for each node/task
     * in a partial schedule, finds the node with the highest summation of the two.
     * The returned Lower bound summation is used as the heuristic for deciding whether to
     * continue with exploring that partial schedule or not.
     * @param startingTimes
     * @param partialScheduleNodes
     * @return
     */
    private Integer findMaxLowerBound(HashMap<Node,Integer> startingTimes, ArrayList<Node> partialScheduleNodes){
        HashMap<Node, Integer> lowerBounds = new HashMap<>();

        for (Node node : partialScheduleNodes){
            int bottomLevel = bottomLevels.get(node);
            int startingTime = startingTimes.get(node);
            int lowerBound = bottomLevel + startingTime;
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
