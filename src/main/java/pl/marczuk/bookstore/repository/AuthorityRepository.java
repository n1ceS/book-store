package pl.marczuk.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.marczuk.bookstore.model.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, String> {

}
