package pl.marczuk.bookstore.service;

import pl.marczuk.bookstore.model.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCategories();
    Category add(Category category);
    void delete(Long id);
    void deleteByName(String name);
    Category findByName(String name);
    Category findById(Long id);
    Category editCategory(Category category);
}
