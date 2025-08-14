package com.example.productsorting.domain.repository;

import com.example.productsorting.domain.model.Product;

import java.util.List;

public interface ProductRepository {
    /**
     * Search for all available products
     * @return List of products in the domain
     */
    List<Product> findAll();

    /**
     * Saves a product
     * @param product The product to save
     * @return The product saved
     */
    Product save(Product product);

    /**
     * Searches for a product by its ID
     * @param id Product ID
     * @return The corresponding product, or null if it does not exist
     */
    Product findById(String id);
}