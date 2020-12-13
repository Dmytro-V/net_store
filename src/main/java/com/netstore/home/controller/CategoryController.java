package com.netstore.home.controller;

import com.netstore.home.model.Category;
import com.netstore.home.model.Product;
import com.netstore.home.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping(value = {"/categories"})
    public String getCategories(Model model) {
        List<Category> productsList = categoryService.findAll();
        model.addAttribute("categories", productsList);
        return "categories/categories";
    }

    @GetMapping("/addCategory")
    public String addProduct(Model model) {
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categories", categoryList);
        return "categories/addCategory";
    }

    @PostMapping("/addNewCategory")
    public String addNewProduct(@ModelAttribute("category") final Category newCategory) {
        categoryService.saveCategory(newCategory);
        return "redirect:/categories";
    }

}
