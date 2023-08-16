package com.javenock.bookpostgres.exceptions;

public class BookWithIdNotFoundException extends Exception{
    public BookWithIdNotFoundException(String message) {
        super(message);
    }
}
