package com.javenock.bookpostgres.exceptions;

public class BookListIsEmptyException extends Exception{
    public BookListIsEmptyException(String message) {
        super(message);
    }
}
