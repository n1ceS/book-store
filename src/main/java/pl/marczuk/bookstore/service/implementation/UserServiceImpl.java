package pl.marczuk.bookstore.service.implementation;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.marczuk.bookstore.exception.UserExistException;
import pl.marczuk.bookstore.model.Authority;
import pl.marczuk.bookstore.model.User;
import pl.marczuk.bookstore.repository.AuthorityRepository;
import pl.marczuk.bookstore.repository.UserRepository;
import pl.marczuk.bookstore.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User addUser(User user) throws UserExistException {
        if(checkIfUserExistByEmail(user.getEmail())) throw new UserExistException("Użytkownik z podanym adresem e-mail już istnieje!", "email");
        if(checkIfUserExistByUsername(user.getUsername())) throw new UserExistException("Użytkownik z taką nazwą już istnieje!", "username");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);
        Authority authority = new Authority("ROLE_USER", user.getUsername());
        authorityRepository.save(authority);
        return user;
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        if(!user.getUsername().equals(getCurrentUser().getUsername())) {
            throw new AccessDeniedException("Can't access to this resource with your permissions!");
        }
        User userToEdit = userRepository.findByUsername(user.getUsername());
        userToEdit.setFirstName(user.getFirstName());
        userToEdit.setLastName(user.getLastName());
        userToEdit.setCity(user.getCity());
        userToEdit.setEmail(user.getEmail());
        userToEdit.setPhone(user.getPhone());
        userToEdit.setStreet(user.getStreet());
        userToEdit.setZip(user.getZip());

    }

    @Override
    public boolean checkIfUserExistByEmail(String email) {
        return userRepository.findByEmail(email) !=null ? true : false;
    }

    @Override
    public boolean checkIfUserExistByUsername(String username) {
        return userRepository.findByUsername(username) !=null ? true : false;
    }

    public  User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if(principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        }else {
            username = principal.toString();
        }
        User user = userRepository.findByUsername(username);
        return user;
    }
}
