package com.example.productsorting.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.productsorting.domain.model.Product;
import com.example.productsorting.domain.model.SalesCriterion;
import com.example.productsorting.domain.model.SortingCriteria;
import com.example.productsorting.domain.model.StockRatioCriterion;
import com.example.productsorting.domain.repository.ProductRepository;
import com.example.productsorting.application.dto.ProductSortingRequest;
import com.example.productsorting.application.dto.ProductSortingResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProductSortingServiceTest {

    @Mock
    private ProductRepository productRepository;

    private ProductSortingService productSortingService;

    private List<Product> testProducts;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        productSortingService = new ProductSortingService(productRepository);

        testProducts = Arrays.asList(
                new Product("1", "V-NECH BASIC SHIRT", 100,
                        Map.of("S", 4, "M", 9, "L", 0)),

                new Product("2", "CONTRASTING FABRIC T-SHIRT", 50,
                        Map.of("S", 35, "M", 9, "L", 9)),

                new Product("3", "RAISED PRINT T-SHIRT", 80,
                        Map.of("S", 20, "M", 2, "L", 20)),

                new Product("4", "PLEATED T-SHIRT", 3,
                        Map.of("S", 25, "M", 30, "L", 10)),

                new Product("5", "CONTRASTING LACE T-SHIRT", 650,
                        Map.of("S", 0, "M", 1, "L", 0)),

                new Product("6", "SLOGAN T-SHIRT", 20,
                        Map.of("S", 9, "M", 2, "L", 5))
        );
    }

    @Test
    public void testCalculateStockRatio() {
        assertEquals(0.67, testProducts.get(0).calculateStockRatio(), 0.01); // ID 1
        assertEquals(1.0, testProducts.get(1).calculateStockRatio(), 0.01);  // ID 2
        assertEquals(1.0, testProducts.get(2).calculateStockRatio(), 0.01);  // ID 3
        assertEquals(1.0, testProducts.get(3).calculateStockRatio(), 0.01);  // ID 4
        assertEquals(0.33, testProducts.get(4).calculateStockRatio(), 0.01); // ID 5
        assertEquals(1.0, testProducts.get(5).calculateStockRatio(), 0.01);  // ID 6
    }

    @Test
    public void testSortProductsBySalesUnit() {
        when(productRepository.findAll()).thenReturn(testProducts);

        List<SortingCriteria> criteria = new ArrayList<>();
        criteria.add(new SalesCriterion(1.0));

        ProductSortingRequest request = new ProductSortingRequest(criteria);

        List<ProductSortingResponse> result = productSortingService.sortProducts(request);

        assertNotNull(result);
        assertEquals(6, result.size());

        assertEquals("5", result.get(0).getId());
        assertEquals("CONTRASTING LACE T-SHIRT", result.get(0).getName());

        assertEquals("4", result.get(5).getId());
        assertEquals("PLEATED T-SHIRT", result.get(5).getName());
    }

    @Test
    public void testSortProductsByStockRatio() {
        when(productRepository.findAll()).thenReturn(testProducts);

        List<SortingCriteria> criteria = new ArrayList<>();
        criteria.add(new StockRatioCriterion(1.0));

        ProductSortingRequest request = new ProductSortingRequest(criteria);

        List<ProductSortingResponse> result = productSortingService.sortProducts(request);

        assertNotNull(result);
        assertEquals(6, result.size());

        List<String> idsWithFullStock = Arrays.asList(result.get(0).getId(),
                result.get(1).getId(),
                result.get(2).getId(),
                result.get(3).getId());

        assertTrue(idsWithFullStock.contains("2"));
        assertTrue(idsWithFullStock.contains("3"));
        assertTrue(idsWithFullStock.contains("4"));
        assertTrue(idsWithFullStock.contains("6"));

        assertEquals("1", result.get(4).getId());

        assertEquals("5", result.get(5).getId());

        for (int i = 0; i < result.size(); i++) {
            String id = result.get(i).getId();
            double ratio = 0;
            for (Product p : testProducts) {
                if (p.getId().equals(id)) {
                    ratio = p.calculateStockRatio();
                    break;
                }
            }
            System.out.println("Posición " + i + ": ID " + id + " - Ratio: " + ratio);
        }
    }

    @Test
    public void testSortProductsWithCombinedCriteria() {
        when(productRepository.findAll()).thenReturn(testProducts);

        List<SortingCriteria> criteria = new ArrayList<>();
        criteria.add(new SalesCriterion(0.7));
        criteria.add(new StockRatioCriterion(0.3));

        ProductSortingRequest request = new ProductSortingRequest(criteria);

        List<ProductSortingResponse> result = productSortingService.sortProducts(request);

        assertNotNull(result);
        assertEquals(6, result.size());

        assertEquals("5", result.get(0).getId());
    }

    @Test
    public void testInvalidCriteriaWeights() {
        List<SortingCriteria> criteria = new ArrayList<>();
        criteria.add(new SalesCriterion(-1.0));
        criteria.add(new StockRatioCriterion(-0.5));

        ProductSortingRequest request = new ProductSortingRequest(criteria);

        assertThrows(IllegalArgumentException.class, () -> {
            productSortingService.sortProducts(request);
        });
    }

    @Test
    public void testEmptyCriteria() {
        List<SortingCriteria> criteria = new ArrayList<>();
        ProductSortingRequest request = new ProductSortingRequest(criteria);

        assertThrows(IllegalArgumentException.class, () -> {
            productSortingService.sortProducts(request);
        });
    }
}