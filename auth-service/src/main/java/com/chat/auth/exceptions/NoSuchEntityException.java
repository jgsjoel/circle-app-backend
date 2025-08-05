package com.chat.auth.exceptions;


public class NoSuchEntityException extends RuntimeException{

    public NoSuchEntityException(String message){
        super(message);
    }

}
