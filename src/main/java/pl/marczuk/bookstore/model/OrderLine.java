package pl.marczuk.bookstore.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity(name = "order_lines")
public class OrderLine {
    @Id
    private OrderLineId orderLineId;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "book_cost")
    private BigDecimal bookCost;

    @Column(name = "order_line_cost")
    private BigDecimal orderLineCost;

}
