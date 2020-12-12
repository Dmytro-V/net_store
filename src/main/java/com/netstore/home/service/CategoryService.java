package com.netstore.home.service;

import com.netstore.home.model.Category;
import com.netstore.home.model.Product;
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
}
