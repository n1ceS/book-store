package pl.marczuk.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.marczuk.bookstore.model.Order;
import pl.marczuk.bookstore.model.User;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUser(User currentUser);
    @Query("select sum(o.totalCost) from orders o where o.user=:user")
    BigDecimal sumTotalCostByUser(User user);

    Integer countByUser(User currentUser);
    @Query(value = "SELECT  username FROM orders group by username  ORDER BY (Select count(username) from orders) DESC LIMIT 10", nativeQuery = true)
    List<String> findTop10UserByCountUser();
}
