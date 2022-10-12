package com.team01.scheduler.matrix.algorithm;

import com.team01.scheduler.algorithm.IRunnable;
import com.team01.scheduler.algorithm.Schedule;
import com.team01.scheduler.graph.models.Edge;
import com.team01.scheduler.graph.models.EdgesLinkedList;
import com.team01.scheduler.graph.models.Node;
import com.team01.scheduler.graph.models.Graph;

import java.util.*;

public class AStarScheduler implements IRunnable {
    /**
     * Adjacency Matrix based implementation for the A* star exhaustive search algorithm.
     * Iterates through each possible partial schedule for the given number of processors and
     * prunes ones which aren't in contention to be the shortest path based on the cost function
     * heuristic.
     */

    /**
     * 1. Put the initial state sinit in the OPEN list. The states in the OPEN list are sorted
     * according to cost function f (s).
     * 2. Remove from OPEN the state s with the best f value.
     * 3. Test s for the goal condition. If true, then a solution has been found and the algorithm terminates.
     * 4.      (i) Expand s to form new states.
     *         (ii) For each new state check whether it is present in either the CLOSED or
     *              OPEN list. If yes, discard the state; otherwise, insert it into the OPEN list.
     *         (iii) Place s in the CLOSED list.
     * 5. Go to Step 2.
     * **/
    /**
     * For one start node:
     * For processor in num processors:
     * 	Schedule start node on processor
     * 	Queue schedule
     * While (queue is not empty)
     * 	Get first schedule in queue
     * 	For adj edges:
     * Schedule nodes and add schedule to queue
     */

    private Graph graph;

    private ScheduleTreeNode root;
    private ScheduleTreeNode currentBestSchedule;

    private PriorityQueue<ScheduleTreeNode> openList;
    private PriorityQueue<ScheduleTreeNode> closedList;
    private HashSet<ScheduleTreeNode> uniqueSchedules = new HashSet<>();
    private int numProcessors;

    public AStarScheduler(Graph graph, int numProcessors){
        this.graph = graph;
        this.numProcessors = numProcessors;
        // add comparator
        openList = new PriorityQueue<>();
        closedList = new PriorityQueue<>();

        createRootNode();
    }

    private void createRootNode() {
        Map<Integer, List<Task>> initialMap = new HashMap<>();

        for (int i = 1; i < root.numProcessors + 1; i++) {
            initialMap.put(i, new ArrayList<Task>());
        }

        root = new ScheduleTreeNode(initialMap, this.numProcessors);
        openList.add(root);
    }

    @Override
    public String getTaskName() {
        return "A Star Scheduler";
    }

    @Override
    public Schedule run(Graph graph, int numProcessors) {

        Node startNode = graph.getPossibleStartNodes().get(0);
        Node currentNode = startNode;

        while (!openList.isEmpty()) {
            // remove and examine schedule with lowest f value
            ScheduleTreeNode currentSchedule = openList.poll();
            // if schedule is complete (no nodes to schedule, current is faster than best and not null) we are done, return currentSchedule
            if(currentSchedule.schedule.values().size() == graph.getNodes().size() ||
                    currentSchedule.getCurrentShortestPath()< currentBestSchedule.getCurrentShortestPath()) {
                currentBestSchedule = currentSchedule;
                System.out.println(currentBestSchedule.currentShortestPath);
            }

            List<ScheduleTreeNode> newChildrenSchedules= CreateChildSchedules(currentSchedule, graph.getAdjacencyMap().get(currentNode));
            currentSchedule.adjNodes = newChildrenSchedules;
            // if schedule is faster than best
                // Expand schedule to form new states (schedule all available nodes to all available processors)
                // For each new state check whether it is in either closed or open list
                    // If yes: discard the schedule
                // Place schedule in closed list.
            for(ScheduleTreeNode childSchedule:newChildrenSchedules){
                if(!uniqueSchedules.contains(childSchedule)){
                    openList.add(childSchedule);
                    uniqueSchedules.add(childSchedule);
                }
            }
                
            openList.remove(currentSchedule);

        }
        return null;
    }

    public List<ScheduleTreeNode> CreateChildSchedules(ScheduleTreeNode parent, EdgesLinkedList adjEdges) {

        List<ScheduleTreeNode> childSchedules = new ArrayList<>();

        for (Edge edge : adjEdges) {
            ScheduleTreeNode clone = parent.clone(parent);
            Node targetNode = edge.getTarget();
            int compTime = targetNode.getValue();
            for (int i =0; i < parent.numProcessors; i++) {
                // TODO consider when last scheduled node was on a different processors and therefore incurs extra time
                int startTime = parent.processorBusyTime[i];
                // map of processorID and corresponding list of tasks
                Map<Integer, List<Task>> childSchedule  = new HashMap<>();
                childSchedule = parent.schedule;
                childSchedule.put(i, (List<Task>) new Task(startTime, i, targetNode));
                ScheduleTreeNode scheduleTreeNode = new ScheduleTreeNode(childSchedule, parent.numProcessors, parent);
                scheduleTreeNode.processorBusyTime[i] = scheduleTreeNode.processorBusyTime[i] + compTime;
                childSchedules.add(scheduleTreeNode);
            }
        }
        return childSchedules;
    }

