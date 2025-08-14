package com.example.productsorting.infrastructure.repository;

import com.example.productsorting.infrastructure.entity.ProductEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductMongoRepository extends MongoRepository<ProductEntity, String> {
}