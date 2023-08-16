package com.javenock.bookpostgres.repository;

import com.javenock.bookpostgres.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByBookNumber(String bookNumber);
    boolean existsByBookNumber(String bookNumber);
}
