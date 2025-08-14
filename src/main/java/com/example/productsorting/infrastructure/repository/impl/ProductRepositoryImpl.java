package com.example.productsorting.infrastructure.repository.impl;

import com.example.productsorting.domain.model.Product;
import com.example.productsorting.domain.repository.ProductRepository;
import com.example.productsorting.infrastructure.entity.ProductEntity;
import com.example.productsorting.infrastructure.repository.ProductMongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductMongoRepository mongoRepository;

    public ProductRepositoryImpl(ProductMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public List<Product> findAll() {
        return mongoRepository.findAll().stream()
                .map(this::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public Product findById(String id) {
        Optional<ProductEntity> entity = mongoRepository.findById(id);
        return entity.map(this::toDomainModel).orElse(null);
    }

    @Override
    public Product save(Product product) {
        ProductEntity entity = toEntity(product);
        ProductEntity savedEntity = mongoRepository.save(entity);
        return toDomainModel(savedEntity);
    }

    private Product toDomainModel(ProductEntity entity) {
        return new Product(
                entity.getId(),
                entity.getName(),
                entity.getSalesUnits(),
                entity.getStock()
        );
    }

    private ProductEntity toEntity(Product product) {
        return new ProductEntity(
                product.getId(),
                product.getName(),
                product.getSalesUnits(),
                product.getStock()
        );
    }
}