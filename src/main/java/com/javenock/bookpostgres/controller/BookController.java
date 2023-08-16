package com.javenock.bookpostgres.controller;

import com.javenock.bookpostgres.exceptions.BookListIsEmptyException;
import com.javenock.bookpostgres.exceptions.BookWithIdNotFoundException;
import com.javenock.bookpostgres.exceptions.ConstraintViolationExceptions;
import com.javenock.bookpostgres.model.Book;
import com.javenock.bookpostgres.request.BookRequest;
import com.javenock.bookpostgres.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/book-postgres")
@Api(tags = { "This section contains all Book Specific Operations" })
public class BookController {

    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "create new book", notes = "create new book")
    public Book createBook(@RequestBody @Valid BookRequest bookRequest) throws ConstraintViolationExceptions {
        return bookService.createNewBook(bookRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "fetch all books", notes = "fetch all books")
    public List<Book> listAllBooks() throws BookListIsEmptyException {
        return bookService.listAllBooks();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Fetch book by id", notes = "fetch book by id")
    public Book getBookById(@PathVariable Long id) throws BookWithIdNotFoundException {
        return bookService.getBookById(id);
    }

    @GetMapping("/book-number/{bookNumber}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get book by its book number", notes = "Get book by its book number")
    public Book findByBookNumber(@PathVariable String bookNumber) throws BookWithIdNotFoundException {
        return bookService.getByBookNumber(bookNumber);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update book by id", notes = "Update book by id")
    public String updateBook(@RequestBody @Valid BookRequest bookRequest, @PathVariable Long id){
        return bookService.updateBookById(bookRequest,id);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete book by id", notes = "Delete book by id")
    public void deleteBookById(@PathVariable Long id){
        bookService.deleteBookById(id);
    }
}
