package pl.marczuk.bookstore.controller;

import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.marczuk.bookstore.dto.AuthorDto;
import pl.marczuk.bookstore.model.Author;
import pl.marczuk.bookstore.service.AuthorService;


import javax.validation.Valid;
import java.io.IOException;


@Controller
@RequestMapping("/authors")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AuthorController {
    private final AuthorService authorService;
    private final ModelMapper modelMapper;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
        modelMapper = new ModelMapper();
    }

    @GetMapping()
    public String authors(Model model) {
        model.addAttribute("authors", authorService.getAllAuthors());
        return "/admin/author/manageAuthors";
    }
    @GetMapping("/add")
    public String addAuthor(Model model) {
        AuthorDto authorDto = new AuthorDto();
        model.addAttribute("author", authorDto);
        return "admin/author/addAuthor";
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addAuthor(@ModelAttribute("author") @Valid AuthorDto authorDto, BindingResult bindingResult, Model model) throws IOException {
        if(bindingResult.hasErrors())
        {
            model.addAttribute("author", authorDto);
            return "admin/author/addAuthor";
        }
        Author author = modelMapper.map(authorDto, Author.class);
        authorService.addAuthor(author);

        return "redirect:/authors";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editAuthor(@PathVariable(name = "id") Long id, Model model) {
        Author author = authorService.findById(id);
        model.addAttribute("author", author);
        return "admin/author/editAuthor";
    }

    @PostMapping("/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    private String editAuthor(@ModelAttribute("author") AuthorDto authorDto, BindingResult bindingResult, Model model) throws IOException {
        if(bindingResult.hasErrors())
        {
            model.addAttribute("author", authorDto);
            return "admin/author/editAuthor";
        }
        Author author = modelMapper.map(authorDto, Author.class);
        authorService.editAuthor(author);
        return "redirect:/authors";
    }

    @GetMapping("/delete/{id}")
    public String deleteAuthor(@PathVariable(name = "id") Long id) {
        authorService.delete(id);
        return "redirect:/authors";
    }
}
