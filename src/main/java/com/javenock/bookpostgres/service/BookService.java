package com.javenock.bookpostgres.service;

import com.javenock.bookpostgres.exceptions.BookListIsEmptyException;
import com.javenock.bookpostgres.exceptions.BookWithIdNotFoundException;
import com.javenock.bookpostgres.exceptions.ConstraintViolationExceptions;
import com.javenock.bookpostgres.model.Book;
import com.javenock.bookpostgres.repository.BookRepository;
import com.javenock.bookpostgres.request.BookRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public Book createNewBook(BookRequest bookRequest) throws ConstraintViolationExceptions {
    if(bookRepository.existsByBookNumber(bookRequest.getBookNumber())){
        throw new ConstraintViolationExceptions("Book Number already exists");
    }
    Book book = Book.builder()
            .bookNumber(bookRequest.getBookNumber())
            .name(bookRequest.getName())
            .author(bookRequest.getAuthor())
            .build();
        return bookRepository.save(book);
    }

    public List<Book> listAllBooks() throws BookListIsEmptyException {
        List<Book> books = bookRepository.findAll();
        if(books.size() > 0){
            return bookRepository.findAll();
        }else {
            throw new BookListIsEmptyException("No books created, create one");
        }

    }

    public Book getBookById(Long id) throws BookWithIdNotFoundException {
        return bookRepository.findById(id).orElseThrow(() -> new BookWithIdNotFoundException("No such book with id : "+id));
    }

    public Book getByBookNumber(String bookNumber) throws BookWithIdNotFoundException {
        return bookRepository.findByBookNumber(bookNumber).orElseThrow(() -> new BookWithIdNotFoundException("No such book with book number : "+bookNumber));
    }

    public String updateBookById(BookRequest bookRequest, Long id) {
        Book prevBook = bookRepository.findById(id).get();
        prevBook.setBookNumber(bookRequest.getBookNumber());
        prevBook.setName(bookRequest.getName());
        prevBook.setAuthor(bookRequest.getAuthor());
        bookRepository.save(prevBook);
        return "updates successfully";
    }

    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }
}
