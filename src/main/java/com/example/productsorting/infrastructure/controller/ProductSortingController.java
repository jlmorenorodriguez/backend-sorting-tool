package com.example.productsorting.infrastructure.controller;

import com.example.productsorting.application.dto.ProductSortingRequest;
import com.example.productsorting.application.dto.ProductSortingResponse;
import com.example.productsorting.application.factory.SortingCriteriaFactory;
import com.example.productsorting.application.service.ProductSortingService;
import com.example.productsorting.domain.model.SortingCriteria;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "API for product management and sorting")
public class ProductSortingController {

    private final ProductSortingService productSortingService;
    private final SortingCriteriaFactory criteriaFactory = new SortingCriteriaFactory();

    public ProductSortingController(ProductSortingService productSortingService) {
        this.productSortingService = productSortingService;
    }

    @Operation(
            summary = "Sort products by criteria",
            description = "Sorts the product list according to the provided criteria and weights"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Products sorted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductSortingResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid parameters",
                    content = @Content
            )
    })
    @PostMapping("/sort")
    public ResponseEntity<List<ProductSortingResponse>> sortProducts(
            @Parameter(description = "Sorting criteria with their weights", required = true)
            @RequestBody SortingCriteriaRequest request) {

        // Validate the request
        if (request == null || request.getSalesCriterionWeight() == null || request.getStockRatioCriterionWeight() == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            // Create sorting criteria using the factory
            List<SortingCriteria> criteria = criteriaFactory.createDefaultCriteria(
                    request.getSalesCriterionWeight(),
                    request.getStockRatioCriterionWeight()
            );

            // Validate that there is at least one criterion
            if (criteria.isEmpty()) {
                throw new IllegalArgumentException("At least one criterion must have a weight greater than zero");
            }

            // Create the request for the service
            ProductSortingRequest sortingRequest = new ProductSortingRequest(criteria);

            // Execute the sorting service
            List<ProductSortingResponse> sortedProducts = productSortingService.sortProducts(sortingRequest);

            return ResponseEntity.ok(sortedProducts);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // DTO for the REST request
    @Schema(description = "Sorting request with weights for each criterion")
    public static class SortingCriteriaRequest {
        @Schema(description = "Weight for the sales criterion (between 0.0 and 1.0)", example = "0.7")
        private Double salesCriterionWeight;

        @Schema(description = "Weight for the stock ratio criterion (between 0.0 and 1.0)", example = "0.3")
        private Double stockRatioCriterionWeight;

        // Empty constructor needed for deserialization
        public SortingCriteriaRequest() {}

        public SortingCriteriaRequest(Double salesCriterionWeight, Double stockRatioCriterionWeight) {
            this.salesCriterionWeight = salesCriterionWeight;
            this.stockRatioCriterionWeight = stockRatioCriterionWeight;
        }

        public Double getSalesCriterionWeight() {
            return salesCriterionWeight;
        }

        public void setSalesCriterionWeight(Double salesCriterionWeight) {
            this.salesCriterionWeight = salesCriterionWeight;
        }

        public Double getStockRatioCriterionWeight() {
            return stockRatioCriterionWeight;
        }

        public void setStockRatioCriterionWeight(Double stockRatioCriterionWeight) {
            this.stockRatioCriterionWeight = stockRatioCriterionWeight;
        }
    }
}