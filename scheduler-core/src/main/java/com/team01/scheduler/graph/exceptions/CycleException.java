package com.team01.scheduler.graph.exceptions;

public class CycleException extends Exception{
    public CycleException(String message){
        super(message);
    }
}
