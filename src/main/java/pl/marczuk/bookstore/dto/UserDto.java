package pl.marczuk.bookstore.dto;

import lombok.Data;
import pl.marczuk.bookstore.annotation.PasswordMatches;
import pl.marczuk.bookstore.annotation.ValidEmail;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@PasswordMatches
public class UserDto {

    @NotEmpty(message = "Pole imię nie może być puste!")
    private String firstName;

    @NotEmpty(message = "Pole Nazwisko nie może być puste!")
    private String lastName;

    @NotEmpty(message = "Pole nazwa użytkownika nie może być puste!")
    private String username;

    @NotEmpty(message = "Pole hasło nie może być puste!")
    private String password;

    @NotEmpty(message = "Pole potwierdź hasło nie może być puste!")
    private String matchingPassword;

    @ValidEmail
    @NotEmpty(message = "Pole email nie może być puste!")
    private String email;

    @NotEmpty(message = "Pole ulica nie może być puste!")
    private String street;

    @NotEmpty(message = "Pole miasto nie może być puste!")
    private String city;

    @NotEmpty(message = "Pole telefon nie może być puste!")
    private String phone;

    @NotEmpty(message = "Pole kod pocztowy nie może być puste!")
    private String zip;
}
