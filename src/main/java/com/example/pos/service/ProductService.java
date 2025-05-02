package com.example.pos.service;

import com.example.pos.common.NotFoundException;
import com.example.pos.dto.product.ProductRequest;
import com.example.pos.dto.product.ProductUpdateRequest;
import com.example.pos.model.Product;
import com.example.pos.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public List<Product> findAll() {
        return repo.findAll();
    }

    public Product findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new NotFoundException("Product not found with id: " + id));
    }

    public Product create(ProductRequest request) {
        Product product = new Product();
        product.setName(request.name);
        product.setDescription(request.description);
        product.setCostPrice(request.costPrice);
        product.setSellingPrice(request.sellingPrice);
        product.setQuantity(request.quantity);
        product.setCreatedAt(LocalDateTime.now());

        return repo.save(product);
    }

    public Product update(Long id, ProductUpdateRequest request) {
        Product existing = findById(id);

        if (request.name != null && !request.name.isBlank()) {
            existing.setName(request.name);
        }

        if (request.description != null && !request.description.isBlank()) {
            existing.setDescription(request.description);
        }

        if (request.costPrice != null) {
            existing.setCostPrice(request.costPrice);
        }

        if (request.sellingPrice != null) {
            existing.setSellingPrice(request.sellingPrice);
        }

        if (request.quantity != null) {
            existing.setQuantity(request.quantity);
        }

        return repo.save(existing);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new NotFoundException("Product not found with id: " + id);
        }
        repo.deleteById(id);
    }

    public List<Product> findLowStock(int threshold) {
        return repo.findByQuantityLessThan(threshold);
    }
}
