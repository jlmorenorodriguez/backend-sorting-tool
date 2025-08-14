package com.example.productsorting.application.dto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;
@Schema(description = "Producto ordenado con su puntuación")
public class ProductSortingResponse {
    @Schema(description = "Identificador único del producto", example = "1")
    private final String id;

    @Schema(description = "Nombre del producto", example = "V-NECH BASIC SHIRT")
    private final String name;

    @Schema(description = "Unidades vendidas del producto", example = "100")
    private final int salesUnits;

    @Schema(description = "Stock disponible por tallas", example = "{\"S\": 4, \"M\": 9, \"L\": 0}")
    private final Map<String, Integer> stock;

    @Schema(description = "Puntuación calculada para la ordenación", example = "5.67")
    private final double score;

    public ProductSortingResponse(
            String id,
            String name,
            int salesUnits,
            Map<String, Integer> stock,
            double score
    ) {
        this.id = id;
        this.name = name;
        this.salesUnits = salesUnits;
        this.stock = stock;
        this.score = score;
    }

    // Getters
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

    public double getScore() {
        return score;
    }
}