    //private PriorityQueue<PartialSchedule> priorityQueue = new PriorityQueue<>(new CostFunctionComparator());

    private class Task {
        private int startTime;
        private int processorID;
        public Node node;

        public Task (int startTime, int processorID, Node node) {
            this.startTime = startTime;
            this.processorID = processorID;
            this.node = node;
        }
    }

    private class ScheduleTreeNode {

        private int numProcessors;
        private Map<Integer, List<Task>> schedule;

        private List<ScheduleTreeNode> adjNodes;

        private ScheduleTreeNode parent;

        private int[] processorBusyTime;
        private int currentShortestPath;
        private Node lastScheduledNode;

        public int getCurrentShortestPath() {
            return currentShortestPath;
        }

        public ScheduleTreeNode(Map<Integer, List<Task>> schedule, int numProcessors, ScheduleTreeNode parent) {
            this.schedule = schedule;
            this.numProcessors = numProcessors;
            this.parent = parent;
            this.processorBusyTime = new int[numProcessors];
        }

        public ScheduleTreeNode(Map<Integer, List<Task>> schedule, int numProcessors) {
            this.schedule = schedule;
            this.numProcessors = numProcessors;
            this.processorBusyTime = new int[numProcessors];
        }

        public void append(ScheduleTreeNode node) {
            this.adjNodes.add(node);
        }

        public ScheduleTreeNode clone(ScheduleTreeNode node) {

            HashMap<Integer, List<Task>> mapCopy = new HashMap<>();

            for (var something : node.schedule.entrySet()) {
                var processorId = something.getKey();
                var list = something.getValue();

                var clonedList = new ArrayList<>(list);
                mapCopy.put(processorId, clonedList);
            }

            int[] array = Arrays.copyOf(node.processorBusyTime, node.processorBusyTime.length);
            ScheduleTreeNode newTing = new ScheduleTreeNode(mapCopy, node.numProcessors, node);
            newTing.processorBusyTime = array;

            return newTing;
        }

    }

//    private class ScheduleTreeEdge {
//
//        private ScheduleTreeNode source;
//        private ScheduleTreeNode dest;
//
//        private ScheduleTreeEdge next;
//
//        public ScheduleTreeEdge(ScheduleTreeNode source, ScheduleTreeNode dest) {
//            this.source = source;
//            this.dest = dest;
//        }
//
//        public void setNext(ScheduleTreeEdge next) {
//            this.next = next;
//        }
//
//        public ScheduleTreeEdge getNext() {
//            return this.next;
//        }
//
//        public ScheduleTreeNode getSource() {
//            return this.source;
//        }
//
//        public ScheduleTreeNode getDest() {
//            return this.dest;
//        }
//
//    }

//    private class ScheduleLinkedList {
//
//        private ScheduleTreeEdge head;
//
//        public ScheduleLinkedList(ScheduleTreeEdge head) {
//            this.head = head;
//        }
//
//        public void append(ScheduleTreeEdge edge) {
//            int listSize = size();
//
//            if (listSize == 0) {
//                head = edge;
//            } else {
//                ScheduleTreeEdge temp = get(listSize - 1);
//                temp.setNext(edge);
//            }
//        }
//
//        public int size() {
//            int count = 0;
//            ScheduleTreeEdge edge = head;
//            while(edge != null) {
//                count++;
//                edge = edge.getNext();
//            }
//            return count;
//        }
//
//        public ScheduleTreeEdge get(int pos) {
//
//            if (pos > size() || pos < 0) {
//                return null;
//            }
//
//            int count = 0;
//            ScheduleTreeEdge currentEdge = head;
//            if (pos == 0) {
//                return head;
//            } else {
//                while (currentEdge != null) {
//                    if (count == pos) {
//                        return currentEdge;
//                    }
//                    currentEdge = currentEdge.getNext();
//                    count++;
//                }
//            }
//
//            return null;
//        }
//
//    }

//    private class ScheduleTree {
//
//        private Map<ScheduleTreeNode, ScheduleLinkedList> adjacencyMap;
//
//        private ScheduleTreeNode root;
//
//        private List<ScheduleTreeNode> nodes;
//
//        public ScheduleTree() {
//
//        }
//
//    }

    // minheap priority queue which returns partial schedules with the lowest cost function/ lower bound value
    // how would we determine if it is a complete solution
        // if the last node in partial solution is a leaf
        // or if partial solution contains all the nodes in the adjacency matrix graph
    // what would be added initially into the pq, since there can be multiple entry nodes into the graph

    // we will need way to determine cost function for each partial schedule >> DONE
    // need to take into account nodes in a partial schedule are scheduled on different processors

    // Steps:
    // Maintain two lists, OPEN and CLOSED, to search the state space
    // OPEN list(priority queue) holds all the stands awaiting expansion.
    // CLOSED list holds all states that have been fully expanded




}
