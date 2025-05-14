package com.challenge.verticallogistics.dto.request;


import lombok.Data;
import java.time.LocalDate;

@Data
public class DateRangeRequest {
    private String orderId;
    private LocalDate startDate;
    private LocalDate endDate;
}