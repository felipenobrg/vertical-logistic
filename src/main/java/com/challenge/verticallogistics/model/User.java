package com.challenge.verticallogistics.model;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class User {
    private Long userId;
    private String name;
    private List<Order> orders;
}