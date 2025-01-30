package com.example.Media.repository;

import com.example.Media.Entity.Media;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MediaRepository extends MongoRepository<Media, String> {
    Optional<Media> findMediaByProductId(String productId);
}
