package com.netstore.home.controller;

import com.netstore.home.model.Cart;
import com.netstore.home.model.Category;
import com.netstore.home.model.Product;
import com.netstore.home.model.User;
import com.netstore.home.model.weather.Forecast;
import com.netstore.home.model.weather.ForecastDto;
import com.netstore.home.service.CategoryService;
import com.netstore.home.service.ProductService;
import com.netstore.home.service.UserService;
import com.netstore.home.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final Cart cart;

    public ProductController(ProductService productService, CategoryService categoryService, Cart cart) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.cart = cart;
    }

    @ModelAttribute("cartSize")
    public int getCartSize() {
        return cart.getLinesForOrder().size();
    }


    @GetMapping("/products")
    public String getProducts(Principal principal,Model model, @PageableDefault(size = 9, sort = "title", direction = Sort.Direction.ASC) Pageable pageable) {

        List<Product> mostQuantityProduct = productService.findMostQuantity();
        log.info("list  of mostQuantityProduct size= " + mostQuantityProduct.size());
        model.addAttribute("products", mostQuantityProduct);

        Page<Product> pageProducts = productService.findAll(pageable);
        model.addAttribute("page", pageProducts);

        List<Sort.Order> sortOrders = pageProducts.getSort().stream().collect(Collectors.toList());
        if (sortOrders.size() > 0) {
            Sort.Order sortOrder = sortOrders.get(0);
            model.addAttribute("sortProperty", sortOrder.getProperty());
            model.addAttribute("sortDesc", sortOrder.getDirection() == Sort.Direction.DESC);
        }

        return "products/products";
    }

    @GetMapping("/viewProduct/{id}")
    public String getProductById(@PathVariable("id") Long id, Model model) {
        Optional<Product> find = productService.findById(id);
        if (find.isPresent()) {
            model.addAttribute("product", find.get());
        }

        return "products/product";
    }

    @GetMapping("/editProduct/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editProductById(@PathVariable("id") Long id, Model model) {
        log.info("in get update id: " + id);

        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categories", categoryList);

        Optional<Product> find = productService.findById(id);
        if (find.isPresent()) {
            model.addAttribute("product", find.get());
        }

        return "products/editProduct";
    }

    @PostMapping("/updateProduct")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateProduct(@ModelAttribute("product") final Product newProduct, @RequestParam Long id) {
        log.info("in post update id: " + id);
        newProduct.setId(id);
        Product savedProduct = productService.save(newProduct);
        return "redirect:/viewProduct/" + savedProduct.getId();
    }

    @GetMapping("/addProduct")
    @PreAuthorize("hasRole('ADMIN')")
    public String addProduct(Model model) {
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categories", categoryList);
        return "products/addProduct";
    }

    @PostMapping("/addNewProduct")
    @PreAuthorize("hasRole('ADMIN')")
    public String addNewProduct(@ModelAttribute("product") final Product newProduct) {
        Product addedProduct = productService.save(newProduct);
        return "redirect:/viewProduct/" + addedProduct.getId();
    }

    @GetMapping("/deleteProduct/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteById(id);
        return "redirect:/products";
    }
}
