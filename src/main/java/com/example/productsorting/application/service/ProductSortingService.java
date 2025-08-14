package com.example.productsorting.application.service;

import com.example.productsorting.domain.model.Product;
import com.example.productsorting.domain.model.SortingCriteria;
import com.example.productsorting.domain.service.ProductSortingDomainService;
import com.example.productsorting.application.dto.ProductSortingRequest;
import com.example.productsorting.application.dto.ProductSortingResponse;
import com.example.productsorting.domain.repository.ProductRepository;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductSortingService {
    private final ProductRepository productRepository;
    private final ProductSortingDomainService domainService;

    public ProductSortingService(ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.domainService = new ProductSortingDomainService();
    }

    public List<ProductSortingResponse> sortProducts(ProductSortingRequest request) {
        // Validate the criteria weights
        validateCriteriaWeights(request);

        // Get all products
        List<Product> products = productRepository.findAll();

        // Use the domain service to order products
        Map<Product, Double> sortedProducts = domainService.sortProducts(products, request.getCriteria());

        // Mapping to response DTOs
        return sortedProducts.entrySet().stream()
                .map(entry -> new ProductSortingResponse(
                        entry.getKey().getId(),
                        entry.getKey().getName(),
                        entry.getKey().getSalesUnits(),
                        entry.getKey().getStock(),
                        entry.getValue()
                ))
                .collect(Collectors.toList());
    }

    private void validateCriteriaWeights(ProductSortingRequest request) {
        if (request.getCriteria() == null || request.getCriteria().isEmpty()) {
            throw new IllegalArgumentException("Se requiere al menos un criterio de ordenación");
        }

        double totalWeight = request.getCriteria().stream()
                .mapToDouble(criteria -> getWeight(criteria))
                .sum();

        if (totalWeight <= 0) {
            throw new IllegalArgumentException("La suma de los pesos de los criterios debe ser mayor que cero");
        }
    }

    private double getWeight(SortingCriteria criteria) {
        if (criteria instanceof com.example.productsorting.domain.model.SalesCriterion) {
            return ((com.example.productsorting.domain.model.SalesCriterion) criteria).getWeight();
        } else if (criteria instanceof com.example.productsorting.domain.model.StockRatioCriterion) {
            return ((com.example.productsorting.domain.model.StockRatioCriterion) criteria).getWeight();
        }
        return 0;
    }
}