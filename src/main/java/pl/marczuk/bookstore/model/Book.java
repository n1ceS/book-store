package pl.marczuk.bookstore.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;


@RequiredArgsConstructor
@Getter
@Setter
@Entity(name = "books")
public class Book {
    @Id
    private String isbn;

    @Column(name = "title")
    private String title;

    @Column(name = "publication_year")
    private Integer publication_year;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "photo")
    private String photo;

    @ManyToOne(fetch = FetchType.EAGER)
    private Author author;

    @ManyToOne(fetch = FetchType.EAGER)
    private Publisher publisher;

    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;



}
