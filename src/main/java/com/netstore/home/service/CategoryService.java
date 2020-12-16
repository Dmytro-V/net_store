package com.netstore.home.service;

import com.netstore.home.model.Category;
import com.netstore.home.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    public List<Category> findAll() {
        log.info("IN CategoryService findAll");
        return categoryRepository.findAll();
    }

    public void saveCategory(Category newCategory) {
        log.info("IN CategoryService saveCategory");

        categoryRepository.save(newCategory);
        Category parent = newCategory.getParent();
        if (parent != null) {
            Category parentCategory = categoryRepository.getOne(newCategory.getParent().getId());
            parentCategory.getChildren().add(newCategory);
            categoryRepository.save(parentCategory);
        }
    }
}
