package pl.marczuk.bookstore.dto;

import lombok.Data;
import pl.marczuk.bookstore.model.Author;
import pl.marczuk.bookstore.model.Category;
import pl.marczuk.bookstore.model.Publisher;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class BookDto {

    @NotEmpty(message = "Pole ISBN nie może być puste!")
    private String isbn;

    @NotEmpty(message = "Pole Tytuł nie może być puste!")
    private String title;

    @NotNull(message = "Pole Rok nie może być puste!")
    @Min(value = 1, message = "Wartość musi być większa niż 1")
    @Max(value = 2022, message = "Wartość musi być mniejsza niż 2022")
    private Integer publication_year;

    @NotNull(message = "Pole Cena nie może być puste!")
    @Min(value = 1, message = "Wartość musi być większa niż 0")
    @Max(value = 999999, message = "")
    private BigDecimal price;

    @NotNull(message = "Pole Ilość nie może być puste!")
    @Min(value = 0, message = "Wartość musi być większa od 0")
    private Long quantity;

    @NotNull(message = "Pole Autor nie może być puste!")
    private Author author;

    @NotEmpty(message = "Pole Wydawnictwo nie może być puste!")
    private String publisher;

    @NotNull(message = "Pole Kategoria nie może być puste!")
    private Category category;

    private String photo;
}
