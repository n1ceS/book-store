package pl.marczuk.bookstore.service.implementation;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.marczuk.bookstore.dto.topDto;
import pl.marczuk.bookstore.exception.ResourceNotFoundException;
import pl.marczuk.bookstore.model.Order;
import pl.marczuk.bookstore.model.OrderLine;
import pl.marczuk.bookstore.model.User;
import pl.marczuk.bookstore.repository.OrderLineRepository;
import pl.marczuk.bookstore.repository.OrderRepository;
import pl.marczuk.bookstore.repository.UserRepository;
import pl.marczuk.bookstore.service.OrderService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;


@Service
public class OrderServiceImpl implements OrderService {
    private  final OrderRepository orderRepository;
    private  final OrderLineRepository orderLineRepository;
    private  final UserRepository userRepository;
    public OrderServiceImpl(OrderRepository orderRepository, OrderLineRepository orderLineRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderLineRepository = orderLineRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public List<Order> getUserOrders() {
        if(isAdmin()) return orderRepository.findAll();
        return orderRepository.findAllByUser(getCurrentUser());
    }

    @Override
    @Transactional
    public Order getOrderById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource with id " + id  + " not found."));
        User user = getCurrentUser();
        if(!order.getUser().getUsername().equals(user.getUsername()) && !isAdmin()) {
            throw new AccessDeniedException("Can't access to this resource with your permissions!");
        }
        return order;
    }

    @Override
    @Transactional
    public void changeStatus(Long id, String status) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource with id " + id  + " not found."));
        User user = getCurrentUser();
        if(!order.getUser().getUsername().equals(user.getUsername()) && !isAdmin()) {
            throw new AccessDeniedException("Can't access to this resource with your permissions!");
        }
        order.setStatus(status);
    }

    @Override
    public BigDecimal countSpentMoney() {
        BigDecimal result = orderRepository.sumTotalCostByUser(getCurrentUser()).setScale(2, RoundingMode.HALF_UP);
        return result;
    }

    @Override
    public void delete(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource with id " + id  + " not found."));
        User user = getCurrentUser();
        if(!order.getUser().getUsername().equals(user.getUsername()) && !isAdmin()) {
            throw new AccessDeniedException("Can't access to this resource with your permissions!");
        }
        List<OrderLine> orderLines = orderLineRepository.findAllByOrderLineId(id);
        orderLineRepository.deleteAll(orderLines);
        orderRepository.delete(order);
    }

    @Override
    public Integer countOrders() {
        return orderRepository.countByUser(getCurrentUser());
    }

    @Override
    public List<topDto> getTop10Buyers() {
        List<topDto> topDtos = new ArrayList<>();
        List<String> top10 = orderRepository.findTop10UserByCountUser();
        int i = 1;
        for(String userName : top10) {
            User user = userRepository.findByUsername(userName);
            Integer value = orderRepository.countByUser(user);
            topDtos.add(new topDto(i, user.getFirstName() + " " + user.getLastName(), value));
            i++;
        }
        return topDtos;
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

    private boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth !=null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) return true;
        return false;
    }
}
