package com.example.stably.dto;

import lombok.Data;

@Data
public class FormRequest {
    String symbol;
    Integer interval;
    Integer duration;
}
