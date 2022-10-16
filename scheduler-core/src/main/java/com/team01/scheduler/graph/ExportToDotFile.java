package com.team01.scheduler.graph;

import com.team01.scheduler.algorithm.Schedule;
import com.team01.scheduler.algorithm.ScheduledTask;
import com.team01.scheduler.graph.model.Edge;
import com.team01.scheduler.graph.model.Graph;
import com.team01.scheduler.graph.model.Node;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class ExportToDotFile {
    private Graph graph;
    private String outputFileName;
    private Schedule schedule;

    /**
     * Constructor for exporting without schedule
     *
     * **/
    public ExportToDotFile(Graph graph, String outputFileName) {
        this.graph = graph;
        this.outputFileName = outputFileName;
    }

    /**
     * Constructor for exporting with a schedule
     *
     *
     * **/
    public ExportToDotFile(Graph graph, String outputFileName, Schedule schedule) {
        this.graph = graph;
        this.outputFileName = outputFileName;
        this.schedule = schedule;
    }

    /**
     * Generate dot file for graph without schedule
     * @throws IOException unable to create new file
     */
    public void writeDot() throws IOException {
        try(BufferedWriter out=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFileName)))){
            out.write("digraph \""+outputFileName+"\" {");
            out.newLine();
            // add all nodes and their corresponding values.
            for (Node node: graph.getNodes()){
                out.write(" "+node.getName()+" [Weight="+node.getComputationCost()+"];");
                out.newLine();
            }
            // add a entry for every edge
            for (Edge edge: graph.getEdges()){
                out.write(" "+edge.getSource().getName()+" -> "+edge.getTarget().getName()+" [Weight="+edge.getWeight()+"];");
                out.newLine();
            }
            out.write("}");
        }
    }


    /**
     * Generate dot file for graph and schedule
     * @throws IOException unable to create new file
     */
    public void writeDotWithSchedule() throws IOException {
        try(BufferedWriter out=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFileName)))){
            out.write("digraph \""+outputFileName+"\" {");
            out.newLine();
            List<ScheduledTask> scheduledTasks= schedule.getScheduledTaskList();
            // add a line for every node, containing its length, start and processor id
            for (ScheduledTask scheduledTask: scheduledTasks){
                out.write(" "+scheduledTask.getNode().getName()+" [Weight="+scheduledTask.getNode().getComputationCost()+",Start="+scheduledTask.getStartTime()+",Processor="+scheduledTask.getProcessorId()+"];");
                out.newLine();
            }
            // add a entry for every edge
            for (Edge edge: graph.getEdges()){
                out.write(" "+edge.getSource().getName()+" -> "+edge.getTarget().getName()+" [Weight="+edge.getWeight()+"];");
                out.newLine();
            }
            out.write("}");
        }
    }
}