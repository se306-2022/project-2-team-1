package com.team01.scheduler.graph.models;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GraphController {

    private Graph graph;

    private void parseGraphviz(BufferedReader br) throws IOException {
        List<Node> nodes = new ArrayList<>();
        List<Node> possibleStartNodes = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();
        String input;

        while ((input = br.readLine()) != null) {

            // Ignore first and last line or those that dont have "weight"
            if (input.contains("}") || input.contains("{") || !input.contains("Weight")) {
                continue;
            }

            input = input.replaceAll("\\s+","");
            input = input.trim();
            int arrowIndex = input.indexOf("->");
            int bracketIndex = input.indexOf("[");
            int weightIndex = input.indexOf("Weight=");
            int commaIndex = input.indexOf(",");
            int endBracketIndex = input.indexOf("]");
            if(arrowIndex!= -1){
                // is an edge
                String source = input.substring(0,arrowIndex);
                String target = input.substring(arrowIndex+2, bracketIndex);
                int weight;
                if(commaIndex == -1){
                    weight = Integer.parseInt(input.substring(weightIndex+7,endBracketIndex));
                }else{
                    weight = Integer.parseInt(input.substring(weightIndex+7,commaIndex));
                }
                Optional<Node> tempSource = Node.containsName(nodes, source);
                Node sourceNode;
                Node targetNode;
                if (tempSource.isPresent()) {
                    sourceNode = tempSource.get();
                } else {
                    sourceNode = new Node(source, 0);
                    nodes.add(sourceNode);
                }

                // Check if target node has already been read
                Optional<Node> tempTarget = Node.containsName(nodes, target);
                if (tempTarget.isPresent()) {
                    targetNode = tempTarget.get();
                } else {
                    targetNode = new Node(target, 0);
                    nodes.add(targetNode);
                }
                edges.add(new Edge(sourceNode, targetNode, weight));

            }else{
                // node
                String source = input.substring(0,bracketIndex);
                int weight;
                if(commaIndex == -1){
                    weight = Integer.parseInt(input.substring(weightIndex+7,endBracketIndex));
                }else{
                    weight = Integer.parseInt(input.substring(weightIndex+7,commaIndex));
                }
                nodes.add(new Node(source, weight));
            }

            }



        this.graph = new Graph(edges, nodes);
    }

    public GraphController(String contents) throws IOException {
        var reader = new BufferedReader(new StringReader(contents));
        parseGraphviz(reader);
    }

    public GraphController(File file) throws IOException {
        var reader = new BufferedReader(new FileReader(file));
        parseGraphviz(reader);
    }

    public Graph getGraph() {
        return this.graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

}
