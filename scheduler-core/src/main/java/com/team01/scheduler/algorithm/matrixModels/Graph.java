package com.team01.scheduler.algorithm.matrixModels;


import com.team01.scheduler.algorithm.matrixModels.exception.DuplicateEdgeException;
import com.team01.scheduler.algorithm.matrixModels.exception.InvalidEdgeException;
import com.team01.scheduler.algorithm.matrixModels.exception.NodeInvalidIDMapping;
import com.team01.scheduler.algorithm.matrixModels.exception.NonExistingNodeException;

import java.util.*;

public class Graph {

    private int[][] adjacencyMatrix;
    private final int SIZE;
    private ArrayList<Node> inputNodes;
    private ArrayList<Edge> inputEdges;
    private ArrayList<Node> entryNodes;
    private ArrayList<Node> exitNodes;

    public Graph(ArrayList<Node> inputNodes, ArrayList<Edge> inputEdges) {
        this.inputNodes = inputNodes;
        this.inputEdges = inputEdges;
        this.SIZE = inputNodes.size();
    }

    public int[][] getAdjacencyMatrix() {
        return adjacencyMatrix;
    }

    public int getNumberofNodes(){
        return SIZE;
    }

    public ArrayList<Node> getNodes(){
        return inputNodes;
    }

    public ArrayList<Edge> getEdges(){
        return inputEdges;
    }

    public void setInputNodes(ArrayList<Node> inputNodes) {
        this.inputNodes = inputNodes;
    }

    public void setInputEdges(ArrayList<Edge> inputEdges) {
        this.inputEdges = inputEdges;
    }

    /**
     * Main method which initializes the adjacency matrix based on the number of nodes
     * as specified by in the input from the graphviz parser class.
     * @throws DuplicateEdgeException
     * @throws InvalidEdgeException
     */
    public void initialize() throws DuplicateEdgeException, InvalidEdgeException {
        int size = inputNodes.size();
        adjacencyMatrix = new int[size][size];
        addEdges();
    }

    /**
     * Helper method parses the Edge class instances into the corresponding edges in
     * the adjacency matrix.
     * @throws DuplicateEdgeException
     * @throws InvalidEdgeException
     */
    private void addEdges() throws DuplicateEdgeException, InvalidEdgeException {
        for (Edge e : inputEdges){

            // check edge validity
            checkEdgeValidity(e.getSource(),e.getTarget());

            int row = e.getSource().getId();
            int dest = e.getSource().getId();
            int communicationCost = e.getWeight();

            if (adjacencyMatrix[row][dest] != 0){
                throw new DuplicateEdgeException("The Edge already exists in matrix, for: " + e);
            }
            adjacencyMatrix[row][dest] = communicationCost;
        }
    }

    /**
     * Helper method which gets the source nodes in the graph,
     * through identifying the nodes which
     * have no incoming edge.
     * @return
     * @throws NodeInvalidIDMapping
     */
    public List<Node> getEntryNodes() throws NodeInvalidIDMapping {
        entryNodes = new ArrayList<>();

        for (int j=0; j<SIZE; j++) {
            if (isEntryNode(adjacencyMatrix, j, SIZE)) {
                entryNodes.add(getNodeById(j));
            }
        }

        return entryNodes;
    }

    /**
     * Gets the leaf nodes for the graph through identifying the nodes
     * which have no outgoing edges in the adjacency matrix.
     * @return
     */
    public ArrayList<Node> getExitNodes() {
        exitNodes = new ArrayList<>();

        for (int i=0; i<SIZE; i++) {
            if (isExitNode(adjacencyMatrix, i, SIZE)) {
                exitNodes.add(getNodeById(i));
            }
        }

        return exitNodes;
    }

