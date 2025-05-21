package com.challenge.verticallogistics.model;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class OrderLine {
    private String userId;
    private String userName;
    private String orderId;
    private String productId;
    private String productValue;
    private String purchaseDate;
}
