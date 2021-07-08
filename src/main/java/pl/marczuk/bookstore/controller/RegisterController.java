package pl.marczuk.bookstore.controller;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.marczuk.bookstore.dto.UserDto;
import pl.marczuk.bookstore.exception.UserExistException;
import pl.marczuk.bookstore.model.User;
import pl.marczuk.bookstore.service.UserService;
import pl.marczuk.bookstore.utils.AuthenticationChecker;

import javax.validation.Valid;

@Controller
public class RegisterController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    public RegisterController(UserService userService) {
        this.userService = userService;
        modelMapper = new ModelMapper();
    }

    @GetMapping("/register")
    private String register(Model model) {
        if (AuthenticationChecker.isAuthenticated()) return "redirect:/dashboard";
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "register";
    }

    @PostMapping("/register")
    public String registerUserAccount(
            @ModelAttribute("user") @Valid UserDto userDto,
            BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userDto);
            return "register";
        }
        try {
            User registered = userService.addUser(modelMapper.map(userDto, User.class));
        } catch (UserExistException uaeEx) {
            bindingResult.rejectValue(uaeEx.getField(), "error.username", uaeEx.getMessage());
            model.addAttribute("user", userDto);
            return "register";
        }
        redirectAttributes.addFlashAttribute("successRegistration", Boolean.TRUE);
        return "redirect:/login";
    }
}
