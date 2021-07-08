package pl.marczuk.bookstore.controller;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.marczuk.bookstore.dto.UserDto;
import pl.marczuk.bookstore.model.User;
import pl.marczuk.bookstore.service.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping("/settings")
public class SettingsController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    public SettingsController(UserService userService) {
        this.userService = userService;
        modelMapper = new ModelMapper();
    }


    @GetMapping()
    public String settings(Model model) {
        User user = userService.getCurrentUser();
        model.addAttribute("user", modelMapper.map(user, UserDto.class));
        return "/settings";
    }

    @PostMapping()
    public String updateUser(@ModelAttribute("user") @Valid UserDto userDto, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            System.out.println(bindingResult.getAllErrors());
            model.addAttribute("user", userDto);
            return "/settings";
        }
        userService.updateUser(modelMapper.map(userDto, User.class));
        model.addAttribute("updateSuccess", Boolean.TRUE);
        return "/settings";
    }

}
