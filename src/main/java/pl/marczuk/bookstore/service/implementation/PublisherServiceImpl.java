package pl.marczuk.bookstore.service.implementation;

import org.springframework.stereotype.Service;
import pl.marczuk.bookstore.exception.ResourceNotFoundException;
import pl.marczuk.bookstore.model.Publisher;
import pl.marczuk.bookstore.repository.PublisherRepository;
import pl.marczuk.bookstore.service.PublisherService;

@Service
public class PublisherServiceImpl implements PublisherService {
        private final PublisherRepository publisherRepository;

    public PublisherServiceImpl(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    @Override
    public Publisher addPublisher(Publisher publisher) {
        return publisherRepository.save(publisher);
    }

    @Override
    public void deletePublisher(String name) {
        Publisher publisher = publisherRepository.findById(name).orElseThrow(() -> new ResourceNotFoundException("Publisher with name " + name + " not found!"));
    }
}
