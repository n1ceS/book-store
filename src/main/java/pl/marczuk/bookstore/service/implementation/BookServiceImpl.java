package pl.marczuk.bookstore.service.implementation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.marczuk.bookstore.exception.ResourceNotFoundException;
import pl.marczuk.bookstore.model.Book;
import pl.marczuk.bookstore.repository.BookRepository;
import pl.marczuk.bookstore.service.BookService;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    @Transactional
    public Book add(Book book) {
        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public void delete(String isbn) {
        Book book = bookRepository.findById(isbn).orElseThrow(() -> new ResourceNotFoundException("Book with isbn " + isbn + " not found!"));
        bookRepository.delete(book);
    }

    @Override
    @Transactional
    public Book findByIsbn(String isbn) {
        Book book = bookRepository.findById(isbn).orElseThrow(() -> new ResourceNotFoundException("Book with isbn " + isbn + " not found!"));
        return book;
    }

    @Override
    @Transactional
    public Book editBook(Book book) {
        Book bookToEdit = bookRepository.findById(book.getIsbn()).orElseThrow(() -> new ResourceNotFoundException("Book with isbn " + book.getIsbn() + " not found!"));

        bookToEdit.setAuthor(book.getAuthor());
        bookToEdit.setCategory(book.getCategory());
        bookToEdit.setPrice(book.getPrice());
        bookToEdit.setTitle(book.getTitle());
        bookToEdit.setPublication_year(book.getPublication_year());
        bookToEdit.setAuthor(book.getAuthor());
        bookToEdit.setPublisher(book.getPublisher());
        bookToEdit.setQuantity(book.getQuantity());
        bookToEdit.setPhoto(book.getPhoto());
        return bookToEdit;
    }

    public Long countAllBooks() {
        return bookRepository.count();
    }

    @Override
    public Optional<Book> findByIsbnOptional(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }
}
