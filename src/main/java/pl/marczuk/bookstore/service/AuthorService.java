package pl.marczuk.bookstore.service;

import pl.marczuk.bookstore.model.Author;
import pl.marczuk.bookstore.model.Book;

import java.util.List;

public interface AuthorService {
    List<Author> getAllAuthors();
    Author addAuthor(Author author);
    void delete(Long id);
    Author editAuthor(Author author);

    Author findById(Long id);
}
