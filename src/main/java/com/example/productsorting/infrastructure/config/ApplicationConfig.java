package com.example.productsorting.infrastructure.config;

import com.example.productsorting.application.service.ProductSortingService;
import com.example.productsorting.domain.repository.ProductRepository;
import com.example.productsorting.infrastructure.entity.ProductEntity;
import com.example.productsorting.infrastructure.repository.ProductMongoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import com.example.productsorting.domain.model.Product;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Configuration
@EnableMongoRepositories(basePackages = "com.example.productsorting.infrastructure.repository")
public class ApplicationConfig {

    @Bean
    public ProductSortingService productSortingService(ProductRepository productRepository) {
        return new ProductSortingService(productRepository);
    }

    /**
     * Initialises the database with sample data
     */
    @Bean
    public CommandLineRunner initDatabase(ProductMongoRepository repository) {
        return args -> {
            repository.deleteAll();

            // Crear productos de muestra
            List<Product> products = Arrays.asList(
                    createProduct("1", "V-NECH BASIC SHIRT", 100, Map.of("S", 4, "M", 9, "L", 0)),
                    createProduct("2", "CONTRASTING FABRIC T-SHIRT", 50, Map.of("S", 35, "M", 9, "L", 9)),
                    createProduct("3", "RAISED PRINT T-SHIRT", 80, Map.of("S", 20, "M", 2, "L", 20)),
                    createProduct("4", "PLEATED T-SHIRT", 3, Map.of("S", 25, "M", 30, "L", 10)),
                    createProduct("5", "CONTRASTING LACE T-SHIRT", 650, Map.of("S", 0, "M", 1, "L", 0)),
                    createProduct("6", "SLOGAN T-SHIRT", 20, Map.of("S", 9, "M", 2, "L", 5))
            );
            products.forEach(product -> repository.save(toProductEntity(product)));
        };
    }

    private Product createProduct(String id, String name, int salesUnits, Map<String, Integer> stock) {
        return new Product(id, name, salesUnits, stock);
    }

    private ProductEntity toProductEntity(Product product) {
        return new ProductEntity(
                product.getId(),
                product.getName(),
                product.getSalesUnits(),
                product.getStock()
        );
    }
}