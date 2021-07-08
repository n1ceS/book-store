package pl.marczuk.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.marczuk.bookstore.model.OrderLine;
import pl.marczuk.bookstore.model.OrderLineId;

import java.util.List;

@Repository
public interface OrderLineRepository extends JpaRepository<OrderLine, OrderLineId> {

    @Query("select o from order_lines o where o.orderLineId.orderId =:id")
    List<OrderLine> findAllByOrderLineId(@Param("id") Long id);

    @Query("select o from order_lines o where o.orderLineId.bookIsbn =:isbn")
    List<OrderLine> findAllByBookIsbn(String isbn);
}
