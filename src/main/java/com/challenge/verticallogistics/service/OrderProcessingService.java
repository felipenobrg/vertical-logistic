package com.challenge.verticallogistics.service;

import com.challenge.verticallogistics.dto.response.UserOrdersResponse;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface OrderProcessingService {
    List<UserOrdersResponse> processOrderFile(MultipartFile file);
    List<UserOrdersResponse> getAllOrders();
    List<UserOrdersResponse> getOrdersByOrderId(Long orderId);
    List<UserOrdersResponse> getOrdersByDateRange(LocalDate startDate, LocalDate endDate);
}