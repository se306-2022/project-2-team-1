package com.team01.scheduler.graph.exceptions;

public class InvalidPositionException extends RuntimeException{

    public InvalidPositionException(){}

    public InvalidPositionException(String msg) {
        super(msg);
    }

}
