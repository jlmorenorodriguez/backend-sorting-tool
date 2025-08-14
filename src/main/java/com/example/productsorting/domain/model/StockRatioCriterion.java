package com.example.productsorting.domain.model;

public class StockRatioCriterion implements SortingCriteria {
    private final double weight;

    public StockRatioCriterion(double weight) {
        this.weight = weight;
    }

    @Override
    public double calculateScore(Product product) {
        return product.calculateStockRatio() * weight;
    }

    public double getWeight() {
        return weight;
    }
}