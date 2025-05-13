package com.challenge.verticallogistics.model;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class Product {
    private Long productId;
    private BigDecimal value;
}