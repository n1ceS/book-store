package pl.marczuk.bookstore.service.implementation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.marczuk.bookstore.exception.ResourceNotFoundException;
import pl.marczuk.bookstore.model.Book;
import pl.marczuk.bookstore.model.Category;
import pl.marczuk.bookstore.repository.CategoryRepository;
import pl.marczuk.bookstore.service.CategoryService;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional
    public Category add(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " not found!"));
        categoryRepository.delete(category);
    }

    @Override
    @Transactional
    public void deleteByName(String name) {
        Category category = categoryRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Category with name " + name + " not found!"));
        categoryRepository.delete(category);
    }

    @Override
    @Transactional
    public Category findByName(String name) {
        Category category = categoryRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Category with name " + name + " not found!"));
        return category;
    }

    @Override
    @Transactional
    public Category findById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category with id " + id + " not found!"));
        return category;
    }

    @Override
    @Transactional
    public Category editCategory(Category category) {
        Category categoryToEdit = categoryRepository.findById(category.getId()).orElseThrow(() -> new ResourceNotFoundException("Category with id " + category.getId() + " not found!"));
        categoryToEdit.setName(category.getName());
        return category;
    }
}
