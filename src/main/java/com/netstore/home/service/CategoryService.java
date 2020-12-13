package com.netstore.home.service;

import com.netstore.home.model.Category;
import com.netstore.home.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public void saveCategory(Category newCategory) {
        categoryRepository.save(newCategory);
        Category parentCategory = categoryRepository.getOne(newCategory.getParent().getId());
        parentCategory.getChild().add(newCategory);
        categoryRepository.save(parentCategory);
    }
}
