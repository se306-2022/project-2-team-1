package com.team01.scheduler.matrix.algorithm;

import com.team01.scheduler.matrix.model.Node;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PartialSchedule {

    private List<Processor> processors;
    private int lowerBound;

    public PartialSchedule(int numProcessors){
        for (int i = 0; i < numProcessors; i++) {
            processors.add(new Processor(i));
        }
    }

    public void setCostFunction(int lowerBound){
        this.lowerBound = lowerBound;
    }

    public List<Processor> getProcessors() {
        return processors;
    }

    public ArrayList<Node> getNodesInPartialSchedule() {
        ArrayList<Node> nodes = new ArrayList<>();

        for (Processor p : processors){
            nodes.addAll(p.getScheduledNodes());
        }

        return nodes;
    }

    public int getCostFunction(){
        return this.lowerBound;
    }

    public int getNumberOfNodes(){
        ArrayList<Node> nodes = new ArrayList<>();

        for (Processor p : processors){
            nodes.addAll(p.getScheduledNodes());
        }

        return nodes.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PartialSchedule that = (PartialSchedule) o;
        return lowerBound == that.lowerBound && Objects.equals(processors, that.processors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(processors, lowerBound);
    }

}
