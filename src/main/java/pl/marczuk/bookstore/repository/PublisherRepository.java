package pl.marczuk.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.marczuk.bookstore.model.Publisher;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, String> {
}
