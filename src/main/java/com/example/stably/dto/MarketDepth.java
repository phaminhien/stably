package com.example.stably.dto;

import lombok.Data;

import java.util.List;

@Data
public class MarketDepth {
    List<List<String>> asks;
    List<List<String>> bids;
}
