package pl.marczuk.bookstore.service;

import pl.marczuk.bookstore.model.Order;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {
   List<Order> getUserOrders();

   Order getOrderById(Long id);

   void changeStatus(Long id, String status);

   BigDecimal countSpentMoney();

   void delete(Long id);

   Integer countOrders();
}
