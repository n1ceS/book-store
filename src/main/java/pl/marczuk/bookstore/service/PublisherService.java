package pl.marczuk.bookstore.service;

import pl.marczuk.bookstore.model.Publisher;

public interface PublisherService {
    Publisher addPublisher(Publisher publisher);
    void deletePublisher(String name);
}
