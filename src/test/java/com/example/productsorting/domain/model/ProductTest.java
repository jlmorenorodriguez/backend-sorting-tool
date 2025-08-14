package com.example.productsorting.domain.model;

import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    @Test
    public void testProductCreation() {
        Product product = new Product("1", "Test Product", 100, Map.of("S", 5, "M", 10, "L", 0));

        assertEquals("1", product.getId());
        assertEquals("Test Product", product.getName());
        assertEquals(100, product.getSalesUnits());
        assertEquals(3, product.getStock().size());
        assertEquals(5, product.getStock().get("S"));
        assertEquals(10, product.getStock().get("M"));
        assertEquals(0, product.getStock().get("L"));
    }

    @Test
    public void testNullValues() {
        assertThrows(NullPointerException.class, () -> {
            new Product(null, "Test Product", 100, Map.of("S", 5));
        });

        assertThrows(NullPointerException.class, () -> {
            new Product("1", null, 100, Map.of("S", 5));
        });

        assertThrows(NullPointerException.class, () -> {
            new Product("1", "Test Product", 100, null);
        });
    }

    @Test
    public void testEqualsAndHashCode() {
        Product product1 = new Product("1", "Test Product", 100, Map.of("S", 5, "M", 10, "L", 0));
        Product product2 = new Product("1", "Test Product", 100, Map.of("S", 5, "M", 10, "L", 0));

        Product product3 = new Product("2", "Test Product", 100, Map.of("S", 5, "M", 10, "L", 0));

        assertEquals(product1, product2);
        assertEquals(product1.hashCode(), product2.hashCode());
        assertNotEquals(product1, product3);
        assertNotEquals(product1.hashCode(), product3.hashCode());
    }

    @Test
    public void testCalculateStockRatio() {
        Product product1 = new Product("1", "Test Product", 100, Map.of("S", 5, "M", 10, "L", 15));
        assertEquals(1.0, product1.calculateStockRatio(), 0.001);

        Product product2 = new Product("2", "Test Product", 100, Map.of("S", 5, "M", 0, "L", 15));
        assertEquals(0.666, product2.calculateStockRatio(), 0.001);

        Product product3 = new Product("3", "Test Product", 100, Map.of("S", 0, "M", 0, "L", 0));
        assertEquals(0.0, product3.calculateStockRatio(), 0.001);

        Product product4 = new Product("4", "Test Product", 100, Map.of());
        assertEquals(0.0, product4.calculateStockRatio(), 0.001);
    }
}