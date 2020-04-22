package com.example.stably.repository;

import com.example.stably.entity.Price;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PriceRepository extends MongoRepository<Price, Integer> {
    Page<Price> findBySymbolAndCreatedOnIn(String symbol, List<LocalDateTime> dateTimes, Pageable pageable);
    Optional<Price> findBySymbolAndCreatedOn(String Symbol, LocalDateTime createdOn);
}
