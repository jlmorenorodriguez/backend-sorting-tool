package com.example.productsorting;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import com.example.productsorting.infrastructure.controller.ProductSortingController;
import com.example.productsorting.infrastructure.entity.ProductEntity;
import com.example.productsorting.infrastructure.repository.ProductMongoRepository;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class ProductSortingApiIT {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.6");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @LocalServerPort
    private int port;

    @Autowired
    private ProductMongoRepository productMongoRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        productMongoRepository.deleteAll();

        ProductEntity product1 = new ProductEntity("1", "V-NECH BASIC SHIRT", 100, Map.of("S", 4, "M", 9, "L", 0));
        ProductEntity product2 = new ProductEntity("2", "CONTRASTING FABRIC T-SHIRT", 50, Map.of("S", 35, "M", 9, "L", 9));
        ProductEntity product3 = new ProductEntity("3", "RAISED PRINT T-SHIRT", 80, Map.of("S", 20, "M", 2, "L", 20));
        ProductEntity product4 = new ProductEntity("4", "PLEATED T-SHIRT", 3, Map.of("S", 25, "M", 30, "L", 10));
        ProductEntity product5 = new ProductEntity("5", "CONTRASTING LACE T-SHIRT", 650, Map.of("S", 0, "M", 1, "L", 0));
        ProductEntity product6 = new ProductEntity("6", "SLOGAN T-SHIRT", 20, Map.of("S", 9, "M", 2, "L", 5));

        productMongoRepository.save(product1);
        productMongoRepository.save(product2);
        productMongoRepository.save(product3);
        productMongoRepository.save(product4);
        productMongoRepository.save(product5);
        productMongoRepository.save(product6);
    }

    @Test
    void testSortProductsWithSalesCriteria() {
        ProductSortingController.SortingCriteriaRequest request =
                new ProductSortingController.SortingCriteriaRequest(1.0, 0.0);

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/products/sort")
                .then()
                .statusCode(200)
                .body("$", hasSize(6))
                .body("[0].id", equalTo("5"))  // Producto con más ventas (650)
                .body("[0].name", equalTo("CONTRASTING LACE T-SHIRT"))
                .body("[5].id", equalTo("4"));  // Producto con menos ventas (3)
    }

    @Test
    void testSortProductsWithStockRatioCriteria() {
        ProductSortingController.SortingCriteriaRequest request =
                new ProductSortingController.SortingCriteriaRequest(0.0, 1.0);

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/products/sort")
                .then()
                .statusCode(200)
                .body("$", hasSize(6))
                .body("[0].id", equalTo("2"))  // Producto con mejor ratio de stock (todas las tallas)
                .body("[5].id", equalTo("5"));  // Producto con peor ratio de stock (solo 1 talla)
    }

    @Test
    void testSortProductsWithCombinedCriteria() {
        ProductSortingController.SortingCriteriaRequest request =
                new ProductSortingController.SortingCriteriaRequest(0.7, 0.3);

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/products/sort")
                .then()
                .statusCode(200)
                .body("$", hasSize(6));
    }
}