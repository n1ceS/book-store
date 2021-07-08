package pl.marczuk.bookstore.service.implementation;

import org.springframework.stereotype.Service;
import pl.marczuk.bookstore.model.OrderLine;
import pl.marczuk.bookstore.repository.OrderLineRepository;
import pl.marczuk.bookstore.service.OrderLineService;

import java.util.List;

@Service
public class OrderLineServiceImpl implements OrderLineService {
    private final OrderLineRepository orderLineRepository;

    public OrderLineServiceImpl(OrderLineRepository orderLineRepository) {
        this.orderLineRepository = orderLineRepository;
    }

    @Override
    public List<OrderLine> findAllByOrderLineId(Long id) {
        return orderLineRepository.findAllByOrderLineId(id);
    }
}
