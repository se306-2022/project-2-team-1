package com.digraph.weighted.exceptions;

public class CycleException extends Exception{
    public CycleException(String message){
        super(message);
    }
}
