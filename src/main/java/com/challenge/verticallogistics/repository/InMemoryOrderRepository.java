package com.challenge.verticallogistics.repository;


import com.challenge.verticallogistics.model.Order;
import com.challenge.verticallogistics.model.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryOrderRepository implements OrderRepository {

    private final Map<Long, User> userMap = new ConcurrentHashMap<>();

    @Override
    public void saveAll(List<User> users) {
        users.forEach(user -> userMap.put(user.getUserId(), user));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public List<User> findByOrderId(Long orderId) {
        return userMap.values().stream()
                .filter(user -> user.getOrders().stream()
                        .anyMatch(order -> order.getOrderId().equals(orderId)))
                .map(this::cloneUserWithFilteredOrders)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return userMap.values().stream()
                .filter(user -> user.getOrders().stream()
                        .anyMatch(order -> isOrderInDateRange(order, startDate, endDate)))
                .map(user -> filterUserOrdersByDateRange(user, startDate, endDate))
                .collect(Collectors.toList());
    }

    @Override
    public void clear() {
        userMap.clear();
    }

    private boolean isOrderInDateRange(Order order, LocalDate startDate, LocalDate endDate) {
        LocalDate orderDate = order.getDate();
        return (startDate == null || !orderDate.isBefore(startDate)) &&
                (endDate == null || !orderDate.isAfter(endDate));
    }

    private User filterUserOrdersByDateRange(User user, LocalDate startDate, LocalDate endDate) {
        List<Order> filteredOrders = user.getOrders().stream()
                .filter(order -> isOrderInDateRange(order, startDate, endDate))
                .collect(Collectors.toList());

        return User.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .orders(filteredOrders)
                .build();
    }

    private User cloneUserWithFilteredOrders(User user) {
        Long orderId = user.getOrders().stream()
                .filter(order -> user.getOrders().stream()
                        .anyMatch(o -> o.getOrderId().equals(order.getOrderId())))
                .findFirst()
                .map(Order::getOrderId)
                .orElse(null);

        List<Order> filteredOrders = user.getOrders().stream()
                .filter(order -> order.getOrderId().equals(orderId))
                .collect(Collectors.toList());

        return User.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .orders(filteredOrders)
                .build();
    }
}