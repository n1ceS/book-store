package pl.marczuk.bookstore.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "publishers")
public class Publisher {
    @Id
    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "publisher")
    List<Book> bookList;

    public Publisher(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
