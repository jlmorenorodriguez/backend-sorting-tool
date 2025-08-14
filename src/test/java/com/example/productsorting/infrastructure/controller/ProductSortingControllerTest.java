package com.example.productsorting.infrastructure.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.productsorting.application.dto.ProductSortingResponse;
import com.example.productsorting.application.service.ProductSortingService;
import com.example.productsorting.domain.model.SortingCriteria;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebMvcTest(ProductSortingController.class)
public class ProductSortingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductSortingService productSortingService;

    @Autowired
    private ObjectMapper objectMapper;

    private List<ProductSortingResponse> mockResponse;

    @BeforeEach
    public void setup() {
        Map<String, Integer> stock1 = new HashMap<>();
        stock1.put("S", 4);
        stock1.put("M", 9);
        stock1.put("L", 0);

        Map<String, Integer> stock2 = new HashMap<>();
        stock2.put("S", 35);
        stock2.put("M", 9);
        stock2.put("L", 9);

        mockResponse = Arrays.asList(
                new ProductSortingResponse("1", "V-NECH BASIC SHIRT", 100, stock1, 5.0),
                new ProductSortingResponse("2", "CONTRASTING FABRIC T-SHIRT", 50, stock2, 4.0)
        );
    }

    @Test
    public void testSortProducts() throws Exception {
        when(productSortingService.sortProducts(any())).thenReturn(mockResponse);

        ProductSortingController.SortingCriteriaRequest request =
                new ProductSortingController.SortingCriteriaRequest(0.7, 0.3);

        mockMvc.perform(post("/api/products/sort")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is("1")))
                .andExpect(jsonPath("$[0].name", is("V-NECH BASIC SHIRT")))
                .andExpect(jsonPath("$[0].score", is(5.0)))
                .andExpect(jsonPath("$[1].id", is("2")))
                .andExpect(jsonPath("$[1].name", is("CONTRASTING FABRIC T-SHIRT")));
    }

    @Test
    public void testSortProductsWithMissingCriteria() throws Exception {
        ProductSortingController.SortingCriteriaRequest request =
                new ProductSortingController.SortingCriteriaRequest(null, null);

        mockMvc.perform(post("/api/products/sort")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testSortProductsWithInvalidCriteria() throws Exception {
        when(productSortingService.sortProducts(any()))
                .thenThrow(new IllegalArgumentException("Criterios inválidos"));

        ProductSortingController.SortingCriteriaRequest request =
                new ProductSortingController.SortingCriteriaRequest(-0.7, -0.3);

        mockMvc.perform(post("/api/products/sort")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}