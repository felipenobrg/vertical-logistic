package com.challenge.verticallogistics.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserOrdersResponse {
    private Long user_id;
    private String name;
    private List<OrderResponse> orders;
}