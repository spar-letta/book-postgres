package com.javenock.bookpostgres.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class BookRequest {
    @NotBlank
    private String bookNumber;
    @NotBlank
    private String name;
    @NotBlank
    private String author;
}
