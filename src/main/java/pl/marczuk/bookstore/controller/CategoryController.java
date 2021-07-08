package pl.marczuk.bookstore.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.marczuk.bookstore.model.Category;
import pl.marczuk.bookstore.service.CategoryService;


import javax.validation.Valid;
import java.io.IOException;


@Controller
@RequestMapping("/categories")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping()
    public String categories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "/admin/category/manageCategory";
    }
    @GetMapping("/add")
    public String addCategory(Model model) {
        Category category = new Category();
        model.addAttribute("category", category);
        return "admin/category/addCategory";
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addCategory(@ModelAttribute("category") @Valid Category category, BindingResult bindingResult, Model model) throws IOException {
        if(bindingResult.hasErrors())
        {
            model.addAttribute("category", category);
            return "admin/category/addCategory";
        }
        categoryService.add(category);

        return "redirect:/categories";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editCategory(@PathVariable(name = "id") Long id, Model model) {
        Category category = categoryService.findById(id);
        model.addAttribute("category", category);
        return "admin/category/editCategory";
    }

    @PostMapping("/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editCategory(@ModelAttribute("category") Category category, BindingResult bindingResult, Model model) throws IOException {
        if(bindingResult.hasErrors())
        {
            model.addAttribute("category", category);
            return "admin/author/editAuthor";
        }
        categoryService.editCategory(category);
        return "redirect:/categories";
    }


    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable(name = "id") Long id) {
        categoryService.delete(id);
        return "redirect:/categories";
    }
}
