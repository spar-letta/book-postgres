package com.javenock.bookpostgres.exceptions;

public class ConstraintViolationExceptions extends Exception{
    public ConstraintViolationExceptions(String message) {
        super(message);
    }
}
