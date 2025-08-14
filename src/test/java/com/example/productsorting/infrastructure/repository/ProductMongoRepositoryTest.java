package com.example.productsorting.infrastructure.repository;

import com.example.productsorting.domain.model.Product;
import com.example.productsorting.infrastructure.entity.ProductEntity;
import com.example.productsorting.infrastructure.repository.impl.ProductRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@Testcontainers
@Import(ProductRepositoryImpl.class)
public class ProductMongoRepositoryTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.6");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private ProductMongoRepository mongoRepository;

    @Autowired
    private ProductRepositoryImpl productRepository;

    private ProductEntity testProductEntity;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        mongoRepository.deleteAll();

        Map<String, Integer> stock = new LinkedHashMap<>();
        stock.put("S", 10);
        stock.put("M", 5);
        stock.put("L", 0);

        testProductEntity = new ProductEntity("test1", "Test Product", 100, stock);

        testProduct = new Product("test2", "Domain Test Product", 200, stock);
    }

    @Test
    void testMongoRepositorySaveAndFindById() {
        ProductEntity savedEntity = mongoRepository.save(testProductEntity);

        assertNotNull(savedEntity);
        assertEquals("test1", savedEntity.getId());

        Optional<ProductEntity> foundEntity = mongoRepository.findById("test1");

        assertTrue(foundEntity.isPresent());
        assertEquals("Test Product", foundEntity.get().getName());
        assertEquals(100, foundEntity.get().getSalesUnits());

        Map<String, Integer> foundStock = foundEntity.get().getStock();
        assertEquals(3, foundStock.size());
        assertEquals(10, foundStock.get("S"));
        assertEquals(5, foundStock.get("M"));
        assertEquals(0, foundStock.get("L"));
    }

    @Test
    void testMongoRepositoryFindAll() {
        ProductEntity entity1 = new ProductEntity("test1", "Product 1", 100, Map.of("S", 10));
        ProductEntity entity2 = new ProductEntity("test2", "Product 2", 200, Map.of("M", 20));

        mongoRepository.saveAll(List.of(entity1, entity2));

        List<ProductEntity> allEntities = mongoRepository.findAll();

        assertEquals(2, allEntities.size());

        List<String> ids = allEntities.stream().map(ProductEntity::getId).toList();
        assertTrue(ids.contains("test1"));
        assertTrue(ids.contains("test2"));
    }

    @Test
    void testDomainRepositorySaveAndFindById() {
        Product savedProduct = productRepository.save(testProduct);

        assertNotNull(savedProduct);
        assertEquals("test2", savedProduct.getId());
        assertEquals("Domain Test Product", savedProduct.getName());

        Product foundProduct = productRepository.findById("test2");

        assertNotNull(foundProduct);
        assertEquals("Domain Test Product", foundProduct.getName());
        assertEquals(200, foundProduct.getSalesUnits());

        Map<String, Integer> foundStock = foundProduct.getStock();
        assertEquals(3, foundStock.size());
        assertEquals(10, foundStock.get("S"));
        assertEquals(5, foundStock.get("M"));
        assertEquals(0, foundStock.get("L"));
    }

    @Test
    void testDomainRepositoryFindAll() {
        productRepository.save(new Product("test1", "Product 1", 100, Map.of("S", 10)));
        productRepository.save(new Product("test2", "Product 2", 200, Map.of("M", 20)));

        List<Product> allProducts = productRepository.findAll();

        assertEquals(2, allProducts.size());

        List<String> ids = allProducts.stream().map(Product::getId).toList();
        assertTrue(ids.contains("test1"));
        assertTrue(ids.contains("test2"));
    }

    @Test
    void testMappingBetweenDomainAndEntity() {
        productRepository.save(testProduct);

        Optional<ProductEntity> foundEntity = mongoRepository.findById("test2");

        assertTrue(foundEntity.isPresent());
        assertEquals("Domain Test Product", foundEntity.get().getName());
        assertEquals(200, foundEntity.get().getSalesUnits());

        ProductEntity newEntity = new ProductEntity("test3", "New Entity", 300, Map.of("XL", 15));
        mongoRepository.save(newEntity);

        Product foundProduct = productRepository.findById("test3");

        assertNotNull(foundProduct);
        assertEquals("New Entity", foundProduct.getName());
        assertEquals(300, foundProduct.getSalesUnits());
        assertEquals(15, foundProduct.getStock().get("XL"));
    }
}