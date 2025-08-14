package com.example.productsorting.domain.service;

import com.example.productsorting.domain.model.Product;
import com.example.productsorting.domain.model.SalesCriterion;
import com.example.productsorting.domain.model.SortingCriteria;
import com.example.productsorting.domain.model.StockRatioCriterion;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class ProductSortingDomainServiceTest {

    @Test
    public void testCalculateProductScore() {
        ProductSortingDomainService service = new ProductSortingDomainService();
        Product product = new Product("1", "Test Product", 100, Map.of("S", 5, "M", 10, "L", 0));

        List<SortingCriteria> criteria = List.of(
                new SalesCriterion(0.7),
                new StockRatioCriterion(0.3)
        );

        double score = service.calculateProductScore(product, criteria);

        double expectedSalesScore = Math.log1p(100) * 0.7;
        double expectedStockScore = (2.0 / 3.0) * 0.3;
        double expectedTotalScore = expectedSalesScore + expectedStockScore;

        assertEquals(expectedTotalScore, score, 0.001);
    }

    @Test
    public void testSortProducts() {
        ProductSortingDomainService service = new ProductSortingDomainService();

        Product product1 = new Product("1", "Product 1", 100, Map.of("S", 5, "M", 10, "L", 0));
        Product product2 = new Product("2", "Product 2", 50, Map.of("S", 5, "M", 10, "L", 5));
        Product product3 = new Product("3", "Product 3", 200, Map.of("S", 0, "M", 0, "L", 5));

        List<Product> products = List.of(product1, product2, product3);

        List<SortingCriteria> criteria = List.of(
                new SalesCriterion(0.7),
                new StockRatioCriterion(0.3)
        );

        Map<Product, Double> sortedProducts = service.sortProducts(products, criteria);

        assertEquals(3, sortedProducts.size());

        Product[] sortedArray = sortedProducts.keySet().toArray(new Product[0]);

        assertEquals("3", sortedArray[0].getId());

        assertEquals("2", sortedArray[2].getId());
    }

    @Test
    public void testSortProductsWithOnlySalesCriterion() {
        ProductSortingDomainService service = new ProductSortingDomainService();

        Product product1 = new Product("1", "Product 1", 100, Map.of("S", 5, "M", 10, "L", 0));
        Product product2 = new Product("2", "Product 2", 50, Map.of("S", 5, "M", 10, "L", 5));
        Product product3 = new Product("3", "Product 3", 200, Map.of("S", 0, "M", 0, "L", 5));

        List<Product> products = List.of(product1, product2, product3);

        List<SortingCriteria> criteria = List.of(
                new SalesCriterion(1.0)
        );

        Map<Product, Double> sortedProducts = service.sortProducts(products, criteria);

        assertEquals(3, sortedProducts.size());

        Product[] sortedArray = sortedProducts.keySet().toArray(new Product[0]);

        assertEquals("3", sortedArray[0].getId());

        assertEquals("2", sortedArray[2].getId());
    }
}