package com.javenock.bookpostgres.adviceController;

import com.javenock.bookpostgres.exceptions.BookListIsEmptyException;
import com.javenock.bookpostgres.exceptions.BookWithIdNotFoundException;
import com.javenock.bookpostgres.exceptions.ConstraintViolationExceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class AdviceControllerException {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ ConstraintViolationExceptions.class })
    public Map<String, String> handleConstraintViolation(ConstraintViolationExceptions ex) {
        Map<String, String> mapError = new HashMap<>();
        mapError.put("System Message", ex.getMessage());
        return mapError;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ BookWithIdNotFoundException.class })
    public Map<String, String> handleBookWithIdNotFoundException(BookWithIdNotFoundException ex) {
        Map<String, String> mapError = new HashMap<>();
        mapError.put("System Message", ex.getMessage());
        return mapError;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ BookListIsEmptyException.class })
    public Map<String, String> handleBookListIsEmptyException(BookListIsEmptyException ex) {
        Map<String, String> mapError = new HashMap<>();
        mapError.put("System Message", ex.getMessage());
        return mapError;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        Map<String, String> mapError = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach((error) -> {
            mapError.put(error.getField(), error.getDefaultMessage());
        });
        return mapError;
    }
}
