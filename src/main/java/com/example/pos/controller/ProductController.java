package com.example.pos.controller;

import com.example.pos.dto.product.ProductRequest;
import com.example.pos.dto.product.ProductUpdateRequest;
import com.example.pos.model.Product;
import com.example.pos.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public List<Product> getAll(@RequestParam(required = false) Integer stockThreshold) {
        if (stockThreshold != null) {
            return service.findLowStock(stockThreshold);
        }
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody @Valid ProductRequest product) {
        return ResponseEntity.status(201).body(service.create(product));
    }

    @PatchMapping("/{id}")
    public Product update(@PathVariable Long id, @RequestBody @Valid ProductUpdateRequest product) {
        return service.update(id, product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
