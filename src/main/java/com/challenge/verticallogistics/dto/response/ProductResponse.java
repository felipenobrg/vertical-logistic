package com.challenge.verticallogistics.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponse {
    private Long product_id;
    private String value;
}