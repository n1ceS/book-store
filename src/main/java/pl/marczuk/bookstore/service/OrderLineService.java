package pl.marczuk.bookstore.service;

import org.springframework.data.repository.query.Param;
import pl.marczuk.bookstore.model.OrderLine;

import java.util.List;

public interface OrderLineService {
    List<OrderLine> findAllByOrderLineId(Long id);
}
