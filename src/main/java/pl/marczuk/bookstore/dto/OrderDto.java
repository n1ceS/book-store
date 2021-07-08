package pl.marczuk.bookstore.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.marczuk.bookstore.model.Book;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class OrderDto {
    private Book book;

    private Long quantity;

    private BigDecimal orderLineCost;


}
