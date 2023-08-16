package com.javenock.bookpostgres;

import com.javenock.bookpostgres.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TestH2Repository extends JpaRepository<Book, Long> {

    Optional<Book> findByBookNumber(String bookNumber);
}
