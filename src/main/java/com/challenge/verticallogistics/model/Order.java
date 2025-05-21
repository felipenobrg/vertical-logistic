package com.challenge.verticallogistics.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class Order {
    private Long orderId;
    private BigDecimal total;
    private LocalDate date;
    private List<Product> products;
}