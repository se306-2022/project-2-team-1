package com.team01.scheduler.graph.util;


import com.team01.scheduler.algorithm.Schedule;
import com.team01.scheduler.algorithm.ScheduledTask;
import com.team01.scheduler.graph.models.Edge;
import com.team01.scheduler.graph.models.Graph;
import com.team01.scheduler.graph.models.Node;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class ExportToDotFile {
    private Graph g;
    private String outputFileName;
    private Schedule schedule;

    public ExportToDotFile(Graph g, String outputFileName) {
        this.g = g;
        this.outputFileName = outputFileName;
    }
    public ExportToDotFile(Graph g, String outputFileName, Schedule schedule) {
        this.g = g;
        this.outputFileName = outputFileName;
        this.schedule = schedule;
    }

    /**
     * Generate dot file for graph only
     *
     * **/
    public void writeDot() throws IOException {
        try(BufferedWriter out=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFileName)))){
            out.write("digraph \""+outputFileName+"\" {");
            out.newLine();
            for (Node node: g.getNodes()){
                out.write(" "+node.getName()+" [weight="+node.getValue()+"];");
                out.newLine();
            }
            for (Edge edge: g.getEdges()){
                out.write(" "+edge.getSource().getName()+" -> "+edge.getTarget().getName()+" [weight="+edge.getWeight()+"];");
                out.newLine();
            }
            out.write("}");
        }
    }

    /**
     * Generate dot file for graph and schedule
     *
     * **/
    public void writeDotWithSchedule() throws IOException {
        try(BufferedWriter out=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFileName)))){
            out.write("digraph \""+outputFileName+"\" {");
            out.newLine();
            List<ScheduledTask> scheduledTasks= schedule.getScheduledTaskList();
            for (ScheduledTask scheduledTask: scheduledTasks){
                out.write(" "+scheduledTask.getNode().getName()+" [weight="+scheduledTask.getNode().getValue()+",Start="+scheduledTask.getStartTime()+",Processor="+scheduledTask.getProcessorId()+"];");
                out.newLine();
            }
            for (Edge edge: g.getEdges()){
                out.write(" "+edge.getSource().getName()+" -> "+edge.getTarget().getName()+" [weight="+edge.getWeight()+"];");
                out.newLine();
            }
            out.write("}");
        }
    }
}