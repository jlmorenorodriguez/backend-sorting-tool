package com.example.productsorting.application.dto;

import com.example.productsorting.domain.model.SortingCriteria;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Solicitud para ordenar productos")
public class ProductSortingRequest {
    @Schema(description = "Lista de criterios de ordenación con sus pesos")
    private final List<SortingCriteria> criteria;

    public ProductSortingRequest(List<SortingCriteria> criteria) {
        this.criteria = criteria;
    }

    public List<SortingCriteria> getCriteria() {
        return criteria;
    }
}