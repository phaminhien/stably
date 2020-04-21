package com.example.stably.entity;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Document("prices")
public class Price {
    String id;
    String symbol;
    BigDecimal bid;
    BigDecimal ask;
    BigDecimal spread;
    LocalDateTime createdOn;
}
