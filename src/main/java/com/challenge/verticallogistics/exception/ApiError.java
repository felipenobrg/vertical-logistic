package com.challenge.verticallogistics.exception;


import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ApiError {
    private String message;
    private String details;
    private int status;
    private LocalDateTime timestamp;
}