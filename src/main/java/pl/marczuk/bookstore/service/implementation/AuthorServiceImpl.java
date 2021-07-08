package pl.marczuk.bookstore.service.implementation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.marczuk.bookstore.exception.ResourceNotFoundException;
import pl.marczuk.bookstore.model.Author;
import pl.marczuk.bookstore.model.Book;
import pl.marczuk.bookstore.model.OrderLineId;
import pl.marczuk.bookstore.repository.AuthorRepository;
import pl.marczuk.bookstore.repository.BookRepository;
import pl.marczuk.bookstore.repository.OrderLineRepository;
import pl.marczuk.bookstore.repository.OrderRepository;
import pl.marczuk.bookstore.service.AuthorService;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final OrderLineRepository orderLineRepository;
    private final OrderRepository orderRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository, OrderLineRepository orderLineRepository, OrderRepository orderRepository) {
        this.authorRepository = authorRepository;
        this.orderLineRepository = orderLineRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    @Transactional
    public Author addAuthor(Author author) {
        return authorRepository.save(author);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Author with id " + id + " not found!"));
        authorRepository.delete(author);
    }
    @Override
    @Transactional
    public Author editAuthor(Author author) {
        Author authorToEdit = authorRepository.findById(author.getId()).orElseThrow(() -> new ResourceNotFoundException("Author with id " + author.getId() + " not found!"));
        authorToEdit.setLastName(author.getLastName());
        authorToEdit.setFirstName(author.getFirstName());
        return authorToEdit;
    }

    @Override
    @Transactional
    public Author findById(Long id) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Author with id " + id + " not found!"));
        return author;
    }
}
