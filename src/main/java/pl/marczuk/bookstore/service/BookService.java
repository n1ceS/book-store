package pl.marczuk.bookstore.service;

import pl.marczuk.bookstore.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    List<Book> getAllBooks();
    Book add(Book book);
    void delete(String isbn);
    Book findByIsbn(String isbn);
    Book editBook(Book book);
    Long countAllBooks();

    Optional<Book> findByIsbnOptional(String isbn);
}
