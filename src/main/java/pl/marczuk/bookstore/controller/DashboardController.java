package pl.marczuk.bookstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.marczuk.bookstore.service.BookService;
import pl.marczuk.bookstore.service.OrderService;
import pl.marczuk.bookstore.service.ShoppingCartService;

@Controller
public class DashboardController {
    private final BookService bookService;
    private final ShoppingCartService shoppingCartService;
    private final OrderService orderService;

    public DashboardController(BookService bookService, ShoppingCartService shoppingCartService, OrderService orderService) {
        this.bookService = bookService;
        this.shoppingCartService = shoppingCartService;
        this.orderService = orderService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("itemsCount", shoppingCartService.getItemsCount());
        model.addAttribute("bookCount", bookService.countAllBooks());
        model.addAttribute("moneySpent", orderService.countSpentMoney());
        model.addAttribute("ordersCount", orderService.countOrders());
        return "dashboard";
    }
}
