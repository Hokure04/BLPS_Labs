package org.example.blps_lab1.authorization.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ApplicationResponseDto {
    private String description;
    private BigDecimal price;
}
