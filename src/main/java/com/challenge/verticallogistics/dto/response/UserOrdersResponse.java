package com.challenge.verticallogistics.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class UserOrdersResponse {
    private Long user_id;
    private String name;
    private List<OrderResponse> orders;
}