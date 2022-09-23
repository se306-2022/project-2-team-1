package com.digraph.weighted.exceptions;

public class InvalidPositionException extends RuntimeException{

    public InvalidPositionException(){}

    public InvalidPositionException(String msg) {
        super(msg);
    }

}
