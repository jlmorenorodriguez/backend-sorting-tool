package com.example.productsorting.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SortingCriteriaTest {

    @Test
    public void testSalesCriterion() {
        Product lowSales = new Product("1", "Low Sales", 10, java.util.Map.of("S", 5));
        Product mediumSales = new Product("2", "Medium Sales", 100, java.util.Map.of("S", 5));
        Product highSales = new Product("3", "High Sales", 1000, java.util.Map.of("S", 5));

        SalesCriterion salesCriterion = new SalesCriterion(1.0);

        double lowScore = salesCriterion.calculateScore(lowSales);
        double mediumScore = salesCriterion.calculateScore(mediumSales);
        double highScore = salesCriterion.calculateScore(highSales);

        assertTrue(lowScore < mediumScore);
        assertTrue(mediumScore < highScore);

        assertEquals(Math.log1p(10), lowScore, 0.001);
        assertEquals(Math.log1p(100), mediumScore, 0.001);
        assertEquals(Math.log1p(1000), highScore, 0.001);
    }

    @Test
    public void testStockRatioCriterion() {
        Product noStock = new Product("1", "No Stock", 100, java.util.Map.of("S", 0, "M", 0, "L", 0));
        Product halfStock = new Product("2", "Half Stock", 100, java.util.Map.of("S", 5, "M", 0, "L", 5));
        Product fullStock = new Product("3", "Full Stock", 100, java.util.Map.of("S", 5, "M", 5, "L", 5));

        StockRatioCriterion stockCriterion = new StockRatioCriterion(1.0);

        double noStockScore = stockCriterion.calculateScore(noStock);
        double halfStockScore = stockCriterion.calculateScore(halfStock);
        double fullStockScore = stockCriterion.calculateScore(fullStock);

        assertTrue(noStockScore < halfStockScore);
        assertTrue(halfStockScore < fullStockScore);

        assertEquals(0.0, noStockScore, 0.001);
        assertEquals(0.666, halfStockScore, 0.001);
        assertEquals(1.0, fullStockScore, 0.001);
    }

    @Test
    public void testCriteriaWithDifferentWeights() {
        Product product = new Product("1", "Test Product", 100, java.util.Map.of("S", 5, "M", 0, "L", 5));

        SalesCriterion lowWeight = new SalesCriterion(0.5);
        SalesCriterion highWeight = new SalesCriterion(2.0);

        double lowWeightScore = lowWeight.calculateScore(product);
        double highWeightScore = highWeight.calculateScore(product);

        assertEquals(lowWeightScore * 4, highWeightScore, 0.001);
    }
}