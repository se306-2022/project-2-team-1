package com.team01.scheduler.graph.util;


import com.team01.scheduler.graph.models.Edge;
import com.team01.scheduler.graph.models.Graph;
import com.team01.scheduler.graph.models.Node;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
public class ExportToDotFile {
    private Graph g;
    private String outputFileName;
  
    public ExportToDotFile(Graph g, String outputFileName) {
        this.g = g;
        this.outputFileName = outputFileName;
    }

    public void writeDot() throws IOException {
        try(BufferedWriter out=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFileName)))){
            out.write("digraph \"OutputExample\" {");
            out.newLine();
            for (Node node: g.getNodes()){
                out.write(" "+node.getName()+" [weight="+node.getValue()+"]");
                out.newLine();
            }
            for (Edge edge: g.getEdges()){
                out.write(" "+edge.getSource().getName()+" -> "+edge.getTarget().getName()+" [weight="+edge.getWeight()+"]");
                out.newLine();
            }
            out.write("}");
        }
    }
}
