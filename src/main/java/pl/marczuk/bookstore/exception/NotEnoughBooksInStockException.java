package pl.marczuk.bookstore.exception;

import pl.marczuk.bookstore.model.Book;

public class NotEnoughBooksInStockException extends Exception{
    private static final String DEFAULT_MESSAGE = "Niewystarczająca ilość książek w sklepie!";

    public NotEnoughBooksInStockException() {
        super(DEFAULT_MESSAGE);
    }

    public NotEnoughBooksInStockException(Book book) {
        super(String.format("Niewystarczająca ilość %s ksiazek w sklepie. Pozostało tylko %s ", book.getTitle(), book.getQuantity()));
    }
}
