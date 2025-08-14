package com.example.productsorting.domain.model;

public class SalesCriterion implements SortingCriteria {
    private final double weight;

    public SalesCriterion(double weight) {
        this.weight = weight;
    }

    @Override
    public double calculateScore(Product product) {
        // Simple sales-based standardisation
        return Math.log1p(product.getSalesUnits()) * weight;
    }

    public double getWeight() {
        return weight;
    }
}
