package com.team01.scheduler.matrix.algorithm;

import com.team01.scheduler.matrix.model.Node;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PartialSchedule {
    /**
     * List of processors which the nodes are scheduled on
     * in the current partial schedule
     */
    private List<Processor> processors;

    /**
     * Stores the lower bound value for the partial schedule,
     * calculated by the CostFunctionCalculator.class
     */
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

    /**
     * Returns all the nodes/tasks which are part of this
     * partial schedule instance.
     * @return
     */
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

    /**
     * Returns the count for the number of nodes/tasks
     * part of this instance of partial schedule
     * @return
     */
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

    /**
     * Equals method needs a corresponding hash code method
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(processors, lowerBound);
    }

}
