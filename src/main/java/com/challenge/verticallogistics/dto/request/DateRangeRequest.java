package com.challenge.verticallogistics.dto.request;


import lombok.Data;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.constraints.Pattern;

@Data
public class DateRangeRequest {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    @Pattern(regexp = "^\\d+$", message = "Order ID deve ser numerico")
    private String orderId;
}