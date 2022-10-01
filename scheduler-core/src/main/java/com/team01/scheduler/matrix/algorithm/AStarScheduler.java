package com.team01.scheduler.matrix.algorithm;

import com.team01.scheduler.matrix.model.Graph;
import java.util.PriorityQueue;

public class AStarScheduler {

    private Graph adjacencyMatrix;
    private PriorityQueue<PartialSchedule> priorityQueue = new PriorityQueue<>(new CostFunctionComparator());

    public AStarScheduler(Graph adjacencyMatrix){
        this.adjacencyMatrix = adjacencyMatrix;
    }

    // minheap priority queue which returns partial schedules with the lowest cost function/ lower bound value
    // how would we determine if it is a complete solution
        // if the last node in partial solution is a leaf
        // or if partial solution contains all the nodes in the adjacency matrix graph
    // what would be added initially into the pq, since there can be multiple entry nodes into the graph

    // we will need way to determine cost function for each partial schedule >> DONE
    // need to take into account nodes in a partial schedule are scheduled on different processors
}
