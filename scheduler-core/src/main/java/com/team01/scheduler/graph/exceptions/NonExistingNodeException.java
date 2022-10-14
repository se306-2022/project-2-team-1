package com.team01.scheduler.graph.exceptions;

public class NonExistingNodeException extends RuntimeException{

    public NonExistingNodeException(String message){
        super(message);
    }
}
