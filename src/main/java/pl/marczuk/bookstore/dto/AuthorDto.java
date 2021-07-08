package pl.marczuk.bookstore.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class AuthorDto {

    private Long id;
    @NotEmpty(message = "Pole Imię nie może być puste!")
    private String firstName;

    @NotEmpty(message = "Pole Nazwisko nie może być puste!")
    private String lastName;

}
