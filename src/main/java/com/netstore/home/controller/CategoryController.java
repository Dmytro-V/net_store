package com.netstore.home.controller;

import com.netstore.home.model.Category;
import com.netstore.home.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Slf4j
@Controller
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping(value = {"/categories"})
    @PreAuthorize("hasAuthority('read')")
    public String getCategories(Model model) {
        log.info("IN CategoryController getCategories");
        List<Category> categoriesList = categoryService.findAll();
        model.addAttribute("categories", categoriesList);

        return "categories/categories";
    }

    @GetMapping("/addCategory")
    @PreAuthorize("hasAuthority('write')")
    public String addProduct(Model model) {
        log.info("IN CategoryController addCategory GET");
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categories", categoryList);
        return "categories/addCategory";
    }

    @PostMapping("/addNewCategory")
    @PreAuthorize("hasAuthority('write')")
    public String addNewProduct(@ModelAttribute("category") final Category newCategory) {
        log.info("IN CategoryController POST addCategory with name: {}, parent: {}", newCategory.getName(), newCategory.getParent());

        categoryService.saveCategory(newCategory);
        return "redirect:/categories";
    }

}
