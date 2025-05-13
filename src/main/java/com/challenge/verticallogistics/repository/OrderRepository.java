package com.challenge.verticallogistics.repository;

import com.challenge.verticallogistics.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    void saveAll(List<User> users);
    List<User> findAll();
    List<User> findByOrderId(Long orderId);
    List<User> findByDateRange(LocalDate startDate, LocalDate endDate);
    void clear();
}