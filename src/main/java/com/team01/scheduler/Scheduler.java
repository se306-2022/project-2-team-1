package com.team01.scheduler;

public class Scheduler {

    private List<Nodes> nodesList;

    public List<Nodes> getNodesList() {
        return nodesList;
    }

    public Scheduler(List<Nodes> nodesList) {
        this.nodesList = nodesList;
    }
}