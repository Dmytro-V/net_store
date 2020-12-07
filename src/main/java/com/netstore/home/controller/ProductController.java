package com.netstore.home.controller;

import com.netstore.home.model.Product;
import com.netstore.home.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = {"/products"})
    public String getProducts(Model model) {
        List<Product> productsList = productService.findAll();
        model.addAttribute("products", productsList);
        return "products/products";
    }

    @GetMapping("/viewProduct")
    public String getProductById(@RequestParam Long id, Model model) {
        Optional<Product> find = productService.findById(id);
        if (find.isPresent()) {
            model.addAttribute("product", find.get());
        }

        return "products/product";
    }

    @GetMapping("/editProduct")
    public String editProductById(@RequestParam Long id, Model model) {
        log.info("in get update id: " + id);

        Optional<Product> find = productService.findById(id);
        if (find.isPresent()) {
            model.addAttribute("product", find.get());
        }

        return "products/editProduct";
    }

    @PostMapping("/updateProduct")
    public String updateProduct(@ModelAttribute("product") final Product newProduct, @RequestParam Long id) {
        log.info("in post update id: " + id);
        newProduct.setId(id);
        Product savedProduct = productService.saveProduct(newProduct);
        return "redirect:/viewProduct?id=" + savedProduct.getId();
    }

    @GetMapping("/addProduct")
    public String addProduct() {
        return "products/addProduct";
    }

    @PostMapping("/addNewProduct")
    public String addNewProduct(@ModelAttribute("product") final Product newProduct) {
        Product addedProduct = productService.saveProduct(newProduct);
        return "redirect:/viewProduct?id=" + addedProduct.getId();
    }

    @GetMapping("/deleteProduct")
    public String deleteProduct(@RequestParam Long id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }
}
