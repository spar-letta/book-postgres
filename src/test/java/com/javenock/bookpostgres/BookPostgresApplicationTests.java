package com.javenock.bookpostgres;

import com.javenock.bookpostgres.exceptions.BookListIsEmptyException;
import com.javenock.bookpostgres.exceptions.BookWithIdNotFoundException;
import com.javenock.bookpostgres.exceptions.ConstraintViolationExceptions;
import com.javenock.bookpostgres.model.Book;
import com.javenock.bookpostgres.request.BookRequest;
import com.javenock.bookpostgres.service.BookService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookPostgresApplicationTests {

	@LocalServerPort
	private int port;

	private String baseUrl="http://localhost";

	private static RestTemplate restTemplate;

	@Autowired
	private TestH2Repository testH2Repository;

	@Autowired
	BookService bookService;

	@BeforeAll
	public static void init(){
		restTemplate = new RestTemplate();
	}

	@BeforeEach
	public void setUp(){
		baseUrl = baseUrl.concat(":").concat(port+"").concat("/book-postgres");
	}

	@Test
	@Sql(statements = "DELETE FROM BOOK WHERE book_number = 'bk123'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void testCreateBook(){
		BookRequest bookRequest = BookRequest.builder()
				.bookNumber("bk123")
				.name("Golden House")
				.author("Brwon Seiger")
				.build();
		ResponseEntity<Book> bookResponseEntity = restTemplate.postForEntity(baseUrl, bookRequest, Book.class);
		assertAll(
				() -> assertEquals("bk123", bookResponseEntity.getBody().getBookNumber()),
				() -> assertEquals(1,testH2Repository.findAll().size())
		);
	}

	@Test
	@Sql(statements = "INSERT INTO BOOK (id, book_number, name, author) VALUES (1L, 'bk123', 'Golden House', 'Brwon Seiger')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "DELETE FROM BOOK WHERE book_number = 'bk123'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void testThrowBookNumberAlreadyExistsException(){
		BookRequest bookRequest = BookRequest.builder()
				.bookNumber("bk123")
				.name("Golden shoe")
				.author("The player")
				.build();
		assertThrows(ConstraintViolationExceptions.class, () -> bookService.createNewBook(bookRequest));
	}

	@Test
	@Sql(statements = "INSERT INTO BOOK (id, book_number, name, author) VALUES (1L, 'bk123', 'Golden House', 'Brwon Seiger'), (2L, 'bk1234', 'The Lier', 'Brwon Seiger')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "DELETE FROM BOOK WHERE id = '1'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Sql(statements = "DELETE FROM BOOK WHERE id = '2'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void testFetchAllBooks(){
		ResponseEntity<List>  bkList = restTemplate.getForEntity(baseUrl, List.class);
		assertAll(
				() -> assertNotNull(bkList),
				() -> assertEquals(2, bkList.getBody().size()),
				() -> assertEquals(2, testH2Repository.findAll().size())
		);
	}

	@Test
	public void testThrowBookListIsEmptyException(){
		assertThrows(BookListIsEmptyException.class, () -> bookService.listAllBooks());
		assertThrows(BookWithIdNotFoundException.class, () -> bookService.getBookById(10L));
		assertThrows(BookWithIdNotFoundException.class, () -> bookService.getByBookNumber("bk12345"));
	}

	@Test
	@Sql(statements = "INSERT INTO BOOK (id, book_number, name, author) VALUES (1L, 'bk123', 'Golden House', 'Brwon Seiger')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "DELETE FROM BOOK WHERE id = '1'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void testFetchBookById(){
		ResponseEntity<Book> book= restTemplate.getForEntity(baseUrl + "/{id}", Book.class, 1L);
		assertAll(
				() -> assertEquals("Golden House", book.getBody().getName()),
				() -> assertEquals("Brwon Seiger", testH2Repository.findById(1L).get().getAuthor())
		);
	}

	@Test
	@Sql(statements = "INSERT INTO BOOK (id, book_number, name, author) VALUES (1L, 'bk123', 'Golden House', 'Brwon Seiger')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "DELETE FROM BOOK WHERE id = '1'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void testFindByBookNumber(){
		ResponseEntity<Book> book= restTemplate.getForEntity(baseUrl + "/book-number/{bookNumber}", Book.class, "bk123");
		assertAll(
				() -> assertEquals("Golden House", book.getBody().getName()),
				() -> assertEquals("Brwon Seiger", testH2Repository.findByBookNumber("bk123").get().getAuthor())
		);
	}

	@Test
	@Sql(statements = "INSERT INTO BOOK (id, book_number, name, author) VALUES (1L, 'bk123', 'Golden House', 'Brwon Seiger')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "DELETE FROM BOOK WHERE id = '1'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void testUpdateBook(){
		BookRequest bookRequest = BookRequest.builder()
				.bookNumber("bk123")
				.name("Golden shoe")
				.author("The player")
				.build();
		restTemplate.put(baseUrl+"/{id}", bookRequest, 1L);
		Book getBook = testH2Repository.findById(1L).get();
		assertAll(
				() -> assertEquals("Golden shoe", getBook.getName()),
				() -> assertEquals("The player", getBook.getAuthor())
		);
	}

	@Test
	@Sql(statements = "INSERT INTO BOOK (id, book_number, name, author) VALUES (1L, 'bk123', 'Golden House', 'Brwon Seiger')", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	public void testDeleteBookById(){
		int list_size = testH2Repository.findAll().size();
		assertEquals(1, list_size);
		restTemplate.delete(baseUrl+"/delete/{id}", 1L);
		assertEquals(0, testH2Repository.findAll().size());
	}

}
