package com.example.stably.repository;

import com.example.stably.entity.Price;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface PriceRepository extends MongoRepository<Price, Integer> {
    Price findFirstBySymbolAndCreatedOnBetweenOrderByCreatedOnDesc(String Symbol, LocalDateTime start, LocalDateTime end);
}
