package com.example.productsorting.application.factory;

import com.example.productsorting.domain.model.SalesCriterion;
import com.example.productsorting.domain.model.SortingCriteria;
import com.example.productsorting.domain.model.StockRatioCriterion;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class SortingCriteriaFactoryTest {

    @Test
    public void testCreateSalesCriterion() {
        SortingCriteriaFactory factory = new SortingCriteriaFactory();
        double weight = 0.7;

        SortingCriteria criterion = factory.createSalesCriterion(weight);

        assertTrue(criterion instanceof SalesCriterion);
        assertEquals(weight, ((SalesCriterion) criterion).getWeight(), 0.001);
    }

    @Test
    public void testCreateStockRatioCriterion() {
        SortingCriteriaFactory factory = new SortingCriteriaFactory();
        double weight = 0.3;

        SortingCriteria criterion = factory.createStockRatioCriterion(weight);

        assertTrue(criterion instanceof StockRatioCriterion);
        assertEquals(weight, ((StockRatioCriterion) criterion).getWeight(), 0.001);
    }

    @Test
    public void testCreateDefaultCriteria() {
        SortingCriteriaFactory factory = new SortingCriteriaFactory();
        double salesWeight = 0.7;
        double stockWeight = 0.3;
        List<SortingCriteria> criteria = factory.createDefaultCriteria(salesWeight, stockWeight);
        assertEquals(2, criteria.size());
        assertTrue(criteria.get(0) instanceof SalesCriterion);
        assertTrue(criteria.get(1) instanceof StockRatioCriterion);
        assertEquals(salesWeight, ((SalesCriterion) criteria.get(0)).getWeight(), 0.001);
        assertEquals(stockWeight, ((StockRatioCriterion) criteria.get(1)).getWeight(), 0.001);
    }

    @Test
    public void testCreateDefaultCriteriaWithZeroWeights() {
        SortingCriteriaFactory factory = new SortingCriteriaFactory();
        List<SortingCriteria> criteria = factory.createDefaultCriteria(0.0, 0.0);
        assertTrue(criteria.isEmpty());
    }

    @Test
    public void testCreateDefaultCriteriaWithNegativeWeights() {
        SortingCriteriaFactory factory = new SortingCriteriaFactory();
        List<SortingCriteria> criteria = factory.createDefaultCriteria(-0.7, -0.3);
        assertTrue(criteria.isEmpty());
    }
}