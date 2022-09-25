package com.digraph.weighted.util;


import com.digraph.weighted.models.Edge;
import com.digraph.weighted.models.Graph;
import com.digraph.weighted.models.Node;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

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
            for(Node node: g.getNodes()){
                out.write(" "+node.getName()+" [weight="+node.getValue()+"]");
                out.newLine();
            }
            for(Edge edge: g.getEdges()){
                out.write(" "+edge.getSource().getValue()+" -> "+edge.getTarget().getValue()+" [weight="+edge.getWeight()+"]");
                out.newLine();
            }
            out.write("}");
        }
    }
}
