package pl.marczuk.bookstore.service;

import pl.marczuk.bookstore.exception.NotEnoughBooksInStockException;
import pl.marczuk.bookstore.model.Book;

import java.math.BigDecimal;
import java.util.Map;

public interface ShoppingCartService {

    void addBook(Book book);

    void removeBook(Book book);

    Map<Book, Long> getBooksInCart();

    void checkout() throws NotEnoughBooksInStockException;

    BigDecimal getTotalCost();

    int getItemsCount();
}
