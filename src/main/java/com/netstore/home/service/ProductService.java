package com.netstore.home.service;

import com.netstore.home.model.Product;
import com.netstore.home.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }


    public List<Product> findMostQuantity() {
       return productRepository.findAll(Sort.by(Sort.Direction.DESC, "quantity")).subList(0,3);
    }

    public Optional<Product> findByTitle(String title) {
       return productRepository.findByTitle(title);
    }
}
