package com.example.productsorting.domain.model;

public interface SortingCriteria {
    double calculateScore(Product product);
}