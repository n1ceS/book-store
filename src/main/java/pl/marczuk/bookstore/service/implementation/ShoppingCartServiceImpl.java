package pl.marczuk.bookstore.service.implementation;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pl.marczuk.bookstore.dto.OrderDto;
import pl.marczuk.bookstore.exception.NotEnoughBooksInStockException;
import pl.marczuk.bookstore.exception.ResourceNotFoundException;
import pl.marczuk.bookstore.model.*;
import pl.marczuk.bookstore.repository.BookRepository;
import pl.marczuk.bookstore.repository.OrderLineRepository;
import pl.marczuk.bookstore.repository.OrderRepository;
import pl.marczuk.bookstore.repository.UserRepository;
import pl.marczuk.bookstore.service.EmailService;
import pl.marczuk.bookstore.service.ShoppingCartService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService{
    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;
    private final OrderLineRepository orderLineRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final TemplateEngine templateEngine;
    private Map<Book, Long> books = new HashMap<>();

    public ShoppingCartServiceImpl(BookRepository bookRepository, OrderRepository orderRepository, OrderLineRepository orderLineRepository, UserRepository userRepository, EmailService emailService, TemplateEngine templateEngine) {
        this.bookRepository = bookRepository;
        this.orderRepository = orderRepository;
        this.orderLineRepository = orderLineRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.templateEngine = templateEngine;
    }

    @Override
    public void addBook(Book bookFromCart) {
        Book book = existsInCart(bookFromCart.getIsbn());
        if(book != null) {
            books.replace(book, books.get(book) + 1);
        } else {
            books.put(bookFromCart, 1L);
        }
    }

    @Override
    public void removeBook(Book bookFromCart) {
        //containsKey nie działało
        for(Book book : books.keySet()) {
            if(book.getIsbn().equals(bookFromCart.getIsbn()))
            {
                if (books.get(book) > 1)
                    books.replace(book, books.get(book) - 1);
                else if (books.get(book) == 1) {
                    books.remove(book);
                }
            }
        }
    }

    @Override
    public Map<Book, Long> getBooksInCart() {
        return Collections.unmodifiableMap(books);
    }

    @Override
    public void checkout() throws NotEnoughBooksInStockException {
        Book book;
        Order order = new Order();
        order.setDate(getCurrentDate());
        order.setUser(getCurrentUser());
        order = orderRepository.save(order);
        order.setStatus("PRZYJĘTE");
        BigDecimal totalCost = BigDecimal.ZERO;
        List<OrderDto> orderDtoList = new ArrayList<>();
        for (Map.Entry<Book, Long> entry : books.entrySet()) {
            book = bookRepository.findById(entry.getKey().getIsbn()).orElseThrow(() -> new ResourceNotFoundException("Book with isbn " + entry.getKey().getIsbn() + " not found!"));
            if (book.getQuantity() < entry.getValue())
                throw new NotEnoughBooksInStockException(book);
            entry.getKey().setQuantity(book.getQuantity() - entry.getValue());
            OrderLine orderLine = getOrderLine(book, order, entry);
            orderLineRepository.save(orderLine);

            OrderDto orderDto = getOrderDto(book, entry, orderLine);
            orderDtoList.add(orderDto);

            //bazujemy na obiektach immutable
            totalCost = totalCost.add(orderLine.getOrderLineCost());
        }
        order.setTotalCost(totalCost);
        sendEmail(orderDtoList, totalCost, getCurrentUser());

        bookRepository.saveAll(books.keySet());
        bookRepository.flush();
        books.clear();
    }

    private OrderDto getOrderDto(Book book, Map.Entry<Book, Long> entry, OrderLine orderLine) {
        //OrderDto
        OrderDto orderDto = new OrderDto();
        orderDto.setBook(book);
        orderDto.setOrderLineCost(orderLine.getOrderLineCost());
        orderDto.setQuantity(entry.getValue());
        return orderDto;
    }

    private OrderLine getOrderLine(Book book, Order order, Map.Entry<Book, Long> entry) {
        OrderLineId orderLineId = new OrderLineId();
        orderLineId.setBookIsbn(book.getIsbn());
        orderLineId.setOrderId(order.getId());

        OrderLine orderLine = new OrderLine();
        orderLine.setOrderLineId(orderLineId);
        orderLine.setBookCost(book.getPrice());
        orderLine.setQuantity(entry.getValue());
        orderLine.setOrderLineCost(book.getPrice().multiply(BigDecimal.valueOf(entry.getValue())));
        return orderLine;
    }

    @Override
    public BigDecimal getTotalCost() {
        return books.entrySet().stream()
                .map(entry -> entry.getKey().getPrice().multiply(BigDecimal.valueOf(entry.getValue())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public int getItemsCount(){
        return books.size();
    }
    private LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    private Book existsInCart(String isbn){
        for(Book book : books.keySet()) {
            if(book.getIsbn().equals(isbn))
            {
                return book;
            }
        }
        return null;
    }

    private void sendEmail(List<OrderDto> orderDtoList, BigDecimal totalCost, User user) {
        Context context = new Context();
        context.setVariable("orderLines", orderDtoList);
        context.setVariable("totalCost", totalCost);
        context.setVariable("firstName", user.getFirstName());
        context.setVariable("lastName", user.getLastName());
        String body = templateEngine.process("emailTemplate", context);
        emailService.sendEmail(user.getEmail(), "Potwierdzenie zakupu - Book Store", body);
    }

    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if(principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        }else {
            username = principal.toString();
        }
        User user = userRepository.findByUsername(username);
        return user;
    }
}