    /**
     * Returns all the nodes in the graph which have an incoming edge coming
     * from the node given as parameter for the method.
     * @param node
     * @return
     * @throws NonExistingNodeException
     */
    public ArrayList<Node> getChildrenForNode(Node node) {
        ArrayList<Node> childrenNodes = new ArrayList<>();
        // first check that the node indeed does exist in the graph
        if (!inputNodes.contains(node)){
            System.out.println("The node does not exist in the graph: " + node);
        }

        for (int j=0; j<SIZE; j++) {
            if (adjacencyMatrix[node.getId()][j] != 0) {
                childrenNodes.add(getNodeById(j));
            }
        }

        return childrenNodes;
    }

    /**
     * Returns all the nodes which have an outgoing edge to the node specifed in
     * the method argument.
     * @param node
     * @return
     * @throws NonExistingNodeException
     */
    public ArrayList<Node> getParentsForNode(Node node) {
        ArrayList<Node> parentNodes = new ArrayList<>();
        // first check that the node indeed does exist in the graph
        if (!inputNodes.contains(node)){
            System.out.println("The node does not exist in the graph: " + node);
        }

        for (int i=0; i<SIZE; i++) {
            if (adjacencyMatrix[i][node.getId()] != 0) {
                parentNodes.add(getNodeById(i));
            }
        }

        return parentNodes;
    }

    /**
     * Utility recursion function to iteratively sort the nodes until they are topologically sorted
     *
     * @param numberOfNodes     The number of nodes in graph
     * @param visited           The array to keep track of visited nodes
     * @param stack             The stack stores the order of nodes
     */
    private void topologicalSortRecursion(int numberOfNodes, boolean visited[], Stack stack) {
        visited[numberOfNodes] = true;
        Integer i;
        Iterator<Integer> iterator = Arrays.stream(adjacencyMatrix[numberOfNodes]).iterator();
        while (iterator.hasNext()) {
            i = iterator.next();
            if (!visited[i])
                topologicalSortRecursion(i, visited, stack);
        }

    }

    /**
     * Base topological sort function to order the nodes in order of precedence
     *
     * @param numberOfNodes     The number of nodes in the graph
     */
    private void topologicalSort(int numberOfNodes) {
        Stack stack = new Stack();

        boolean visited[] = new boolean[numberOfNodes];
        for (int i = 0; i < numberOfNodes; i++) {
            visited[i] = false;
        }

        for (int i = 0; i < numberOfNodes; i++) {
            if (visited[i] == false) {
                topologicalSortRecursion(i, visited, stack);
            }
        }

        //Print out all nodes
        //TODO return the stack as a list when getNodes() is called
        while (stack.empty() == false) {
            System.out.print(stack.pop() + " ");
        }
    }

    /**
     * Helper method for determing whether a given node is a leaf node.
     * @param matrix
     * @param nodeId
     * @param size
     * @return
     */
    private boolean isExitNode(int[][] matrix, int nodeId, int size) {
        for (int j=0; j<size; j++) {
            if (matrix[nodeId][j] != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Helper method for identifying whether a given node is a source node
     * in the given graph.
     * @param matrix
     * @param nodeId
     * @param size
     * @return
     */
    private boolean isEntryNode(int[][] matrix, int nodeId, int size) {
        for (int i=0; i<size; i++) {
            if (matrix[i][nodeId] != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Return the Node.class instance for a node given its unique "id" field.
     * @param id
     * @return
     * @throws NodeInvalidIDMapping
     */
    public Node getNodeById(int id) {
        for (Node n : inputNodes){
            if (n.getId() == id){
                return n;
            }
        }
        return null;
    }

    /**
     * Helper method which checks if an edge actually exists in the graph
     * @param source
     * @param dest
     * @throws InvalidEdgeException
     */
    private void checkEdgeValidity(Node source, Node dest) throws InvalidEdgeException {
        if (!inputNodes.contains(source) || !inputNodes.contains(dest)){
            throw new InvalidEdgeException("The Edge accesses a non-existing node.");
        }
    }


}
