package com.example.productsorting.domain.service;

import com.example.productsorting.domain.model.Product;
import com.example.productsorting.domain.model.SortingCriteria;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

/**
 * Domain service for the ordering of products
 */
public class ProductSortingDomainService {

    /**
     * Calculates the total score of a product according to the criteria provided.
     * The product to be evaluated
     * @param criteria List of criteria with their respective weights
     * @return The calculated score
     */
    public double calculateProductScore(Product product, List<SortingCriteria> criteria) {
        return criteria.stream()
                .mapToDouble(criterion -> criterion.calculateScore(product))
                .sum();
    }

    /**
     * Sorts a list of products according to the criteria provided.
     * @param products List of products to be ordered
     * @param criteria List of criteria with their respective weights
     * @return Sorted map with products and their scores
     */
    public Map<Product, Double> sortProducts(List<Product> products, List<SortingCriteria> criteria) {
        return products.stream()
                .collect(Collectors.toMap(
                        product -> product,
                        product -> calculateProductScore(product, criteria),
                        (e1, e2) -> e1,
                        () -> new LinkedHashMap<>()
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<Product, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
}