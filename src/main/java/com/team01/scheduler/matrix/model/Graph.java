package com.team01.scheduler.matrix.model;

import com.team01.scheduler.matrix.exception.DuplicateEdgeException;
import com.team01.scheduler.matrix.exception.InvalidEdgeException;
import com.team01.scheduler.matrix.exception.NodeInvalidIDMapping;
import com.team01.scheduler.matrix.exception.NonExistingNodeException;

import java.util.ArrayList;
import java.util.List;

public class Graph {

    private int[][] adjacencyMatrix;
    private final int SIZE;
    private List<Node> inputNodes;
    private List<Edge> inputEdges;
    private List<Node> entryNodes;
    private List<Node> exitNodes;

    public Graph(List<Node> inputNodes, List<Edge> inputEdges) {
        this.inputNodes = inputNodes;
        this.inputEdges = inputEdges;
        this.SIZE = inputNodes.size();
    }

    public int[][] getAdjacencyMatrix() {
        return adjacencyMatrix;
    }

    public void setInputNodes(List<Node> inputNodes) {
        this.inputNodes = inputNodes;
    }

    public void setInputEdges(List<Edge> inputEdges) {
        this.inputEdges = inputEdges;
    }

    public void initialize() throws DuplicateEdgeException, InvalidEdgeException {
        int size = inputNodes.size();
        adjacencyMatrix = new int[size][size];
        addEdges();
    }

    private void addEdges() throws DuplicateEdgeException, InvalidEdgeException {
        for (Edge e : inputEdges){

            // check edge validity
            checkEdgeValidity(e.getSrcNode(),e.getDestNode());

            int row = e.getSrcNode().getId();
            int dest = e.getSrcNode().getId();
            int communicationCost = e.getWeight();

            if (adjacencyMatrix[row][dest] != 0){
                throw new DuplicateEdgeException("The Edge already exists in matrix, for: " + e);
            }
            adjacencyMatrix[row][dest] = communicationCost;
        }
    }

    public List<Node> getEntryNodes() throws NodeInvalidIDMapping {
        entryNodes = new ArrayList<>();

        for (int j=0; j<SIZE; j++) {
            if (isEntryNode(adjacencyMatrix, j, SIZE)) {
                entryNodes.add(getNodeById(j));
            }
        }

        return entryNodes;
    }

    public List<Node> getExitNodes() throws NodeInvalidIDMapping {
        exitNodes = new ArrayList<>();

        for (int i=0; i<SIZE; i++) {
            if (isExitNode(adjacencyMatrix, i, SIZE)) {
                exitNodes.add(getNodeById(i));
            }
        }

        return exitNodes;
    }

    public List<Node> getChildrenForNode(Node node) throws NonExistingNodeException, NodeInvalidIDMapping {
        List<Node> childrenNodes = new ArrayList<>();
        // first check that the node indeed does exist in the graph
        if (!inputNodes.contains(node)){
            throw new NonExistingNodeException("The node does not exist in the graph: " + node);
        }

        for (int j=0; j<SIZE; j++) {
            if (adjacencyMatrix[node.getId()][j] != 0) {
                childrenNodes.add(getNodeById(j));
            }
        }

        return childrenNodes;
    }

    public List<Node> getParentsForNode(Node node) throws NonExistingNodeException, NodeInvalidIDMapping {
        List<Node> parentNodes = new ArrayList<>();
        // first check that the node indeed does exist in the graph
        if (!inputNodes.contains(node)){
            throw new NonExistingNodeException("The node does not exist in the graph: " + node);
        }

        for (int i=0; i<SIZE; i++) {
            if (adjacencyMatrix[i][node.getId()] != 0) {
                parentNodes.add(getNodeById(i));
            }
        }

        return parentNodes;
    }


    private boolean isExitNode(int[][] matrix, int nodeId, int size) {
        for (int j=0; j<size; j++) {
            if (matrix[nodeId][j] != 0) {
                return false;
            }
        }
        return true;
    }

    private boolean isEntryNode(int[][] matrix, int nodeId, int size) {
        for (int i=0; i<size; i++) {
            if (matrix[i][nodeId] != 0) {
                return false;
            }
        }
        return true;
    }

    private Node getNodeById(int id) throws NodeInvalidIDMapping {
        for (Node n : inputNodes){
            if (n.getId() == id){
                return n;
            }
        }
        throw new NodeInvalidIDMapping("Can't find Node for given ID");
    }

    private void checkEdgeValidity(Node source, Node dest) throws InvalidEdgeException {
        if (!inputNodes.contains(source) || !inputNodes.contains(dest)){
            throw new InvalidEdgeException("The Edge accesses a non-existing node.");
        }
    }

}
