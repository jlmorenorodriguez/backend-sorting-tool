package com.example.productsorting.domain.model;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Product {
    private final String id;
    private final String name;
    private final int salesUnits;
    private final Map<String, Integer> stock;

    public Product(String id, String name, int salesUnits, Map<String, Integer> stock) {
        this.id = Objects.requireNonNull(id, "Id cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.salesUnits = salesUnits;
        this.stock = Objects.requireNonNull(stock, "Stock cannot be null");
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getSalesUnits() {
        return salesUnits;
    }

    public Map<String, Integer> getStock() {
        return stock;
    }

    public double calculateStockRatio() {
        if (stock.isEmpty()) return 0.0;

        long availableSizes = stock.values().stream()
                .filter(quantity -> quantity > 0)
                .count();

        return (double) availableSizes / stock.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return salesUnits == product.salesUnits &&
                Objects.equals(id, product.id) &&
                Objects.equals(name, product.name) &&
                Objects.equals(stock, product.stock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, salesUnits, stock);
    }
}
