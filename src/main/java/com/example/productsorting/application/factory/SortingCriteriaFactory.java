package com.example.productsorting.application.factory;

import com.example.productsorting.domain.model.SalesCriterion;
import com.example.productsorting.domain.model.SortingCriteria;
import com.example.productsorting.domain.model.StockRatioCriterion;

import java.util.ArrayList;
import java.util.List;

public class SortingCriteriaFactory {

    public SortingCriteria createSalesCriterion(double weight) {
        return new SalesCriterion(weight);
    }

    public SortingCriteria createStockRatioCriterion(double weight) {
        return new StockRatioCriterion(weight);
    }

    /**
     * Creates a list with the default criteria
     * @param salesCriterionWeight Weight for sales criteria
     * @param stockRatioCriterionWeight Weight for stock ratio criteria
     * @return Criteria list
     */
    public List<SortingCriteria> createDefaultCriteria(double salesCriterionWeight, double stockRatioCriterionWeight) {
        List<SortingCriteria> criteria = new ArrayList<>();

        if (salesCriterionWeight > 0) {
            criteria.add(createSalesCriterion(salesCriterionWeight));
        }

        if (stockRatioCriterionWeight > 0) {
            criteria.add(createStockRatioCriterion(stockRatioCriterionWeight));
        }

        return criteria;
    }
}