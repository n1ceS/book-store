package pl.marczuk.bookstore.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.marczuk.bookstore.dto.OrderDto;
import pl.marczuk.bookstore.model.Order;
import pl.marczuk.bookstore.model.OrderLine;
import pl.marczuk.bookstore.service.BookService;
import pl.marczuk.bookstore.service.OrderLineService;
import pl.marczuk.bookstore.service.OrderService;
import pl.marczuk.bookstore.service.implementation.ShoppingCartServiceImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final ShoppingCartServiceImpl shoppingCartService;
    private final OrderLineService orderLineService;
    private final BookService bookService;

    public OrderController(OrderService orderService, ShoppingCartServiceImpl shoppingCartService, OrderLineService orderLineService, BookService bookService) {
        this.orderService = orderService;
        this.shoppingCartService = shoppingCartService;
        this.orderLineService = orderLineService;
        this.bookService = bookService;
    }

    @GetMapping
    public String orders(Model model){
        orderService.getUserOrders();
        model.addAttribute("itemsCount", shoppingCartService.getItemsCount());
        model.addAttribute("orders",orderService.getUserOrders());
        return "orders";
    }

    @GetMapping("/status/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String changeStatus(@PathVariable(name = "id") Long id, Model model){
        Order order = orderService.getOrderById(id);
        model.addAttribute("status", order.getStatus());
        model.addAttribute("id", id);
        return "admin/editOrderStatus";
    }

    @PostMapping("/status/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String changeStatus(@PathVariable(name = "id") Long id, @RequestParam(name = "status") String status, Model model){
        orderService.changeStatus(id, status);
        return "redirect:/orders";
    }
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String changeStatus(@PathVariable(name = "id") Long id){
        orderService.delete(id);
        return "redirect:/orders";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable(name = "id") Long id, Model model){
        List<OrderDto> orderDtoList = orderLineService.findAllByOrderLineId(id).stream().map(this::convertToDto).collect(Collectors.toList());
        BigDecimal totalCost = orderService.getOrderById(id).getTotalCost();
        model.addAttribute("orderLines", orderDtoList);
        model.addAttribute("totalCost", totalCost);
        model.addAttribute("itemsCount", shoppingCartService.getItemsCount());
        return "orderDetails";
    }

    private OrderDto convertToDto(OrderLine orderLine) {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderLineCost(orderLine.getOrderLineCost());
        orderDto.setBook(bookService.findByIsbn(orderLine.getOrderLineId().getBookIsbn()));
        orderDto.setQuantity(orderLine.getQuantity());
        return orderDto;
    }


}
