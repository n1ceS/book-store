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

    @Query(value = "SELECT book_isbn FROM order_lines  group by book_isbn  ORDER BY (Select count(book_isbn) from order_lines) DESC LIMIT 10", nativeQuery = true)
    List<String> findTop10BookByCountBook();

    @Query(value = "SELECT sum(quantity) from order_lines where book_isbn=:bookisbn", nativeQuery = true)
    Integer sumQuantityByBookIsbn(String bookisbn);
}
