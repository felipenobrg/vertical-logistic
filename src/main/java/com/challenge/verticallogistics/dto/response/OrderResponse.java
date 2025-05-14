package com.challenge.verticallogistics.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class OrderResponse {
    private Long order_id;
    private String total;
    private String date;
    private List<ProductResponse> products;
}