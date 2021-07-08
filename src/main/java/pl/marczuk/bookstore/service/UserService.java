package pl.marczuk.bookstore.service;

import pl.marczuk.bookstore.exception.UserExistException;
import pl.marczuk.bookstore.model.User;

public interface UserService {

    User addUser(User user) throws UserExistException;

    boolean checkIfUserExistByEmail(String email);

    boolean checkIfUserExistByUsername(String email);
    User getCurrentUser();

    void updateUser(User convertToEntity);
}
