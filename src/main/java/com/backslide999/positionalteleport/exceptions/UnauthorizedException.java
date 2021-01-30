package com.backslide999.positionalteleport.exceptions;

public class UnauthorizedException extends Exception{

    private String authorization;
    public UnauthorizedException(String message){
        super(message);
    }

}
