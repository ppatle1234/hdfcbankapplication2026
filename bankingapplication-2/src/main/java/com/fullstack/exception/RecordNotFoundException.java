package com.fullstack.exception;

public class RecordNotFoundException extends RuntimeException {

    public RecordNotFoundException(String msg){
        super(msg);
    }
}
