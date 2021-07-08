package pl.marczuk.bookstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pl.marczuk.bookstore.exception.NotEnoughBooksInStockException;
import pl.marczuk.bookstore.service.BookService;
import pl.marczuk.bookstore.service.ShoppingCartService;

@Controller
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    private final BookService bookService;

    public ShoppingCartController(ShoppingCartService shoppingCartService, BookService bookService) {
        this.shoppingCartService = shoppingCartService;
        this.bookService = bookService;
    }

    @GetMapping("/shoppingCart")
    public String shoppingCart(Model model) {
        model.addAttribute("itemsCount", shoppingCartService.getItemsCount());
        model.addAttribute("books", shoppingCartService.getBooksInCart());
        model.addAttribute("total", shoppingCartService.getTotalCost().toString());
        return "shoppingCart";
    }

    @GetMapping("/shoppingCart/add/{isbn}")
    public String addProductToCart(@PathVariable("isbn") String isbn) {
        bookService.findByIsbnOptional(isbn).ifPresent(shoppingCartService::addBook);
        return "redirect:/books";
    }

    @GetMapping("/shoppingCart/remove/{isbn}")
    public String removeProductFromCart(@PathVariable("isbn") String isbn) {
        bookService.findByIsbnOptional(isbn).ifPresent(shoppingCartService::removeBook);
        return "redirect:/shoppingCart";
    }

    @GetMapping("/shoppingCart/checkout")
    public String checkout(Model model) {
        try {
            shoppingCartService.checkout();
        } catch (NotEnoughBooksInStockException e) {
            model.addAttribute("outOfStockMessage", e.getMessage());
            model.addAttribute("itemsCount", shoppingCartService.getItemsCount());
            model.addAttribute("books", shoppingCartService.getBooksInCart());
            model.addAttribute("total", shoppingCartService.getTotalCost().toString());
            return "shoppingCart";
        }

        return "orderSuccess";
    }
}