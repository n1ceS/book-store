package pl.marczuk.bookstore.service.implementation;

import org.springframework.stereotype.Service;
import pl.marczuk.bookstore.dto.topDto;
import pl.marczuk.bookstore.model.Book;
import pl.marczuk.bookstore.model.OrderLine;
import pl.marczuk.bookstore.repository.BookRepository;
import pl.marczuk.bookstore.repository.OrderLineRepository;
import pl.marczuk.bookstore.service.OrderLineService;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderLineServiceImpl implements OrderLineService {
    private final OrderLineRepository orderLineRepository;
    private final BookRepository bookRepository;

    public OrderLineServiceImpl(OrderLineRepository orderLineRepository, BookRepository bookRepository) {
        this.orderLineRepository = orderLineRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public List<OrderLine> findAllByOrderLineId(Long id) {
        return orderLineRepository.findAllByOrderLineId(id);
    }

    @Override
    public List<topDto> getTop10Books() {
        List<topDto> topDtos = new ArrayList<>();
        List<String> top10 = orderLineRepository.findTop10BookByCountBook();
        int i = 1;
        for(String bookisbn : top10) {
           Book book = bookRepository.getById(bookisbn);
            Integer value = orderLineRepository.sumQuantityByBookIsbn(bookisbn);
            topDtos.add(new topDto(i, book.getTitle(), value));
            i++;
        }
        return topDtos;
    }
}
