package com.team01.scheduler.matrix.algorithm;

import com.team01.scheduler.graph.exceptions.InvalidPositionException;
import com.team01.scheduler.graph.models.Edge;
import com.team01.scheduler.graph.models.Node;
import com.team01.scheduler.matrix.model.Graph;

import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class AStarScheduler {
    /**
     * Adjacency Matrix based implementation for the A* star exhaustive search algorithm.
     * Iterates through each possible partial schedule for the given number of processors and
     * prunes ones which aren't in contention to be the shortest path based on the cost function
     * heuristic.
     */
    private Graph adjacencyMatrix;
    private PriorityQueue<PartialSchedule> priorityQueue = new PriorityQueue<>(new CostFunctionComparator());

    private class Task {

        private int startTime;

        private int processorID;

        private Node node;

        public Task (int startTime, int processorID, Node node) {
            this.startTime = startTime;
            this.processorID = processorID;
            this.node = node;
        }
    }

    private class ScheduleTreeNode {

        private int numProcessors;
        private List<Task> taskList;

        public ScheduleTreeNode(List<Task> taskList, int numProcessors) {
            this.taskList = taskList;
            this.numProcessors = numProcessors;
        }

    }

    private class ScheduleTreeEdge {

        private ScheduleTreeNode source;
        private ScheduleTreeNode dest;

        private ScheduleTreeEdge next;

        public ScheduleTreeEdge(ScheduleTreeNode source, ScheduleTreeNode dest) {
            this.source = source;
            this.dest = dest;
        }

        public void setNext(ScheduleTreeEdge next) {
            this.next = next;
        }

        public ScheduleTreeEdge getNext() {
            return this.next;
        }

        public ScheduleTreeNode getSource() {
            return this.source;
        }

        public ScheduleTreeNode getDest() {
            return this.dest;
        }

    }

    private class ScheduleLinkedList {

        private ScheduleTreeEdge head;

        public ScheduleLinkedList(ScheduleTreeEdge head) {
            this.head = head;
        }

        public void append(ScheduleTreeEdge edge) {
            int listSize = size();

            if (listSize == 0) {
                head = edge;
            } else {
                ScheduleTreeEdge temp = get(listSize - 1);
                temp.setNext(edge);
            }
        }

        public int size() {
            int count = 0;
            ScheduleTreeEdge edge = head;
            while(edge != null) {
                count++;
                edge = edge.getNext();
            }
            return count;
        }

        public ScheduleTreeEdge get(int pos) {

            if (pos > size() || pos < 0) {
                return null;
            }

            int count = 0;
            ScheduleTreeEdge currentEdge = head;
            if (pos == 0) {
                return head;
            } else {
                while (currentEdge != null) {
                    if (count == pos) {
                        return currentEdge;
                    }
                    currentEdge = currentEdge.getNext();
                    count++;
                }
            }

            return null;
        }

    }

    private class ScheduleTree {

        private Map<ScheduleTreeNode, ScheduleLinkedList> adjacencyMap;

        private ScheduleTreeNode root;

        private List<ScheduleTreeNode> nodes;

        public ScheduleTree() {

        }

    }

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
