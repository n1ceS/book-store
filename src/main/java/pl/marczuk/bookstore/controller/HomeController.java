package pl.marczuk.bookstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import pl.marczuk.bookstore.utils.AuthenticationChecker;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        if(AuthenticationChecker.isAuthenticated()){
            return "redirect:/dashboard";
        }
        return "login";
    }

}
