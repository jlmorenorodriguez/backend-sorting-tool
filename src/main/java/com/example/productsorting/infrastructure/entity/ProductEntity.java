package com.example.productsorting.infrastructure.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "products")
public class ProductEntity {
    @Id
    private String id;
    private String name;
    private int salesUnits;
    private Map<String, Integer> stock;

    public ProductEntity() {}

    public ProductEntity(String id, String name, int salesUnits, Map<String, Integer> stock) {
        this.id = id;
        this.name = name;
        this.salesUnits = salesUnits;
        this.stock = stock;
    }

    // Getters y setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSalesUnits() {
        return salesUnits;
    }

    public void setSalesUnits(int salesUnits) {
        this.salesUnits = salesUnits;
    }

    public Map<String, Integer> getStock() {
        return stock;
    }

    public void setStock(Map<String, Integer> stock) {
        this.stock = stock;
    }
}