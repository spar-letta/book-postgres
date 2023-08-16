package com.javenock.bookpostgres.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"bookNumber"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bookNumber;
    private String name;
    private String author;
}
